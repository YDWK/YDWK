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
package io.github.ydwk.yde.interaction.message.selectmenu.types

import io.github.ydwk.yde.impl.interaction.message.selectmenu.SelectMenuImpl
import io.github.ydwk.yde.impl.interaction.message.selectmenu.types.StringSelectMenuImpl
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.yde.interaction.message.selectmenu.creator.types.StringSelectMenuCreator
import io.github.ydwk.yde.interaction.message.selectmenu.types.string.StringSelectMenuOption
import io.github.ydwk.yde.util.Checks

interface StringSelectMenu : SelectMenu {
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
         * @return [StringSelectMenuCreator] to construct the select menu
         */
        operator fun invoke(
            customId: String,
            options: List<StringSelectMenuImpl.StringSelectMenuOptionCreator>,
        ): StringSelectMenuCreator {
            Checks.customCheck(
                options.size in MIN_OPTIONS..MAX_OPTIONS,
                "The number of options must be between $MIN_OPTIONS and $MAX_OPTIONS")
            return SelectMenuImpl.StringSelectMenuCreatorImpl(customId, options)
        }
    }
}
