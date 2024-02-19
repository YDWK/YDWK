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
package io.github.ydwk.yde.interaction.message

enum class ComponentType(
    private val type: Int,
    private val maxPerRow: Int,
    private val messageCompatible: Boolean,
    private val modalCompatible: Boolean
) {
    /** A container for other components. */
    ACTION_ROW(1, 0, true, true),

    /** A button object */
    BUTTON(2, 5, true, false),

    /** A select menu for picking from choices */
    STRING_SELECT_MENU(3, 1, true, false),

    /** A text input object */
    TEXT_INPUT(4, 1, false, true),

    /** Select menu for users. */
    USER_SELECT_MENU(5, 1, true, false),

    /** Select menu for roles. */
    ROLE_SELECT_MENU(6, 1, true, false),

    /** Select menu for mentionables (users and roles) */
    MENTIONABLE_SELECT_MENU(7, 1, true, false),

    /** Select menu for channels. */
    CHANNEL_SELECT_MENU(8, 1, true, false),

    /** Unknown component type */
    UNKNOWN(-1, 0, false, false);

    companion object {
        /**
         * Get the [ComponentType] from the value
         *
         * @param value The value to get the [ComponentType] from.
         * @return The [ComponentType] from the given [value].
         */
        fun fromInt(value: Int): ComponentType {
            return values().firstOrNull { it.type == value } ?: UNKNOWN
        }
    }

    /**
     * Get the type of the component.
     *
     * @return The type of the component.
     */
    fun getType(): Int {
        return type
    }

    /**
     * Get the maximum number of components that can be in a row of this type
     *
     * @return The maximum number of components that can be in a row of this type
     */
    fun getMaxPerRow(): Int {
        return maxPerRow
    }

    /**
     * Whether this component type is compatible with messages
     *
     * @return Whether this component type is compatible with messages
     */
    fun isMessageCompatible(): Boolean {
        return messageCompatible
    }

    /**
     * Whether this component type is compatible with modals
     *
     * @return Whether this component type is compatible with modals
     */
    fun isModalCompatible(): Boolean {
        return modalCompatible
    }
}
