package io.github.ydwk.yde.interaction.message.button

import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.reply.Repliable

interface ButtonInteraction : ComponentInteraction, Repliable {

    /**
     * The custom id of the button.
     *
     * @return the custom id of the button
     */
    val customId: String

    /**
     * The label of the button.
     *
     * @return the label of the button
     */
    val label: String?
}