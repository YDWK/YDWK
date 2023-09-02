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
package io.github.ydwk.ydwk.voice

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

/** Represents a source of voice data. */
interface VoiceSource {

    /**
     * Retrieves the audio data.
     *
     * @return the audio data as a byte array.
     */
    fun getAudioData(): ByteArray

    /**
     * Indicates whether the audio has finished playing.
     *
     * @return true if the audio has finished, false otherwise.
     */
    val isFinished: Boolean

    /**
     * Builds a VoiceSource object.
     *
     * @return a VoiceSource object representing the built voice source.
     */
    fun build(): VoiceSource

    class ConvertVoiceSource(val file: File) : VoiceSource {

        private val outputStream = ByteArrayOutputStream()
        private val inputStream = FileInputStream(file)

        private lateinit var audioData: ByteArray

        override fun getAudioData(): ByteArray {
            val data = outputStream.toByteArray()
            outputStream.reset() // Clear output stream

            return data
        }

        override val isFinished: Boolean
            get() =
                with(inputStream) {
                    if (available() == 0) {
                        close()
                        true
                    } else {
                        false
                    }
                }

        override fun build(): VoiceSource {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            audioData = outputStream.toByteArray()

            return this
        }
    }
}
