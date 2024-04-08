/*
 * Copyright 2024 YDWK inc.
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

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.ydwk.entity.VoiceConnection
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.util.ydwk
import io.github.ydwk.ydwk.voice.VoiceSource
import io.github.ydwk.ydwk.voice.removeVoiceConnection
import io.github.ydwk.ydwk.ws.voice.VoiceWebSocket
import io.github.ydwk.ydwk.ws.voice.payload.VoiceReadyPayload
import io.github.ydwk.ydwk.ws.voice.payload.VoiceServerUpdatePayload
import io.github.ydwk.ydwk.ws.voice.util.SpeakingFlag
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Represents a voice connection implementation.
 *
 * @param connectFuture A CompletableFuture that represents the future result of connecting to a
 *   voice channel.
 * @param guild The guild that the voice connection belongs to.
 * @param voiceChannel The voice channel that the voice connection is connected to.
 * @param muted Indicates whether the voice connection is muted or not.
 * @param deafen Indicates whether the voice connection is deafened or not.
 */

// TODO: Rework the voice system so it can send and receive voice data
// For example have a class for MP3 and it breaks it down into packets and sends it to the voice
// server
data class VoiceConnectionImpl(
    val connectFuture: CompletableFuture<VoiceConnection>,
    val guild: Guild,
    var voiceChannel: GuildVoiceChannel,
    var muted: Boolean,
    var deafen: Boolean
) : VoiceConnection {
    val ydwk = voiceChannel.ydwk
    private var voiceServerUpdatePayload: VoiceServerUpdatePayload? = null
    private var voiceState: VoiceState? = null
    private var voiceWebSocket: VoiceWebSocket? = null
    private var disconnectFuture: CompletableFuture<Void> = CompletableFuture()
    private var changedChannelFuture: CompletableFuture<Void>? = CompletableFuture()
    private var attemptingToConnectOrConnected = false
    private var voiceReadyPayload: VoiceReadyPayload? = null
    private var voiceSource: VoiceSource? = null
    private val speakingFlags: EnumSet<SpeakingFlag> = EnumSet.noneOf(SpeakingFlag::class.java)

    init {
        ydwk.webSocketManager?.sendVoiceState(guild.idAsLong, voiceChannel.idAsLong, muted, deafen)
    }

    /**
     * Sets the Voice Server Update Payload.
     *
     * @param payload The Voice Server Update Payload to set.
     */
    fun setVoiceServerUpdatePayload(payload: VoiceServerUpdatePayload) {
        this.voiceServerUpdatePayload = payload
    }

    /**
     * Sets the voice state of the current instance.
     *
     * @param voiceState the voice state to be set.
     */
    fun setVoiceState(voiceState: VoiceState) {
        this.voiceState = voiceState
    }

    /**
     * Retrieves the payload for a voice server update.
     *
     * @return the voice server update payload, or null if none is found.
     */
    fun getVoiceServerUpdatePayload(): VoiceServerUpdatePayload? {
        return voiceServerUpdatePayload
    }

    /**
     * Retrieves the current voice state.
     *
     * @return The voice state of the user, or null if the voice state has not been set.
     */
    fun getVoiceState(): VoiceState? {
        return voiceState
    }

    /**
     * Sets the voice ready payload.
     *
     * @param voiceReadyPayload The voice ready payload to set.
     */
    fun setVoiceReadyPayload(voiceReadyPayload: VoiceReadyPayload) {
        this.voiceReadyPayload = voiceReadyPayload
    }

    /**
     * Retrieves the VoiceReadyPayload object.
     *
     * @return The VoiceReadyPayload object, or null if it hasn't been set yet.
     */
    fun getVoiceReadyPayload(): VoiceReadyPayload? {
        return voiceReadyPayload
    }

    /**
     * Sets the deafened state of the voice connection.
     *
     * @param deafened true to deafen the voice connection, false to undeafen
     * @return the updated voice connection
     */
    override fun setDeafened(deafened: Boolean): VoiceConnection {
        this.deafen = deafened
        return this
    }

    /**
     * Sets the muted state of the voice connection.
     *
     * @param muted true if the voice connection should be muted, false otherwise
     * @return the modified VoiceConnection object
     */
    override fun setMuted(muted: Boolean): VoiceConnection {
        this.muted = muted
        return this
    }

    fun getSpeakingFlags(): EnumSet<SpeakingFlag> {
        return speakingFlags
    }

    /**
     * Determines whether the speaker has priority.
     *
     * @return `true` if the speaker has priority, `false` otherwise.
     */
    override fun isPrioritySpeaker(): Boolean {
        return speakingFlags.contains(SpeakingFlag.PRIORITY)
    }

    /**
     * Set the priority of the VoiceConnection.
     *
     * @param priority the priority value to be set. True indicates high priority, False indicates
     *   normal priority.
     * @return the modified VoiceConnection object.
     */
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

    /**
     * Check if the engine is currently speaking.
     *
     * @return true if the engine is speaking, false otherwise.
     */
    override fun isSpeaking(): Boolean {
        return speakingFlags.contains(SpeakingFlag.MICROPHONE)
    }

    /**
     * Sets the speaking state of the voice connection.
     *
     * @param speaking The speaking state to set. `true` to indicate that the connection is
     *   speaking, `false` otherwise.
     * @return The updated VoiceConnection with the speaking state set.
     */
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

    fun getVoiceSource(): VoiceSource? {
        return voiceSource
    }

    /**
     * Sets the source for the voice connection.
     *
     * @param source the voice source to set
     * @return the updated voice connection object
     */
    override fun setSource(source: VoiceSource): CompletableFuture<VoiceConnection> {
        this.voiceSource = source
        return CompletableFuture.completedFuture(this)
    }

    /**
     * Establishes a connection to a voice channel.
     *
     * If there is a pending channel change (stored in `changedChannelFuture`), it is completed with
     * `null`.
     *
     * If the client is already attempting to connect or is already connected to a voice channel, a
     * warning message is logged and the method returns.
     *
     * Otherwise, a new `VoiceWebSocket` is created with the current `this` instance and the
     * `etfInsteadOfJson` flag from the `webSocketManager` (defaulting to `false` if not set) and a
     * connection is established by calling the `connect()` method on the websocket.
     *
     * The `voiceChannel` field is updated with the latest information from
     * `ydwk.getGuildChannelById()` (if available) or remains unchanged.
     *
     * Finally, the `connectFuture` is completed with `this` instance.
     */
    fun connect() {
        if (changedChannelFuture != null && !changedChannelFuture!!.isDone) {
            changedChannelFuture!!.complete(null)
        }

        if (attemptingToConnectOrConnected) {
            (ydwk as YDWKImpl)
                .logger
                .warn("Attempted to connect to a voice channel that was already connected to.")
            return // We are already connecting or we don't have enough information to connect
        }

        attemptingToConnectOrConnected = true

        voiceWebSocket =
            VoiceWebSocket(this, ydwk.webSocketManager?.etfInsteadOfJson ?: false).connect()

        voiceChannel =
            ydwk.getGuildChannelById(voiceChannel.id)?.guildChannelGetter?.asGuildVoiceChannel()
                ?: voiceChannel // Update the channel in case it was updated

        connectFuture.complete(this)
    }

    override fun disconnect(): CompletableFuture<Void> {
        return if (attemptingToConnectOrConnected) {
            disconnectFuture = CompletableFuture()
            voiceWebSocket?.triggerDisconnect()

            ydwk.webSocketManager?.sendVoiceState(guild.idAsLong, null, false, false)

            (guild).removeVoiceConnection()

            attemptingToConnectOrConnected = false
            disconnectFuture
        } else {
            (ydwk as YDWKImpl)
                .logger
                .warn("Attempted to disconnect from a voice channel that was not connected to.")
            CompletableFuture.completedFuture(null)
        }
    }
}
