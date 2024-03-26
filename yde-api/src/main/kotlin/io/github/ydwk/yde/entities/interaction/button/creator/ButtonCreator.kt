package io.github.ydwk.yde.entities.interaction.button.creator

import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.interaction.button.Button

interface ButtonCreator {
    /**
     * Sets the custom id of the button.
     *
     * @param customId The custom id of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setCustomId(customId: String): ButtonCreator

    /**
     * Sets the label of the button.
     *
     * @param label The label of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setLabel(label: String): ButtonCreator

    /**
     * Sets the emoji of the button.
     *
     * @param emoji The emoji of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setEmoji(emoji: Emoji): ButtonCreator

    /**
     * Sets the url of the button.
     *
     * @param url The url of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setUrl(url: String): ButtonCreator

    /**
     * Creates a Button.
     *
     * @return The created Button.
     */
    fun create(): Button
}
