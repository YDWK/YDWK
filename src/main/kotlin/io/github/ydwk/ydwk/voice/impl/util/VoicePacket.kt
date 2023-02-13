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
package io.github.ydwk.ydwk.voice.impl.util

import com.codahale.xsalsa20poly1305.SecretBox
import java.net.DatagramPacket
import java.net.InetSocketAddress
import java.nio.ByteBuffer

/**
 * This has been modified and inspired from
 * [Javacords AudioPacket](https://github.com/Javacord/Javacord/blob/da4d18690b3a22c2b261d629757c40aad6965f7b/javacord-core/src/main/java/org/javacord/core/util/gateway/AudioPacket.java)
 */
class VoicePacket(
    private var audio: ByteArray?,
    private val ssrc: Int,
    private val seq: Char,
    val timestamp: Int
) {
    private var header: ByteArray? = null
    init {
        if (audio != null) {
            audio = byteArrayOf(0xF8.toByte(), 0xFF.toByte(), 0xFE.toByte())
        }

        val buffer: ByteBuffer =
            ByteBuffer.allocate(12)
                .put(0, 0x80.toByte())
                .put(1, 0x78.toByte())
                .putChar(2, seq)
                .putInt(4, timestamp)
                .putInt(8, ssrc)
        header = buffer.array()
    }
    fun encrypt(secretKey: ByteArray?) {
        if (secretKey == null) return
        val nonce = ByteArray(24)
        System.arraycopy(secretKey, 0, nonce, 0, 12)
        audio = SecretBox(secretKey).seal(nonce, audio)
    }

    fun asDatagramPacket(address: InetSocketAddress): DatagramPacket {
        val packet = ByteArray(header!!.size + audio!!.size)
        System.arraycopy(header, 0, packet, 0, header!!.size)
        System.arraycopy(audio, 0, packet, header!!.size, audio!!.size)
        return DatagramPacket(packet, packet.size, address)
    }
}
