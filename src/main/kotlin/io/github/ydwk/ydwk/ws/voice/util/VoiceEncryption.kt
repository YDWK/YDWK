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
package io.github.ydwk.ydwk.ws.voice.util

import java.util.*

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
        fun getPreferred(modes: List<String>): VoiceEncryption {
            var encryption: VoiceEncryption? = null
            for (a in modes) {
                when (a) {
                    "xsalsa20_poly1305_lite" -> {
                        encryption = XSALSA20_POLY1305_LITE
                        break
                    }
                    "xsalsa20_poly1305_suffix" -> {
                        encryption = XSALSA20_POLY1305_SUFFIX
                        break
                    }
                    "xsalsa20_poly1305" -> {
                        encryption = XSALSA20_POLY1305
                        break
                    }
                }
            }
            return encryption!!
        }
    }
}
