package io.github.ydwk.yde.entities.interaction.actionrow.creator

import io.github.ydwk.yde.entities.interaction.actionrow.ActionRow

interface ActionRowCreator {

    /**
     * Creates an action row.
     *
     * @return The created action row.
     */
    fun create(): ActionRow
}