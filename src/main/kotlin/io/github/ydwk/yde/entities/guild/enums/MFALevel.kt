/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.entities.guild.enums

enum class MFALevel(private val value: Int) {
    /** Guild has no MFA/2FA requirement for moderation actions. */
    NONE(0),

    /** Guild has a 2FA requirement for moderation actions. */
    ELEVATED(1),

    /** An unknown MFA level was returned. */
    UNKNOWN(-1);

    companion object {
        /**
         * The [MFALevel] for the given [value].
         *
         * @param value The value to get the [MFALevel] for.
         * @return The [MFALevel] for the given [value].
         */
        fun fromInt(value: Int): MFALevel {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * The value of the [MFALevel].
     *
     * @return The value of the [MFALevel].
     */
    fun getValue(): Int {
        return value
    }
}
