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
package io.github.ydwk.yde.interaction.message.selectmenu

import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.reply.Repliable
import java.util.EnumSet

interface SelectMenu : ComponentInteraction, Repliable {
    /**
     * The select menu types.
     *
     * @return the select menu types
     */
    val selectMenuTypes: EnumSet<ComponentType>
        get() =
            EnumSet.of(
                ComponentType.STRING_SELECT_MENU,
                ComponentType.USER_SELECT_MENU,
                ComponentType.ROLE_SELECT_MENU,
                ComponentType.MENTIONABLE_SELECT_MENU,
                ComponentType.CHANNEL_SELECT_MENU)

    /**
     * The custom id of the select menu.
     *
     * @return the custom id of the select menu
     */
    val customId: String

    /**
     * The placeholder text if nothing is selected; max 150 characters.
     *
     * @return the placeholder text if nothing is selected; max 150 characters
     */
    val placeholder: String?

    /**
     * The minimum number of items that must be chosen; default 1, min 0, max 25.
     *
     * @return the minimum number of items that must be chosen; default 1, min 0, max 25
     */
    val minValues: Int

    /**
     * The maximum number of items that can be chosen; default 1, max 25.
     *
     * @return the maximum number of items that can be chosen; default 1, max 25
     */
    val maxValues: Int

    /**
     * The list of values of the select menu.
     *
     * @return the list of values of the select menu
     */
    val values: List<String>

    /**
     * Whether select menu is disabled (defaults to false)
     *
     * @return whether select menu is disabled (defaults to false)
     */
    val isDisabled: Boolean
}
