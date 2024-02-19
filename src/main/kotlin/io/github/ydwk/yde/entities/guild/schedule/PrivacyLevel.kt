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
package io.github.ydwk.yde.entities.guild.schedule

enum class PrivacyLevel(private val value: Int) {
    /** The scheduled event is private and not available in discovery. */
    GUILD_ONLY(2);

    companion object {
        /**
         * The privacy level from the value.
         *
         * @param value the value.
         * @return the privacy level.
         */
        fun getValue(value: Int): PrivacyLevel {
            return values().first { it.value == value }
        }
    }

    /**
     * The value of the privacy level.
     *
     * @return the value of the privacy level.
     */
    fun getValue(): Int {
        return value
    }
}
