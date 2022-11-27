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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.voice.impl

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.ydwk.voice.VoiceConnection
import io.github.ydwk.ydwk.ws.voice.VoiceWebSocket
import io.github.ydwk.ydwk.ws.voice.util.SpeakingFlag
import java.net.DatagramSocket
import java.util.*
import java.util.concurrent.CompletableFuture

data class VoiceConnectionImpl(
    var channel: GuildVoiceChannel,
    val ydwk: YDWK,
    val future: CompletableFuture<VoiceConnection>,
    override var isMuted: Boolean,
    override var isDeafened: Boolean,
) : VoiceConnection {
    var token: String? = null
    var sessionId: String? = null
    var voiceEndpoint: String? = null
    var userId: Long? = null
    override val speakingFlags: EnumSet<SpeakingFlag> = EnumSet.noneOf(SpeakingFlag::class.java)
    private var disconnectFuture: CompletableFuture<Void> = CompletableFuture()
    private var changedChannelFuture: CompletableFuture<Void>? = CompletableFuture()
    private var voiceWebSocket: VoiceWebSocket? = null
    var udpsocket: DatagramSocket? = null
    private var attemptingToConnectOrConnected = false

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

    override fun disconnect(): CompletableFuture<Void> {
        disconnectFuture = CompletableFuture()
        voiceWebSocket?.close()
        ydwk.webSocketManager?.sendVoiceStateUpdate(channel.guild, null, isDeafened, isMuted)
        channel.guild.removeVoiceConnection(this)
        return disconnectFuture
    }

    @Synchronized
    fun attemptConnect(): Boolean {
        if (changedChannelFuture != null && !changedChannelFuture!!.isDone) {
            changedChannelFuture!!.complete(null)
        }

        if (attemptingToConnectOrConnected ||
            sessionId == null ||
            token == null ||
            voiceEndpoint == null ||
            userId == null) {
            return false
        }

        attemptingToConnectOrConnected = true
        voiceWebSocket = VoiceWebSocket(this)
        channel =
            ydwk.getGuildChannelById(channel.id)?.guildChannelGetter?.asGuildVoiceChannel()
                ?: channel
        return true
    }
}
