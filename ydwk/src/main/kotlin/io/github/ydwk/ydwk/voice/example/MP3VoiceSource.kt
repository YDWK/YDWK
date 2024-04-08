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
package io.github.ydwk.ydwk.voice.example

import io.github.ydwk.ydwk.voice.VoiceSource
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

/** Handles mp3 players. */
class MP3VoiceSource(private val audioFilePath: File) : VoiceSource {

    private val audioInputStream: AudioInputStream
    private val audioFormat: AudioFormat
    private val bufferSize: Int

    init {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioFilePath)
            // Extract audio format from the input stream
            audioFormat = audioInputStream.format
            // Set the buffer size based on audio format
            bufferSize =
                audioFormat.sampleRate.toInt() * audioFormat.frameSize * BUFFER_DURATION / 1000
        } catch (ex: Exception) {
            throw IOException("Failed to initialize MP3VoiceSource for file: $audioFilePath", ex)
        }
    }

    override fun getNextAudioChunk(): ByteArray {
        getOriginalAudio().let { buffer ->
            return buffer.array().copyOf(buffer.remaining())
        }
    }

    override fun getOriginalAudio(): ByteBuffer {
        val buffer = ByteArray(bufferSize)
        try {
            val bytesRead = audioInputStream.read(buffer)
            return if (bytesRead != -1) ByteBuffer.wrap(buffer.copyOf(bytesRead))
            else ByteBuffer.wrap(byteArrayOf())
        } catch (ex: IOException) {
            throw IOException("Error reading audio data from MP3 file: $audioFilePath", ex)
        }
    }

    override val isFinished: Boolean
        get() = audioInputStream.available() <= 0

    companion object {
        // Duration of audio buffer in milliseconds
        private const val BUFFER_DURATION = 20
    }
}
