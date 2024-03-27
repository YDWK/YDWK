package io.github.ydwk.yde.interaction.message.selectmenu.interaction.type

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction

interface UserSelectMenuInteraction : SelectMenuInteraction {

    /**
     * The selected users.
     *
     * @return the selected users
     */
    val selectedUsers: List<User>
}