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
package io.github.ydwk.yde.interaction.application

enum class ApplicationCommandType(private val value: Int) {
    /** A slash command; a text-based command that shows up when a user types */
    CHAT_INPUT(1),

    /** A UI-based command that shows up when you right-click or tap on a user */
    USER(2),

    /** A UI-based command that shows up when you right-click or tap on a message */
    MESSAGE(3),

    /** Unknown command type */
    UNKNOWN(-1);

    companion object {
        /**
         * Get the [ApplicationCommandType] from the value
         *
         * @param value The value to get the [ApplicationCommandType] from.
         * @return The [ApplicationCommandType] from the given [value].
         */
        fun fromInt(value: Int): ApplicationCommandType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /** Get the value of the [ApplicationCommandType] */
    fun toInt(): Int {
        return value
    }
}
