/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.impl.interaction.message.button

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.message.Embed
import io.github.ydwk.yde.impl.entities.EmojiImpl
import io.github.ydwk.yde.impl.entities.MessageImpl
import io.github.ydwk.yde.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.yde.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.yde.interaction.application.sub.Reply
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.message.button.Button
import io.github.ydwk.yde.interaction.message.button.ButtonStyle
import io.github.ydwk.yde.util.GetterSnowFlake
import java.net.URL

open class ButtonImpl(
    yde: YDE,
    json: JsonNode,
    interactionId: GetterSnowFlake,
) : Button, ComponentInteractionImpl(yde, json, interactionId) {

    constructor(
        componentInteractionImpl: ComponentInteractionImpl,
    ) : this(
        componentInteractionImpl.yde,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId)

    override val customId: String?
        get() = json["data"]["custom_id"]?.asText()

    private val componentJson: JsonNode = run {
        val message = MessageImpl(yde, json["message"], json["message"]["id"].asLong()).json
        val mainComponents = message["components"]
        for (mainComponent in mainComponents) {
            val components = mainComponent["components"]
            val component = components.find { it["custom_id"].asText() == customId }
            if (component != null) {
                return@run component
            }
        }
        throw IllegalStateException("Component not found")
    }

    override val url: URL?
        get() = if (componentJson.has("url")) URL(componentJson["url"].asText()) else null

    override val disabled: Boolean
        get() = componentJson["disabled"].asBoolean()

    override fun reply(content: String): Reply {
        return ReplyImpl(yde, content, null, interactionId.asString, interactionToken)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(yde, null, embed, interactionId.asString, interactionToken)
    }

    override val label: String?
        get() = if (componentJson.has("label")) componentJson["label"].asText() else null

    override val emoji: Emoji?
        get() = if (componentJson.has("emoji")) EmojiImpl(yde, componentJson["emoji"]) else null

    override val style: ButtonStyle
        get() = ButtonStyle.fromInt(componentJson["style"].asInt())

    override val type: ComponentType
        get() = ComponentType.BUTTON
}
