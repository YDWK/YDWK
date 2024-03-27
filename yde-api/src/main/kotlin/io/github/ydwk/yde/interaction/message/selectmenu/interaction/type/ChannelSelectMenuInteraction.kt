package io.github.ydwk.yde.interaction.message.selectmenu.interaction.type

import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction

interface ChannelSelectMenuInteraction : SelectMenuInteraction {
    /**
     * The selected channels.
     *
     * @return the selected channels
     */
    val selectedChannels: List<GuildChannel>
}