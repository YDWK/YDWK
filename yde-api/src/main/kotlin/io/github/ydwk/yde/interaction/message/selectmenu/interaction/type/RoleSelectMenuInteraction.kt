package io.github.ydwk.yde.interaction.message.selectmenu.interaction.type

import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction

interface RoleSelectMenuInteraction : SelectMenuInteraction {
    /**
     * The selected roles.
     *
     * @return the selected roles
     */
    val selectedRoles: List<Role>
}