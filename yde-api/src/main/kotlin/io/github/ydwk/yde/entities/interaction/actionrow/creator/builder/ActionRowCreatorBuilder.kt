package io.github.ydwk.yde.entities.interaction.actionrow.creator.builder

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.interaction.actionrow.ActionRow
import io.github.ydwk.yde.entities.interaction.actionrow.creator.ActionRowCreator

class ActionRowCreatorBuilder(
    private val yde: YDE,
    private val components: List<Component>

) : ActionRowCreator {
    override fun create(): ActionRow {

        val json = yde.objectNode
        val arrayNode = yde.objectMapper.createArrayNode()

        arrayNode.addAll(components.map { it.json })

        json.put("type", 1)
        json.set<ArrayNode>("components", arrayNode)

        return yde.entityInstanceBuilder.buildActionRow(json)
    }
}