package io.github.ydwk.yde.interaction.message.selectmenu

import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.reply.Repliable

interface SelectMenuInteraction : ComponentInteraction, Repliable {

    /**
     * The custom id of the select menu.
     *
     * @return the custom id of the select menu
     */
    val customId: String

    /**
     * The placeholder of the select menu.
     *
     * @return the placeholder of the select menu
     */
    val placeholder: String

    /**
     * The minimum number of options that must be selected.
     *
     * @return the minimum number of options that must be selected
     */
    val minValues: Int

    /**
     * The maximum number of options that can be selected.
     *
     * @return the maximum number of options that can be selected
     */
    val maxValues: Int
}