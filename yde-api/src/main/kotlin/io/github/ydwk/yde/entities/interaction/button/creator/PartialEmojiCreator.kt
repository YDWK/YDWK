package io.github.ydwk.yde.entities.interaction.button.creator

import io.github.ydwk.yde.entities.interaction.button.PartialEmoji

interface PartialEmojiCreator {
    /**
     * Sets whether the partial emoji is animated.
     *
     * @param animated Whether the partial emoji is animated.
     * @return This [PartialEmojiCreator] for chaining.
     */
    fun setAnimated(animated: Boolean): PartialEmojiCreator

    /**
     * Creates a PartialEmoji.
     *
     * @return The created PartialEmoji.
     */
    fun create(): PartialEmoji
}