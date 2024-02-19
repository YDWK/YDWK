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
package io.github.ydwk.yde.interaction.sub

enum class InteractionCallbackType(private val type: Int) {
    /** ACK a `Ping` */
    PONG(1),
    /** Respond to an interaction with a message */
    CHANNEL_MESSAGE_WITH_SOURCE(4),
    /** ACK an interaction and edit a response later, the user sees a loading state */
    DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE(5),
    /**
     * For components, ACK an interaction and edit the original message later; the user does not see
     * a loading state
     */
    DEFERRED_UPDATE_MESSAGE(6),
    /** For components, edit the message the component was attached to */
    UPDATE_MESSAGE(7),
    /** Respond to an autocomplete interaction with suggested choices */
    APPLICATION_COMMAND_AUTOCOMPLETE_RESULT(8),
    /** Respond to an interaction with a popup modal */
    MODAL(9),
    /** An unknown type */
    UNKNOWN(-1);

    companion object {
        /**
         * Get the [InteractionCallbackType] from the [type] integer
         *
         * @param type The type integer
         * @return The [InteractionCallbackType] from the given [type]
         */
        fun fromInt(type: Int): InteractionCallbackType {
            return values().firstOrNull { it.type == type } ?: UNKNOWN
        }
    }

    /**
     * Get the integer type of the [InteractionCallbackType]
     *
     * @return The integer type of the [InteractionCallbackType]
     */
    fun getType(): Int {
        return type
    }
}
