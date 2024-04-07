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
package io.github.ydwk.ydwk.ws.voice.util

enum class VoiceEncryption {
    XSALSA20_POLY1305_LITE,
    XSALSA20_POLY1305_SUFFIX,
    XSALSA20_POLY1305;

    fun getPreferredMode(): String {
        return when (this) {
            XSALSA20_POLY1305_LITE -> "xsalsa20_poly1305_lite"
            XSALSA20_POLY1305_SUFFIX -> "xsalsa20_poly1305_suffix"
            XSALSA20_POLY1305 -> "xsalsa20_poly1305"
        }
    }

    companion object {
        /**
         * Gets the preferred encryption method for the current platform.
         *
         * @return The preferred encryption method for the current platform.
         */
        fun getPreferred(modes: List<VoiceEncryption>): VoiceEncryption {
            var encryption: VoiceEncryption? = null
            for (a in modes) {
                when (a) {
                    XSALSA20_POLY1305_LITE -> {
                        encryption = XSALSA20_POLY1305_LITE
                        break
                    }
                    XSALSA20_POLY1305_SUFFIX -> {
                        encryption = XSALSA20_POLY1305_SUFFIX
                        break
                    }
                    XSALSA20_POLY1305 -> {
                        encryption = XSALSA20_POLY1305
                        break
                    }
                }
            }
            return encryption!!
        }

        /**
         * Gets the encryption method from the string.
         *
         * @param mode The string to get the encryption method from.
         * @return The encryption method from the string.
         */
        fun getValue(mode: String): VoiceEncryption {
            return when (mode) {
                "xsalsa20_poly1305_lite" -> XSALSA20_POLY1305_LITE
                "xsalsa20_poly1305_suffix" -> XSALSA20_POLY1305_SUFFIX
                "xsalsa20_poly1305" -> XSALSA20_POLY1305
                else -> throw IllegalArgumentException("Unknown voice encryption mode: $mode")
            }
        }
    }
}
