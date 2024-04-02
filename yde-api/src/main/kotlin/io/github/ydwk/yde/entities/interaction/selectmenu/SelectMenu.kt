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
package io.github.ydwk.yde.entities.interaction.selectmenu

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.interaction.selectmenu.creator.builder.types.*
import io.github.ydwk.yde.entities.interaction.selectmenu.creator.types.*
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.util.Checks
import java.util.EnumSet

interface SelectMenu : Component {
    /**
     * The select menu types. [ComponentType.STRING_SELECT_MENU], [ComponentType.USER_SELECT_MENU],
     * [ComponentType.ROLE_SELECT_MENU], [ComponentType.MENTIONABLE_SELECT_MENU],
     * [ComponentType.CHANNEL_SELECT_MENU].
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
    val minValues: Int?

    /**
     * The maximum number of items that can be chosen; default 1, max 25.
     *
     * @return the maximum number of items that can be chosen; default 1, max 25
     */
    val maxValues: Int?

    /**
     * Whether select menu is disabled (defaults to false)
     *
     * @return whether select menu is disabled (defaults to false)
     */
    val isDisabled: Boolean?

    /**
     * The specified choices in a select menu. (only required and available for string selects
     * [ComponentType.STRING_SELECT_MENU])
     *
     * @return the options of the select menu
     */
    val options: List<SelectMenuOption>?

    /**
     * The list of default values for auto-populated select menu components; number of default
     * values must be in the range defined by min_values and max_values.
     *
     * @return the list of default values for auto-populated select menu components; number of
     *   default values must be in the range defined by min_values and max_values
     */
    val defaultValues: List<SelectMenuDefaultValues>?

    /**
     * The list of channel types to include in the channel select component
     * [ComponentType.CHANNEL_SELECT_MENU].
     *
     * @return the list of channel types to include in the channel select component
     *   [ComponentType.CHANNEL_SELECT_MENU]
     */
    val channelTypes: Set<ChannelType>?

    companion object {
        /** The minimum number of options. */
        private const val MIN_OPTIONS = 1

        /** The maximum number of options. */
        private const val MAX_OPTIONS = 25

        /**
         * Create a new channel select menu.
         *
         * @param customId the custom id of the select menu
         * @param channelTypes the type of the channels that can be selected
         * @return [ChannelSelectMenuCreator] to construct the select menu
         */
        fun createChannelSelectMenu(
            yde: YDE,
            customId: String,
            channelTypes: List<ChannelType>
        ): ChannelSelectMenuCreator {
            return ChannelSelectMenuCreatorBuilder(yde, customId, channelTypes)
        }

        /**
         * Create a new member select menu.
         *
         * @param customId the custom id of the select menu
         * @return [MemberSelectMenuCreator] to construct the select menu
         */
        fun createMemberSelectMenu(
            yde: YDE,
            customId: String,
        ): MemberSelectMenuCreator {
            return MemberSelectMenuCreatorBuilder(yde, customId)
        }

        /**
         * Create a new select menu for roles.
         *
         * @param customId the custom id of the select menu
         * @return [RoleSelectMenuCreator] to construct the select menu
         */
        fun createRoleSelectMenu(
            yde: YDE,
            customId: String,
        ): RoleSelectMenuCreator {
            return RoleSelectMenuCreatorBuilder(yde, customId)
        }

        /**
         * Create a new string select menu.
         *
         * @param customId the custom id of the select menu
         * @param options the options of the select menu
         * @return [StringSelectMenuCreator] to construct the select menu
         */
        fun createStringSelectMenu(
            yde: YDE,
            customId: String,
            options: List<SelectMenuOption>
        ): StringSelectMenuCreator {
            Checks.customCheck(
                options.size in MIN_OPTIONS..MAX_OPTIONS,
                "Options must be between $MIN_OPTIONS and $MAX_OPTIONS")

            return StringSelectMenuCreatorBuilder(yde, customId, options)
        }

        /**
         * Create a new user select menu.
         *
         * @param customId the custom id of the select menu
         * @return [UserSelectMenuCreator] to construct the select menu
         */
        fun createUserSelectMenu(
            yde: YDE,
            customId: String,
        ): UserSelectMenuCreator {
            return UserSelectMenuCreatorBuilder(yde, customId)
        }
    }
}
