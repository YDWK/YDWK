package io.github.ydwk.yde.interaction.message.selectmenu.interaction.type

import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuOption
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction

interface StringSelectMenuInteraction : SelectMenuInteraction {
    /**
     * The selected options that the user selected.
     *
     * @return the selected options that the user selected
     */
    val selectedOptions: List<SelectMenuOption>

    /**
     * The possible options that the user can select.
     *
     * @return the possible options that the user can select
     */
    val options: List<SelectMenuOption>
}