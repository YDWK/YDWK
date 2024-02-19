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
package io.github.ydwk.yde.interaction.message.selectmenu.types.string

import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.impl.interaction.message.selectmenu.types.StringSelectMenuImpl

interface StringSelectMenuOption : GenericEntity {
    /**
     * The label of the option. Max 100 characters
     *
     * @return the label of the option
     */
    val label: String

    /**
     * The value of the option. Max 100 characters
     *
     * @return the value of the option
     */
    val value: String

    /**
     * The description of the option. Max 100 characters
     *
     * @return the description of the option
     */
    val description: String?

    /**
     * The emoji of the option.
     *
     * @return the emoji of the option
     */
    val emoji: Emoji?

    /**
     * Whether the option is default.
     *
     * @return whether the option is default
     */
    val default: Boolean

    companion object {
        /**
         * Creates a new [StringSelectMenuOption] with the given [label] and [value].
         *
         * @param label the label of the option
         * @param value the value of the option
         * @param description the description of the option
         * @param emoji the emoji of the option
         * @param default whether the option is default
         * @return the created [StringSelectMenuOption]
         */
        fun invoke(
            label: String,
            value: String,
            description: String? = null,
            emoji: Emoji? = null,
            default: Boolean = false
        ): StringSelectMenuImpl.StringSelectMenuOptionCreator {
            return StringSelectMenuImpl.StringSelectMenuOptionCreator(
                label, value, description, emoji, default)
        }
    }
}
