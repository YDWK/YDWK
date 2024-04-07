package io.github.ydwk.ydwk.voice.packet.opus

import com.sun.jna.ptr.PointerByReference
import tomp2p.opuswrapper.Opus
import java.nio.ByteBuffer
import java.nio.IntBuffer

object OpusHandler {
    private val opusDecoder : PointerByReference =
        Opus.INSTANCE.opus_decoder_create(48000, 2, IntBuffer.allocate(1));

    fun encodeAudio(audio : ByteBuffer, opusEncoder : PointerByReference?) : ByteBuffer {
        if (opusEncoder == null) {
            // TODO : Add to chekc if the opus library is loaded

            var opusEncoder = opusEncoder

            val error = IntBuffer.allocate(1)
            opusEncoder = Opus.INSTANCE.opus_encoder_create(48000, 2, Opus.OPUS_APPLICATION_VOIP, error)
            if (error.get() != Opus.OPUS_OK) {
                throw IllegalStateException("Could not create Opus encoder: ${Opus.INSTANCE.opus_strerror(error.get())}")
            }
        }

        TODO("Finish this function")
    }
}