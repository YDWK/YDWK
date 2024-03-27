package io.github.ydwk.yde.entities.interaction.button.creator.builder

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.button.PartialEmoji
import io.github.ydwk.yde.entities.interaction.button.creator.PartialEmojiCreator

class PartialEmojiCreatorBuilder(
    val yde: YDE,
    val name: String,
    val id: String,
    val json: ObjectNode = yde.objectNode
) : PartialEmojiCreator {
    private var animated: Boolean = false

    override fun setAnimated(animated: Boolean): PartialEmojiCreator {
        this.animated = animated
        return this
    }

    override fun create(): PartialEmoji {
        json.put("name", name)
        json.put("animated", animated)
        json.put("id", id)

        return yde.objectMapper.convertValue(json, PartialEmoji::class.java)
    }
}