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

import com.iwebpp.crypto.TweetNaclFast
import java.nio.ByteBuffer
import java.sql.Types.TIMESTAMP
import jdk.jfr.internal.consumer.ChunkHeader.HEADER_SIZE

class VoicePacket {
    private var type: Byte? = null
    private var sequence: Char? = null
    private var ssrc: Int? = null
    private var timestamp: Int? = null
    private var data: ByteArray? = null

    /**
     * Used to decode a voice packet from Discord. (Currently not implemented)
     *
     * @param data The raw data from Discord.
     * @throws IllegalArgumentException If the data is not a valid voice packet.
     */
    constructor(data: ByteArray) {
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

    private fun getVoicePacket(
        audio: ByteBuffer,
        sequence: Char,
        timestamp: Int,
        ssrc: Int,
        buffer: ByteBuffer?
    ): ByteArray {
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

    fun getEncryptedVoicePacket(
        box: TweetNaclFast.SecretBox,
        nonce: ByteArray,
        nonceLength: Int,
        nonceBuffer: ByteBuffer
    ): ByteBuffer {
        // Discord uses RTP header which is a different lengh in comparsion to Xsalsa20 nonce use 24
        // bytes

        var extendedNonce = nonce
        if (nonceLength == 0) {
            extendedNonce = ByteArray(24)
            System.arraycopy(nonce, 0, extendedNonce, 0, nonce.size)
        }

        val encryptedData = box.box(data, extendedNonce)
        return TODO()
    }

    companion object {
        // Discord voice packet structure is as follows:

        // Single byte value of 0x78
        private const val PAYLOAD_TYPE = 1
        private const val SEQUENCE_NUMBER = 2
        private const val TIMESTAMP = 4
        private const val SSRC = 4

        private const val PAYLOAD_TYPE_VALUE = 0x78.toByte()
        private const val VERSION_VALUE = 0x80.toByte()
        private const val HEADER_SIZE = 12
    }
}
