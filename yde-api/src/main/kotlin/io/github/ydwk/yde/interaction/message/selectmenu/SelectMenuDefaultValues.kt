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
package io.github.ydwk.yde.interaction.message.selectmenu

import io.github.ydwk.yde.entities.util.GenericEntity

interface SelectMenuDefaultValues : GenericEntity {
    /**
     * The type of value that id represents. Either "user", "role", or "channel"
     *
     * @return the type of value that id represents
     */
    val type: Type

    enum class Type(val value: String) {
        USER("user"),
        ROLE("role"),
        CHANNEL("channel");

        companion object {
            /**
             * Get the enum value from the string value.
             *
             * @param value the string value
             * @return the enum value
             */
            fun getValue(value: String): Type {
                return when (value) {
                    "user" -> USER
                    "role" -> ROLE
                    "channel" -> CHANNEL
                    else -> throw IllegalArgumentException("Unknown type: $value")
                }
            }
        }
    }
}
