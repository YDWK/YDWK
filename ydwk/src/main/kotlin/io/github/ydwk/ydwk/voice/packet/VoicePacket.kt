package io.github.ydwk.ydwk.voice.packet

import org.bouncycastle.asn1.x500.style.RFC4519Style.c
import java.nio.ByteBuffer

class VoicePacket {
    private var type : Byte? = null
    private var sequence : Char? = null
    private var ssrc : Int? = null
    private var timestamp : Int? = null
    private var data : ByteArray? = null


    /**
     * Used to decode a voice packet from Discord. (Currently not implemented)
     *
     * @param data The raw data from Discord.
     * @throws IllegalArgumentException If the data is not a valid voice packet.
     */
    constructor(data: ByteArray){
        this.data = data

        val buffer = ByteBuffer.wrap(data)
        type = buffer.get(PAYLOAD_TYPE)
        sequence = buffer.getChar(SEQUENCE_NUMBER)
        timestamp = buffer.getInt(TIMESTAMP)
        ssrc = buffer.getInt(SSRC)

       throw UnsupportedOperationException("Not implemented")
    }

    /**
     * Used to create a voice packet to send to Discord.
     *
     * @param audio The audio data to send. Has to be in the Opus format.
     * @param sequence The sequence number of the packet.
     * @param timestamp The timestamp of the packet.
     * @param ssrc The SSRC of the packet.
     * @param buffer The buffer to write the packet to.
     */
    constructor(audio: ByteBuffer, sequence: Char, timestamp: Int, ssrc: Int, buffer: ByteBuffer?) {
        this.type = PAYLOAD_TYPE_VALUE
        this.sequence = sequence
        this.timestamp = timestamp
        this.ssrc = ssrc

        this.data = getVoicePacket(audio, sequence, timestamp, ssrc, buffer)
    }

    private fun getVoicePacket(audio: ByteBuffer, sequence: Char, timestamp: Int, ssrc: Int, buffer: ByteBuffer?): ByteArray {
        var buffer = buffer
        if (buffer == null) {
           buffer = ByteBuffer.allocate(HEADER_SIZE + audio.remaining())
        }

        buffer?.put(VERSION_VALUE)
        buffer?.put(PAYLOAD_TYPE_VALUE)
        buffer?.putChar(sequence)
        buffer?.putInt(timestamp)
        buffer?.putInt(ssrc)
        buffer?.put(audio)
        audio.flip()
        return buffer?.array() ?: byteArrayOf()
    }


    companion object {
        // Discord voice packet structure is as follows:

        //Single byte value of 0x78
        private const val PAYLOAD_TYPE = 1
        private const val SEQUENCE_NUMBER = 2
        private const val TIMESTAMP = 4
        private const val SSRC = 4

        private const val PAYLOAD_TYPE_VALUE = 0x78.toByte()
        private const val VERSION_VALUE = 0x80.toByte()
        private const val HEADER_SIZE = 12
    }
}