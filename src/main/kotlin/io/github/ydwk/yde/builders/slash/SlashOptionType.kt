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
package io.github.ydwk.yde.builders.slash

enum class SlashOptionType(private val value: Int) {
    SUB_COMMAND(1),
    SUB_COMMAND_GROUP(2),
    STRING(3),
    INTEGER(4),
    BOOLEAN(5),
    USER(6),
    CHANNEL(7),
    ROLE(8),
    MENTIONABLE(9),
    NUMBER(10),
    ATTACHMENT(11),
    UNKNOWN(-1);

    companion object {
        /**
         * Get the [SlashOptionType] from the value
         *
         * @param value The value to get the [SlashOptionType] from.
         * @return The [SlashOptionType] from the given [value].
         */
        fun fromInt(value: Int): SlashOptionType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * Get the value of the [SlashOptionType].
     *
     * @return The value of the [SlashOptionType].
     */
    fun toInt(): Int {
        return value
    }
}
