/*
 * Copyright 2022 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.voice.impl

import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.impl.YDWKImpl
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.voice.VoiceConnection
import io.github.ydwk.ydwk.voice.impl.util.VoicePacket
import io.github.ydwk.ydwk.voice.impl.util.removeVoiceConnection
import io.github.ydwk.ydwk.voice.sub.VoiceSource
import io.github.ydwk.ydwk.ws.voice.VoiceWebSocket
import io.github.ydwk.ydwk.ws.voice.util.SpeakingFlag
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicLong

data class VoiceConnectionImpl(
    override var channel: GuildVoiceChannel,
    val ydwk: YDWK,
    val future: CompletableFuture<VoiceConnection>? = null,
    override var isMuted: Boolean = false,
    override var isDeafened: Boolean = false,
) : VoiceConnection {
    override val speakingFlags: EnumSet<SpeakingFlag> = EnumSet.noneOf(SpeakingFlag::class.java)
    private var currentSource: BlockingQueue<VoiceSource> = LinkedBlockingDeque()
    private val logger = (ydwk as YDWKImpl).logger
    private var sendAudio = false
    private var sequence: Char = 0.toChar()

    private var disconnectFuture: CompletableFuture<Void> = CompletableFuture()
    private var changedChannelFuture: CompletableFuture<Void>? = CompletableFuture()
    private var voiceWebSocket: VoiceWebSocket? = null
    var udpsocket: DatagramSocket? = null
    private var attemptingToConnectOrConnected = false

    var voiceEndpoint: String? = null
    var token: String? = null
    var sessionId: String? = null
    var userId: String? = null
    var address: InetSocketAddress? = null

    init {
        ydwk.webSocketManager?.sendVoiceStateUpdate(channel.guild, channel, isMuted, isDeafened)
    }

    override fun setDeafened(deafened: Boolean): VoiceConnection {
        this.isDeafened = deafened
        ydwk.webSocketManager?.sendVoiceStateUpdate(channel.guild, channel, isDeafened, isMuted)
        return this
    }

    override fun setMuted(muted: Boolean): VoiceConnection {
        this.isMuted = muted
        ydwk.webSocketManager?.sendVoiceStateUpdate(channel.guild, channel, isDeafened, isMuted)
        return this
    }

    override fun isPrioritySpeaker(): Boolean {
        return speakingFlags.contains(SpeakingFlag.PRIORITY)
    }

    override fun setPriority(priority: Boolean): VoiceConnection {
        val newSpeakingFlags = speakingFlags.clone()
        if (priority) {
            newSpeakingFlags.add(SpeakingFlag.PRIORITY)
        } else {
            newSpeakingFlags.remove(SpeakingFlag.PRIORITY)
        }
        speakingFlags.addAll(newSpeakingFlags)
        return this
    }

    override fun isSpeaking(): Boolean {
        return speakingFlags.contains(SpeakingFlag.MICROPHONE)
    }

    override fun setSpeaking(speaking: Boolean): VoiceConnection {
        val newSpeakingFlags = speakingFlags.clone()
        if (speaking) {
            newSpeakingFlags.add(SpeakingFlag.MICROPHONE)
        } else {
            newSpeakingFlags.remove(SpeakingFlag.MICROPHONE)
        }
        speakingFlags.addAll(newSpeakingFlags)
        return this
    }

    override fun setSource(source: VoiceSource): VoiceConnection {
        currentSource.add(source)
        return this
    }

    fun sendSendingAudio() {
        val socket = udpsocket
        if (socket == null) {
            logger.error("UDP socket is null")
            return
        }
        if (sendAudio) {
            return
        }
        sendAudio = true
        var isSpeaking = false
        var framesOfSilenceToPlay = 5
        var dontSleep = true
        var nextFrameTime = AtomicLong(System.nanoTime())
        ydwk.threadFactory.createThreadExecutor("Sending Audio").execute {
            try {
                while (sendAudio) {
                    val source = currentSource.poll() ?: return@execute
                    if (source.isFinished) {
                        currentSource.remove()
                        dontSleep = true
                        continue
                    }

                    var voicePacket: VoicePacket? = null
                    var frame: ByteArray? =
                        if (source.isNextAudioAvailable) source.nextAudio else null
                    if (source.isPaused) {
                        frame = null
                    }

                    sequence++
                    if (frame != null || framesOfSilenceToPlay > 0) {
                        if (!isSpeaking && frame != null) {
                            isSpeaking = true
                            setSpeaking(true)
                        }

                        voicePacket =
                            VoicePacket(
                                frame, voiceWebSocket?.ssrc!!, sequence, (sequence.code * 960))

                        if (frame == null) {
                            framesOfSilenceToPlay--
                            if (framesOfSilenceToPlay == 0) {
                                isSpeaking = false
                                setSpeaking(false)
                            }
                        } else {
                            framesOfSilenceToPlay = 5
                        }
                    }
                    nextFrameTime.set(nextFrameTime.get() + 20_000_000)

                    voicePacket?.encrypt(voiceWebSocket?.secretKey)
                    try {
                        if (dontSleep) {
                            nextFrameTime = AtomicLong(System.nanoTime() + 20_000_000)
                            dontSleep = false
                        } else {
                            val sleepTime = nextFrameTime.get() - System.nanoTime()
                            if (sleepTime > 0) {
                                Thread.sleep(sleepTime / 1_000_000, (sleepTime % 1_000_000).toInt())
                            }
                        }
                        voicePacket?.let { socket.send(voicePacket.asDatagramPacket(address!!)) }
                    } catch (e: InterruptedException) {
                        logger.error("Failed to send audio", e)
                    }
                }
            } catch (e: InterruptedException) {
                if (sendAudio) {
                    logger.error("Failed to send audio", e)
                }
            }
        }
    }

    fun stopSendingAudio() {
        sendAudio = false
        ydwk.threadFactory.getThreadExecutorByName("Sending Audio")?.shutdown()
    }

    override fun disconnect(): CompletableFuture<Void> {
        return if (attemptingToConnectOrConnected) {
            disconnectFuture = CompletableFuture()
            voiceWebSocket?.close()
            ydwk.webSocketManager?.sendVoiceStateUpdate(channel.guild, null, isDeafened, isMuted)
            (channel.guild as GuildImpl).removeVoiceConnection()
            attemptingToConnectOrConnected = false
            disconnectFuture
        } else {
            (ydwk as YDWKImpl)
                .logger
                .warn("Attempted to disconnect from a voice channel that was not connected to.")
            CompletableFuture.completedFuture(null)
        }
    }

    fun safeConnect(endPoint: String, token: String, sessionId: String, userId: Long) {
        if (changedChannelFuture != null && !changedChannelFuture!!.isDone) {
            changedChannelFuture!!.complete(null)
        }

        if (attemptingToConnectOrConnected) {
            (ydwk as YDWKImpl)
                .logger
                .warn("Attempted to connect to a voice channel that was already connected to.")
            return // We are already connecting or we don't have enough information to connect
        }

        this.voiceEndpoint = endPoint
        this.token = token
        this.sessionId = sessionId
        this.userId = userId.toString()

        attemptingToConnectOrConnected = true
        connect()
    }

    private fun connect() {
        voiceWebSocket = VoiceWebSocket(this)
        channel =
            ydwk.getGuildChannelById(channel.id)?.guildChannelGetter?.asGuildVoiceChannel()
                ?: channel // Update the channel in case it was updated
        future?.complete(this)
    }
}
