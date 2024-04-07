package io.github.ydwk.ydwk.voice.packet.opus

import com.sun.jna.ptr.PointerByReference
import tomp2p.opuswrapper.Opus
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

class OpusEncoder {
    private var opusEncoder: PointerByReference? = null

    init {
        // Initialize Opus encoder with desired parameters
        val error = IntBuffer.allocate(1)
        opusEncoder = Opus.INSTANCE.opus_encoder_create(48000, 2, Opus.OPUS_APPLICATION_VOIP, error)
        if (error.get() != Opus.OPUS_OK) {
            throw IllegalStateException("Could not create Opus encoder: ${Opus.INSTANCE.opus_strerror(error.get())}")
        }
    }

    fun encode(audioData: ByteBuffer): ByteBuffer? {
        // Convert byte buffer to short buffer
        val shortBuffer = audioData.asShortBuffer()

        // Allocate buffer for encoded data
        val encodedSize = audioData.remaining() * 2 // Each short is 2 bytes
        val encodedData = ByteBuffer.allocate(encodedSize)

        // Encode audio using Opus
        val result = Opus.INSTANCE.opus_encode(opusEncoder, shortBuffer, audioData.remaining() / 2, encodedData, encodedSize)
        if (result < 0) {
            // Error handling
            println("Error encoding audio: ${Opus.INSTANCE.opus_strerror(result)}")
            return null
        }

        // Set buffer position and limit
        (encodedData as Buffer).position(0).limit(result)

        return encodedData
    }

    fun close() {
        // Clean up resources
        if (opusEncoder != null) {
            Opus.INSTANCE.opus_encoder_destroy(opusEncoder)
            opusEncoder = null
        }
    }
}
