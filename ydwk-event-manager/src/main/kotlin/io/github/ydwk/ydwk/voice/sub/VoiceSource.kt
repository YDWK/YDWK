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
package io.github.ydwk.ydwk.voice.sub

import javax.sound.sampled.AudioFormat

interface VoiceSource {
    /**
     * The input format used by YDWK. This is the format that the audio is converted to before being
     * sent to Discord.
     */
    val inputFormat: AudioFormat
        get() = AudioFormat(48000f, 16, 2, true, true)

    /**
     * Add an audio frame to the queue.
     *
     * @param frame The audio frame to add.
     * @return Whether the frame was added successfully.
     */
    fun addAudio(frame: ByteArray): Boolean

    /**
     * Gets the next audio frame.
     *
     * @return The next audio frame.
     */
    val nextAudio: ByteArray?

    /**
     * Whether the next audio frame is available.
     *
     * @return Whether the next audio frame is available.
     */
    val isNextAudioAvailable: Boolean

    /**
     * Whether the source is paused.
     *
     * @return Whether the source is paused.
     */
    val isPaused: Boolean

    /**
     * Pauses the source.
     *
     * @return The [VoiceSource] object.
     */
    fun pause(): VoiceSource

    /**
     * Resumes the source.
     *
     * @return The [VoiceSource] object.
     */
    fun resume(): VoiceSource

    /**
     * Whether the source is finished.
     *
     * @return Whether the source is finished.
     */
    val isFinished: Boolean

    /** Clear the source. */
    fun clear()
}
