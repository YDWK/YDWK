package io.github.ydwk.yde.entities.interaction.actionrow.creator.builder

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.interaction.actionrow.ActionRow
import io.github.ydwk.yde.entities.interaction.actionrow.creator.ActionRowCreator

class ActionRowCreatorBuilder(
    private val yde: YDE,
    private val components: List<Component>
) : ActionRowCreator {
    override fun create(): ActionRow {
        TODO()
    }
}