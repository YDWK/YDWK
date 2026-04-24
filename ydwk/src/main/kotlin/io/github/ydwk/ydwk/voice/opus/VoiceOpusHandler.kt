/*
 * Copyright 2024-2026 YDWK inc.
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
package io.github.ydwk.ydwk.voice.opus

import org.jitsi.impl.neomedia.codec.audio.opus.Opus
import org.slf4j.LoggerFactory

/**
 * Handles Opus audio encoding for Discord voice.
 *
 * Expects raw 16-bit signed little-endian stereo PCM at 48 kHz.
 * Each call to [encode] must receive exactly [PCM_FRAME_BYTES] bytes (20 ms of audio).
 */
class VoiceOpusHandler {

    companion object {
        const val SAMPLE_RATE = 48000
        const val CHANNELS = 2
        const val FRAME_SIZE = 960              // samples per channel in 20 ms
        const val PCM_FRAME_BYTES = FRAME_SIZE * CHANNELS * 2  // 3840 bytes
        const val MAX_OPUS_PACKET_SIZE = 1275   // max per RFC 6716 §3.1.2

        private val logger = LoggerFactory.getLogger(VoiceOpusHandler::class.java)
    }

    private var encoder: Long = 0L

    var isInitialized: Boolean = false
        private set

    fun initialize(): Boolean {
        if (isInitialized) return true
        return try {
            encoder = Opus.encoder_create(SAMPLE_RATE, CHANNELS)
            Opus.encoder_set_bitrate(encoder, 64000)
            isInitialized = true
            true
        } catch (t: Throwable) {
            logger.warn("Opus native library unavailable — audio will not be encoded: {}", t.message)
            false
        }
    }

    /**
     * Encodes one 20-ms frame of raw PCM ([PCM_FRAME_BYTES] bytes) to an Opus packet.
     *
     * @param pcm Raw signed 16-bit little-endian stereo PCM, exactly [PCM_FRAME_BYTES] bytes.
     * @return Opus-encoded bytes, or an empty array on encoding failure.
     */
    fun encode(pcm: ByteArray): ByteArray {
        check(isInitialized) { "VoiceOpusHandler not initialized" }
        require(pcm.size == PCM_FRAME_BYTES) {
            "Expected $PCM_FRAME_BYTES PCM bytes, got ${pcm.size}"
        }
        val output = ByteArray(MAX_OPUS_PACKET_SIZE)
        val length = Opus.encode(encoder, pcm, 0, FRAME_SIZE, output, 0, output.size)
        return if (length > 0) output.copyOf(length) else ByteArray(0)
    }

    fun destroy() {
        if (isInitialized) {
            Opus.encoder_destroy(encoder)
            isInitialized = false
        }
    }
}
