/*
 * Copyright 2024 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.yde.entities.interaction.button.creator.builder

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.ButtonStyle
import io.github.ydwk.yde.entities.interaction.button.Button
import io.github.ydwk.yde.entities.interaction.button.PartialEmoji
import io.github.ydwk.yde.entities.interaction.button.creator.ButtonCreator
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.util.Checks

class ButtonCreatorBuilder(
    private val style: ButtonStyle,
    val yde: YDE,
    val json: ObjectNode = yde.objectNode
) : ButtonCreator {
    private var customId: String? = null
    private var label: String? = null
    private var emoji: PartialEmoji? = null
    private var url: String? = null

    override fun setCustomId(customId: String): ButtonCreator {
        this.customId = customId
        return this
    }

    override fun setLabel(label: String): ButtonCreator {
        Checks.customCheck(label.length <= 80, "Label must be between 1 and 80 characters long.")

        this.label = label
        return this
    }

    override fun setEmoji(emoji: PartialEmoji): ButtonCreator {
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

        if (emoji != null) {
            json.set<ObjectNode>("emoji", emoji!!.json)
        }

        json.put("custom_id", customId)

        return yde.entityInstanceBuilder.buildButton(json)
    }
}
