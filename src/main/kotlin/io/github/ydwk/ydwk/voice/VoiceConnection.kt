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
package io.github.ydwk.ydwk.voice

import io.github.ydwk.ydwk.entities.VoiceState

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
     * Disconnects the bot from the voice channel.
     *
     * @return The [VoiceConnection] object.
     */
    fun disconnect(): VoiceConnection

    /**
     * Gets the [VoiceState] of the bot.
     *
     * @return The [VoiceState] of the bot.
     */
    val voiceState: VoiceState
}
