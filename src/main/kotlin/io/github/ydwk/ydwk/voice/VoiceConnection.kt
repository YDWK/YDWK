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
import io.github.ydwk.ydwk.ws.voice.util.SpeakingFlag
import java.util.*

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
     * Gets the speaking flags.
     *
     * @return The speaking flags.
     */
    val speakingFlags: EnumSet<SpeakingFlag>

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
