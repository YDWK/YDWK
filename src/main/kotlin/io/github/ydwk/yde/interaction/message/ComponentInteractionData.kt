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

import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.interaction.sub.GenericCommandData

interface ComponentInteractionData : GenericCommandData {
    /**
     * The custom id of this component.
     *
     * @return The custom id of this component.
     */
    val customId: String

    /**
     * The component type of this component.
     *
     * @return The component type of this component.
     */
    val componentType: ComponentType

    /**
     * Gets an array of select option values, if the component is a select menu of type
     * [ComponentType.SELECT_MENU].
     *
     * @return An array of select option values, if the component is a select menu of type
     *   [ComponentType.SELECT_MENU].
     */
    val values: List<SelectOptionValue>?

    interface SelectOptionValue : GenericCommandData {
        /**
         * The label of this select option value.
         *
         * @return The label of this select option value.
         */
        val label: String

        /**
         * The value of this select option value.
         *
         * @return The value of this select option value.
         */
        val value: String

        /**
         * The description of this select option value.
         *
         * @return The description of this select option value.
         */
        val description: String?

        /**
         * The emoji of this select option value.
         *
         * @return The emoji of this select option value.
         */
        val emoji: Emoji?

        /**
         * The default value of this select option value.
         *
         * @return The default value of this select option value.
         */
        val default: Boolean?
    }
}
