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

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.message.selectmenu.creator.builder.SelectMenuOptionCreatorBuilder

interface SelectMenuOption : GenericEntity {
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
         * Creates a new select menu option.
         *
         * @param label The label of the option. Max 100 characters
         * @param value The value of the option. Max 100 characters
         * @param description The description of the option. Max 100 characters
         * @param emoji The emoji of the option
         * @param default Whether the option is default
         * @return A new select menu option
         */
        fun create(
            yde: YDE,
            label: String,
            value: String,
            description: String? = null,
            emoji: Emoji? = null,
            default: Boolean = false
        ): SelectMenuOption {
            val builder = SelectMenuOptionCreatorBuilder(yde)

            builder.setLabel(label)
            builder.setValue(value)
            if (description != null) {
                builder.setDescription(description)
            }
            if (emoji != null) {
                builder.setEmoji(emoji)
            }
            builder.setDefault(default)

            return builder.create()
        }
    }
}
