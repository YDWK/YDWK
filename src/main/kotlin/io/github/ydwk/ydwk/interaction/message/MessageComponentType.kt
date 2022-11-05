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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.interaction.message

enum class MessageComponentType(private val type: Int) {
    /** A container for other components. */
    ACTION_ROW(1),

    /** A button object */
    BUTTON(2),

    /** A select menu for picking from choices */
    SELECT_MENU(3),

    /** A text input object */
    TEXT_INPUT(4),

    /** Unknown component type */
    UNKNOWN(-1);

    companion object {
        /** Get the [MessageComponentType] from the value */
        fun fromInt(value: Int): MessageComponentType {
            return values().firstOrNull { it.type == value } ?: UNKNOWN
        }
    }

    /** Get the value of the [MessageComponentType] */
    fun toInt(): Int {
        return type
    }
}
