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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.voice.sub

import com.codahale.xsalsa20poly1305.SecretBox
import java.nio.ByteBuffer

class VoicePacket(
    var data: ByteArray?,
    private val ssrc: Int,
    private val sequence: Char,
    private val timestamp: Int,
) {
    private var header: ByteArray? = null
    private var encrypted: Boolean = false

    init {
        if (data != null) {
            val buffer: ByteBuffer =
                ByteBuffer.allocate(12)
                    .put(0, 0x80.toByte())
                    .put(1, 0x78.toByte())
                    .putChar(2, sequence)
                    .putInt(4, timestamp)
                    .putInt(8, ssrc)
            header = buffer.array()
        } else {
            header = null
        }
    }
    fun encrypt() {
        val nonce = ByteArray(24)
        header?.let { System.arraycopy(it, 0, nonce, 0, 12) }
        data = SecretBox(nonce).seal(nonce, data)
        encrypted = true
    }
}
