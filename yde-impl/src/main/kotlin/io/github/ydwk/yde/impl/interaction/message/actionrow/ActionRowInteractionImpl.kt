package io.github.ydwk.yde.impl.interaction.message.actionrow

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.yde.interaction.message.actionrow.ActionRowInteraction
import io.github.ydwk.yde.util.GetterSnowFlake

class ActionRowInteractionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val interactionId: GetterSnowFlake
) : ActionRowInteraction, ComponentInteractionImpl(yde.entityInstanceBuilder.buildComponentInteraction(json, interactionId))