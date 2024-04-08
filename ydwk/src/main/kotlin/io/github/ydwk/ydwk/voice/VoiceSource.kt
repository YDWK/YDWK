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
package io.github.ydwk.ydwk.voice

import java.nio.ByteBuffer

/** Represents a source of voice data. */
interface VoiceSource {

    /**
     * Gets the original audio data as a byte buffer.
     *
     * @return the audio data as a byte buffer.
     */
    fun getOriginalAudio(): ByteBuffer

    /**
     * Retrieves the next chunk of audio data.
     *
     * @return the audio data as a byte array, representing a chunk of audio.
     */
    fun getNextAudioChunk(): ByteArray

    /**
     * Indicates whether the audio has finished playing.
     *
     * @return true if the audio has finished, false otherwise.
     */
    val isFinished: Boolean

    /**
     * Whether the voice source is already encoded with Opus. (false by default)
     *
     * @return true if the voice source is already encoded with Opus, false otherwise.
     */
    val isOpusEncoded: Boolean
        get() = false
}
