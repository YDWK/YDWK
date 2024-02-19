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

import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.impl.interaction.message.selectmenu.SelectMenuImpl
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.yde.interaction.message.selectmenu.creator.types.ChannelSelectMenuCreator

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
         * @return [ChannelSelectMenuCreator] to construct the select menu
         */
        operator fun invoke(
            customId: String,
            channelTypes: List<ChannelType>
        ): ChannelSelectMenuCreator {
            return SelectMenuImpl.ChannelSelectMenuCreatorImpl(customId, channelTypes)
        }
    }
}
