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

enum class SpeakingFlag(private val value: Long) {
    /** Normal transmission of voice audio. */
    MICROPHONE(1 shl 0),

    /** Transmission of context audio for video, no speaking indicator. */
    SOUND_SHARE(1 shl 1),

    /** Priority speaker, lowering audio of other speakers. */
    PRIORITY(1 shl 2);

    fun getValue(): Long {
        return value
    }

    companion object {
        /**
         * The speaking flag from the given value.
         *
         * @param value The value to get the speaking flag from.
         * @return The speaking flag.
         */
        fun fromValue(value: Long): SpeakingFlag {
            return entries.first { it.value == value }
        }
    }
}
