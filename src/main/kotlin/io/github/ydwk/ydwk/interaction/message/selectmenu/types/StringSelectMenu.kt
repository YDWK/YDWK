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
package io.github.ydwk.ydwk.interaction.message.selectmenu.types

import io.github.ydwk.ydwk.impl.interaction.message.selectmenu.SelectMenuImpl
import io.github.ydwk.ydwk.impl.interaction.message.selectmenu.types.StringSelectMenuImpl
import io.github.ydwk.ydwk.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.ydwk.interaction.message.selectmenu.types.string.StringSelectMenuOption
import io.github.ydwk.ydwk.interaction.reply.Repliable
import io.github.ydwk.ydwk.util.Checks

interface StringSelectMenu : SelectMenu, Repliable {
    /**
     * The options of the select menu.
     *
     * @return the options of the select menu
     */
    val options: List<StringSelectMenuOption>

    companion object {
        /** The minimum number of options. */
        private const val MIN_OPTIONS = 1

        /** The maximum number of options. */
        private const val MAX_OPTIONS = 25

        /**
         * Create a new [StringSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param options the options of the select menu
         * @param placeholder the placeholder of the select menu
         * @param minValues the minimum number of values
         * @param maxValues the maximum number of values
         * @param disabled whether the select menu is disabled
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   String select menu
         */
        operator fun invoke(
            customId: String,
            options: List<StringSelectMenuImpl.StringSelectMenuOptionCreator>,
            placeholder: String? = null,
            minValues: Int? = null,
            maxValues: Int? = null,
            disabled: Boolean = false
        ): SelectMenuImpl.SelectMenuCreator {
            Checks.customCheck(
                options.size in MIN_OPTIONS..MAX_OPTIONS,
                "The number of options must be between $MIN_OPTIONS and $MAX_OPTIONS")
            return SelectMenuImpl.StringSelectMenuCreator(
                customId, options, placeholder, minValues, maxValues, disabled)
        }

        /**
         * Create a new [StringSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param options the options of the select menu
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   String select menu
         */
        operator fun invoke(
            customId: String,
            options: List<StringSelectMenuImpl.StringSelectMenuOptionCreator>
        ): SelectMenuImpl.SelectMenuCreator =
            SelectMenuImpl.StringSelectMenuCreator(customId, options)

        /**
         * Creates a new [StringSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param options the options of the select menu
         * @param placeholder the placeholder of the select menu
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   String select menu
         */
        operator fun invoke(
            customId: String,
            options: List<StringSelectMenuImpl.StringSelectMenuOptionCreator>,
            placeholder: String
        ): SelectMenuImpl.SelectMenuCreator =
            SelectMenuImpl.StringSelectMenuCreator(customId, options, placeholder)

        /**
         * Creates a new [StringSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param options the options of the select menu
         * @param placeholder the placeholder of the select menu
         * @param minValues the minimum number of values
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   String select menu
         */
        operator fun invoke(
            customId: String,
            options: List<StringSelectMenuImpl.StringSelectMenuOptionCreator>,
            placeholder: String,
            minValues: Int
        ): SelectMenuImpl.SelectMenuCreator =
            SelectMenuImpl.StringSelectMenuCreator(customId, options, placeholder, minValues)

        /**
         * Creates a new [StringSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param options the options of the select menu
         * @param placeholder the placeholder of the select menu
         * @param minValues the minimum number of values
         * @param maxValues the maximum number of values
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   String select menu
         */
        operator fun invoke(
            customId: String,
            options: List<StringSelectMenuImpl.StringSelectMenuOptionCreator>,
            placeholder: String,
            minValues: Int,
            maxValues: Int
        ): SelectMenuImpl.SelectMenuCreator =
            SelectMenuImpl.StringSelectMenuCreator(
                customId, options, placeholder, minValues, maxValues)
    }
}
