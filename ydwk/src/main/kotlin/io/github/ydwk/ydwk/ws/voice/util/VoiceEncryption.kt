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
package io.github.ydwk.ydwk.ws.voice.util

/**
 * Discord voice encryption modes.
 *
 * The `aead_*` modes are required as of Discord's November 2024 voice encryption update.
 * The legacy `xsalsa20_poly1305*` modes were removed by Discord in November 2024.
 */
enum class VoiceEncryption(val modeName: String) {
    // New modes (Discord Nov 2024+) — preferred first
    AEAD_AES256_GCM_RTPSIZE("aead_aes256_gcm_rtpsize"),
    AEAD_XCHACHA20_POLY1305_RTPSIZE("aead_xchacha20_poly1305_rtpsize"),
    // Legacy modes (removed by Discord Nov 2024 — kept for any remaining compatibility)
    XSALSA20_POLY1305_LITE("xsalsa20_poly1305_lite"),
    XSALSA20_POLY1305_SUFFIX("xsalsa20_poly1305_suffix"),
    XSALSA20_POLY1305("xsalsa20_poly1305");

    fun getPreferredMode(): String = modeName

    companion object {
        private val PREFERENCE_ORDER = listOf(
            "aead_aes256_gcm_rtpsize",
            "aead_xchacha20_poly1305_rtpsize",
            "xsalsa20_poly1305_lite",
            "xsalsa20_poly1305_suffix",
            "xsalsa20_poly1305",
        )

        fun getPreferred(modes: List<String>): VoiceEncryption {
            for (preferred in PREFERENCE_ORDER) {
                if (modes.contains(preferred)) {
                    return values().first { it.modeName == preferred }
                }
            }
            throw IllegalArgumentException("No supported voice encryption mode in: $modes")
        }

        fun fromMode(mode: String): VoiceEncryption =
            values().find { it.modeName == mode }
                ?: throw IllegalArgumentException("Unknown voice encryption mode: $mode")
    }
}
