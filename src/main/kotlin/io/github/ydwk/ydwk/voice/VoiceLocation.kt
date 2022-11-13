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

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.util.AssignableEntity

interface VoiceLocation : AssignableEntity<VoiceLocation> {

    /**
     * Gets the main instance of YDWK.
     *
     * @return The main instance of YDWK.
     */
    val ydwk: YDWK

    /**
     * Weather the next frame is available.
     *
     * @return true if the next frame is available.
     */
    fun hasNext(): Boolean

    /**
     * Gets the next frame.
     *
     * @return The next frame.
     */
    fun next(): ByteArray

    /**
     * Check fif the track has finished playing.
     *
     * @return True if the track has finished playing.
     */
    fun isFinished(): Boolean

    /**
     * Mutes the track.
     *
     * @return The track that was muted.
     */
    fun mute(): VoiceLocation

    /**
     * Unmutes the track.
     *
     * @return The track that was unmuted.
     */
    fun unmute(): VoiceLocation

    /**
     * Weather the track is muted or not.
     *
     * @return True if the track is muted.
     */
    fun isMuted(): Boolean

    /**
     * Gets a copy of this voice location.
     *
     * @return A copy of this voice location.
     */
    fun copy(): VoiceLocation
}
