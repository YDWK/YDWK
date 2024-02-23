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
package io.github.ydwk.yde.impl.interaction.message.button

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.message.Embed
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
    override val style: ButtonStyle,
    override val label: String?,
    override val customId: String?,
    override val emoji: Emoji?,
    override val url: URL?,
    override val disabled: Boolean,
) : Button, ComponentInteractionImpl(yde, json, interactionId) {

    constructor(
        componentInteractionImpl: ComponentInteractionImpl,
        style: ButtonStyle,
        label: String?,
        customId: String?,
        emoji: Emoji?,
        url: URL?,
        disabled: Boolean,
    ) : this(
        componentInteractionImpl.yde,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId,
        style,
        label,
        customId,
        emoji,
        url,
        disabled)

    private val componentJson: JsonNode = run {
        val message = yde.entityInstanceBuilder.buildMessage(json["message"]).json
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

    override fun reply(content: String): Reply {
        return ReplyImpl(yde, content, null, interactionId.asString, interactionToken)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(yde, null, embed, interactionId.asString, interactionToken)
    }

    override val type: ComponentType
        get() = ComponentType.BUTTON
}
