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
package io.github.ydwk.ydwk.entity

import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.ydwk.voice.VoiceSource
import java.util.concurrent.CompletableFuture

interface VoiceConnection {
    /**
     * Sets the bot as deafened.
     *
     * @param deafened Whether the bot should be deafened.
     * @return The [VoiceConnection] object.
     */
    fun setDeafened(deafened: Boolean): VoiceConnection

    /**
     * Sets the bot as muted.
     *
     * @param muted Whether the bot should be muted.
     * @return The [VoiceConnection] object.
     */
    fun setMuted(muted: Boolean): VoiceConnection

    /**
     * Whether the bot is priority speaker.
     *
     * @return Whether the bot is priority speaker.
     */
    fun isPrioritySpeaker(): Boolean

    /**
     * Set as the priority speaker.
     *
     * @param priority Whether the bot should be the priority speaker.
     * @return The [VoiceConnection] object.
     */
    fun setPriority(priority: Boolean): VoiceConnection

    /**
     * Whether the bot is currently speaking.
     *
     * @return Whether the bot is currently speaking.
     */
    fun isSpeaking(): Boolean

    /**
     * Sets the bot as speaking.
     *
     * @param speaking Whether the bot should be speaking.
     * @return The [VoiceConnection] object.
     */
    fun setSpeaking(speaking: Boolean): VoiceConnection

    /**
     * Sets the VoiceSource for the bot to use.
     *
     * @param source The [VoiceSource] to use.
     * @return The [VoiceConnection] object.
     */
    fun setSource(source: VoiceSource): RestResult<VoiceConnection>

    /**
     * Disconnects from the voice channel.
     *
     * @return A CompletableFuture that represents the asynchronous operation.
     */

    //TODO: change to RestResult<Void>
    fun disconnect(): CompletableFuture<Void>
}
