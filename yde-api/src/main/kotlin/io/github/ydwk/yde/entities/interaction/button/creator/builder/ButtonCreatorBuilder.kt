package io.github.ydwk.yde.entities.interaction.button.creator.builder

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.interaction.ButtonStyle
import io.github.ydwk.yde.entities.interaction.button.Button
import io.github.ydwk.yde.entities.interaction.button.creator.ButtonCreator
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.util.Checks

class ButtonCreatorBuilder(
    val style: ButtonStyle,
    val yde: YDE,
    val json: ObjectNode = yde.objectNode
) : ButtonCreator {
    private var customId: String? = null
    private var label: String? = null
    private var emoji: Emoji? = null
    private var url: String? = null

    override fun setCustomId(customId: String): ButtonCreator {
        this.customId = customId
        return this
    }

    override fun setLabel(label: String): ButtonCreator {
        Checks.customCheck(
            label.length <= 80,
            "Label must be between 1 and 80 characters long.")

        this.label = label
        return this
    }

    override fun setEmoji(emoji: Emoji): ButtonCreator {
        this.emoji = emoji
        return this
    }

    override fun setUrl(url: String): ButtonCreator {
        require(url.startsWith("https://")) { "URL must start with 'https://'." }

        this.url = url
        return this
    }

    override fun create(): Button {
        if (style == ButtonStyle.LINK && url == null) {
            throw IllegalArgumentException("Url button must have a url")
        } else if (style != ButtonStyle.LINK && url != null) {
            throw IllegalArgumentException("Non-url button must not have a url")
        }

        json.put("type", ComponentType.BUTTON.getType())
        json.put("style", style.getType())
        json.put("label", label)
        if (url != null) {
            json.put("url", url)
        }
        json.put("custom_id", customId)

        return yde.entityInstanceBuilder.buildButton(json)
    }
}