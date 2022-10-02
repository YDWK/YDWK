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
package io.github.ydwk.ydwk.interaction.sub

enum class InteractionType(private val value: Int) {
    /** Ping interaction */
    PING(1),

    /** Application command interaction */
    APPLICATION_COMMAND(2),

    /** Message component interaction */
    MESSAGE_COMPONENT(3),

    /** An Autocomplete interaction */
    APPLICATION_COMMAND_AUTOCOMPLETE(4),

    /** A submission of a model */
    MODAL_SUBMIT(5),

    /** Unknown interaction type */
    UNKNOWN(-1);

    companion object {
        /** Get the [InteractionType] from the value */
        fun fromInt(value: Int): InteractionType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /** Get the value of the [InteractionType] */
    fun toInt(): Int {
        return value
    }
}
