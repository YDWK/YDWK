package io.github.ydwk.yde.interaction.message.selectmenu.interaction.type

import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction

interface MemberSelectMenuInteraction : SelectMenuInteraction {
    /**
     * The selected members.
     *
     * @return the selected members
     */
    val selectedMembers: List<Member>
}