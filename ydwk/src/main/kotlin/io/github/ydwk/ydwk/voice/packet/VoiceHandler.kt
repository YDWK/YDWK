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
package io.github.ydwk.ydwk.voice.packet

import club.minnced.opus.util.OpusLibrary
import com.iwebpp.crypto.TweetNaclFast
import com.sun.jna.ptr.PointerByReference
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.voice.VoiceSource
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.ws.voice.util.VoiceEncryption
import java.net.DatagramPacket
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.concurrent.ThreadLocalRandom
import tomp2p.opuswrapper.Opus

class VoiceHandler(
    private val voiceConnectionImpl: VoiceConnectionImpl,
    private val box: TweetNaclFast.SecretBox
) {
    private var opusEncoder: PointerByReference? = null

    private var opusLibraryStated = false
    private var opusLibrarySupported = false

    private var sequence: Char = 0.toChar() // Sequence number for voice packets.
    private var timestamp = 0 // Timestamp for voice packets.
    private var buffer = ByteBuffer.allocate(512) // Buffer for audio data
    private var encryptionBuffer = ByteBuffer.allocate(512) // Buffer for encrypted audio data
    private var nonce: Long = 0 // Nonce for encryption
    private var maxNonce = 4294967295L // Maximum nonce value 32 bit
    private var nonceBuffer = ByteArray(TweetNaclFast.SecretBox.nonceLength)

    private var voiceSource: VoiceSource? = null

    init {
        val error = IntBuffer.allocate(1)
        opusEncoder = Opus.INSTANCE.opus_encoder_create(48000, 2, Opus.OPUS_APPLICATION_VOIP, error)
        if (error.get() != Opus.OPUS_OK) {
            throw IllegalStateException(
                "Could not create Opus encoder: ${Opus.INSTANCE.opus_strerror(error.get())}")
        }
    }

    private fun encode(audioData: ByteBuffer): ByteBuffer? {
        // Convert byte buffer to short buffer
        val shortBuffer = audioData.asShortBuffer()

        // Allocate buffer for encoded data
        val encodedSize = audioData.remaining() * 2 // Each short is 2 bytes
        val encodedData = ByteBuffer.allocate(encodedSize)

        // Encode audio using Opus
        val result =
            Opus.INSTANCE.opus_encode(
                opusEncoder, shortBuffer, audioData.remaining() / 2, encodedData, encodedSize)
        if (result < 0) {
            // Error handling
            println("Error encoding audio: ${Opus.INSTANCE.opus_strerror(result)}")
            return null
        }

        // Set buffer position and limit
        (encodedData as Buffer).position(0).limit(result)

        return encodedData
    }

    private fun encodeAudio(audio: ByteBuffer): ByteBuffer? {
        if (opusEncoder == null) {
            if (!checkOpusLibrary()) {
                (voiceConnectionImpl.ydwk as YDWKImpl)
                    .logger
                    .error("Opus library has failed to load")
                return null
            }

            val error = IntBuffer.allocate(1)
            opusEncoder =
                Opus.INSTANCE.opus_encoder_create(48000, 2, Opus.OPUS_APPLICATION_VOIP, error)
            if (error.get() != Opus.OPUS_OK) {
                throw IllegalStateException(
                    "Could not create Opus encoder: ${Opus.INSTANCE.opus_strerror(error.get())}")
            }
        }

        return encode(audio)
    }

    fun getNextFrame(): DatagramPacket? {
        val nextFrameRaw = getNextFrameRaw() ?: return null
        return if (buffer != null) {
            val data = nextFrameRaw.array()
            val offset = nextFrameRaw.arrayOffset() + nextFrameRaw.position()
            val length = nextFrameRaw.remaining()
            DatagramPacket(data, offset, length, voiceConnectionImpl.getAddress())
        } else {
            null
        }
    }

    private fun getNextFrameRaw(): ByteBuffer? {
        var nextFrame: ByteBuffer? = null

        try {
            if (voiceSource != null) {
                var audio = voiceSource!!.getOriginalAudio()
                if (!voiceSource!!.isOpusEncoded) {
                    audio = encodeAudio(audio) ?: return null
                }

                nextFrame = encryptAudio(audio)

                if (sequence + 1 > Character.MAX_VALUE) {
                    sequence = 0.toChar()
                } else {
                    sequence++
                }
            }
        } catch (e: Exception) {
            (voiceConnectionImpl.ydwk as YDWKImpl).logger.error("Failed to get next frame", e)
        }

        if (nextFrame != null) {
            timestamp += 960
        }

        return nextFrame
    }

    @Synchronized
    private fun checkOpusLibrary(): Boolean {
        if (opusLibraryStated) {
            return opusLibrarySupported
        }

        opusLibraryStated = true

        try {
            if (OpusLibrary.isInitialized()) {
                opusLibrarySupported = true
            }

            opusLibrarySupported = OpusLibrary.loadFromJar()
        } catch (e: UnsatisfiedLinkError) {
            opusLibrarySupported = false
            throw IllegalStateException("Could not load Opus library")
        }

        return opusLibrarySupported
    }

    fun close() {
        // Clean up resources
        if (opusEncoder != null) {
            Opus.INSTANCE.opus_encoder_destroy(opusEncoder)
            opusEncoder = null
        }
    }

    private fun encryptAudio(audio: ByteBuffer): ByteBuffer {
        assertEncrypted(audio)
        val voicePacket =
            VoicePacket(
                audio,
                sequence,
                timestamp,
                voiceConnectionImpl.getVoiceReadyPayload()?.ssrc ?: 0,
                encryptionBuffer)
        var nonceLength = 0

        val voiceReadyPayload = voiceConnectionImpl.getVoiceReadyPayload()

        if (voiceReadyPayload == null) {
            (voiceConnectionImpl.ydwk as YDWKImpl)
                .logger
                .error("Voice ready payload was not handled, please re run the voice connection")
        }

        when (VoiceEncryption.getPreferred(voiceReadyPayload!!.modes)) {
            VoiceEncryption.XSALSA20_POLY1305 -> {
                nonceLength = 0
            }
            VoiceEncryption.XSALSA20_POLY1305_LITE -> {
                if (nonce >= maxNonce) {
                    getNextNonce(nonce = 0)
                } else {
                    getNextNonce(nonce++)
                }
            }
            VoiceEncryption.XSALSA20_POLY1305_SUFFIX -> {
                ThreadLocalRandom.current().nextBytes(nonceBuffer)
                nonceLength = TweetNaclFast.SecretBox.nonceLength
            }
        }

        buffer = voicePacket.getEncryptedVoicePacket(box, nonceBuffer, nonceLength, buffer)
        return buffer
    }

    private fun assertEncrypted(audio: ByteBuffer) {
        encryptionBuffer.clear()
        val currentStatus = audio.remaining()
        val needStatus = 12 + audio.remaining()
        if (currentStatus < needStatus) {
            encryptionBuffer = ByteBuffer.allocate(needStatus)
        }
    }

    private fun getNextNonce(nonce: Long) {
        nonceBuffer[0] = (nonce shr 24).toByte()
        nonceBuffer[1] = (nonce shr 16).toByte()
        nonceBuffer[2] = (nonce shr 8).toByte()
        nonceBuffer[3] = nonce.toByte()
    }
}
