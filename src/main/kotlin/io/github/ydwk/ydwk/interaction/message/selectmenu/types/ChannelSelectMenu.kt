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

import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.impl.interaction.message.selectmenu.SelectMenuImpl
import io.github.ydwk.ydwk.interaction.message.selectmenu.SelectMenu

interface ChannelSelectMenu : SelectMenu {

    /**
     * The channels that are selected.
     *
     * @return the channels that are selected.
     */
    val selectedChannels: List<GuildChannel>

    companion object {
        /**
         * Create a new [ChannelSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param channelTypes the type of the channels that can be selected
         * @param placeholder the placeholder of the select menu
         * @param minValues the minimum number of values
         * @param maxValues the maximum number of values
         * @param disabled whether the select menu is disabled
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   Channel select menu
         */
        operator fun invoke(
            customId: String,
            channelTypes: List<ChannelType>,
            placeholder: String? = null,
            minValues: Int? = null,
            maxValues: Int? = null,
            disabled: Boolean = false
        ): SelectMenuImpl.SelectMenuCreator {
            return SelectMenuImpl.ChannelSelectMenuCreator(
                customId, channelTypes, placeholder, minValues, maxValues, disabled)
        }

        /**
         * Create a new [ChannelSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param channelTypes the type of the channels that can be selected
         * @param placeholder the placeholder of the select menu
         * @param minValues the minimum number of values
         * @param maxValues the maximum number of values
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   Channel select menu
         */
        operator fun invoke(
            customId: String,
            channelTypes: List<ChannelType>,
            placeholder: String? = null,
            minValues: Int? = null,
            maxValues: Int? = null,
        ): SelectMenuImpl.SelectMenuCreator {
            return SelectMenuImpl.ChannelSelectMenuCreator(
                customId, channelTypes, placeholder, minValues, maxValues, false)
        }

        /**
         * Create a new [ChannelSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param channelTypes the type of the channels that can be selected
         * @param placeholder the placeholder of the select menu
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   Channel select menu
         */
        operator fun invoke(
            customId: String,
            channelTypes: List<ChannelType>,
            placeholder: String? = null,
        ): SelectMenuImpl.SelectMenuCreator {
            return SelectMenuImpl.ChannelSelectMenuCreator(
                customId, channelTypes, placeholder, null, null, false)
        }

        /**
         * Create a new [ChannelSelectMenu].
         *
         * @param customId the custom id of the select menu
         * @param channelTypes the type of the channels that can be selected
         * @return [SelectMenuImpl.SelectMenuCreator] which contains the json representation of the
         *   Channel select menu
         */
        operator fun invoke(
            customId: String,
            channelTypes: List<ChannelType>,
        ): SelectMenuImpl.SelectMenuCreator {
            return SelectMenuImpl.ChannelSelectMenuCreator(
                customId, channelTypes, null, null, null, false)
        }
    }
}
