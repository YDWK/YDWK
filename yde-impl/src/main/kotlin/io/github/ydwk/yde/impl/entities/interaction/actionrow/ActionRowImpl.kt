package io.github.ydwk.yde.impl.entities.interaction.actionrow

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.interaction.actionrow.ActionRow
import io.github.ydwk.yde.impl.entities.interaction.ComponentImpl

class ActionRowImpl(override val yde: YDE, override val json: JsonNode, override val components: List<Component>) : ActionRow, ComponentImpl(yde.entityInstanceBuilder.buildComponent(json))