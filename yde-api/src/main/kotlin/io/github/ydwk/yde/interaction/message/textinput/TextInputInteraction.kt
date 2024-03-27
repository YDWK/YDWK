package io.github.ydwk.yde.interaction.message.textinput

import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.reply.Repliable

interface TextInputInteraction : ComponentInteraction, Repliable {

    /**
     * The custom id of the text input.
     *
     * @return the custom id of the text input
     */
    val customId: String

    /**
     * The values of each text input.
     *
     * @return the values of each text input
     */
    val values: List<String>

    /**
     * The value of the text input by its custom id.
     *
     * @param customId The custom id of the text input.
     * @return the value of the text input by its custom id
     */
    fun getValue(customId: String): String?
}