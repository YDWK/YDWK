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

enum class MessageNotificationLevel(private val value: Int) {
    /** Members will receive notifications for all messages by default. */
    ALL_MESSAGES(0),

    /** Members will receive notifications only for messages that @mention them by default. */
    ONLY_MENTIONS(1),

    /** An unknown value. */
    UNKNOWN(-1);

    companion object {
        /**
         * The [MessageNotificationLevel] by its [value].
         *
         * @param value The value to get the [MessageNotificationLevel] by.
         * @return The [MessageNotificationLevel] by the given [value].
         */
        fun fromInt(value: Int): MessageNotificationLevel {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * The value of the [MessageNotificationLevel].
     *
     * @return The value of the [MessageNotificationLevel].
     */
    fun getValue(): Int {
        return value
    }
}
