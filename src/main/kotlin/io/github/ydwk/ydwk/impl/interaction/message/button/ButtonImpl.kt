/*
 * Copyright 2022 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.interaction.message.button

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Emoji
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.impl.entities.EmojiImpl
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.ydwk.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.ydwk.interaction.application.sub.Reply
import io.github.ydwk.ydwk.interaction.message.Component
import io.github.ydwk.ydwk.interaction.message.ComponentType
import io.github.ydwk.ydwk.interaction.message.button.Button
import io.github.ydwk.ydwk.interaction.message.button.ButtonStyle
import io.github.ydwk.ydwk.util.GetterSnowFlake
import java.net.URL

open class ButtonImpl(
    ydwk: YDWK,
    json: JsonNode,
    interactionId: GetterSnowFlake,
) : Button, ComponentInteractionImpl(ydwk, json, interactionId) {
    private val componentJson: JsonNode =
        MessageImpl(ydwk, json["message"], json["message"]["id"].asLong()).json

    constructor(
        componentInteractionImpl: ComponentInteractionImpl,
        component: Component
    ) : this(
        componentInteractionImpl.ydwk,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId)

    override val url: URL?
        get() = if (componentJson.has("url")) URL(componentJson["url"].asText()) else null

    override val disabled: Boolean
        get() = componentJson["disabled"].asBoolean()

    override fun reply(content: String): Reply {
        return ReplyImpl(ydwk, content, null, interactionId.asString, interactionToken)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(ydwk, null, embed, interactionId.asString, interactionToken)
    }

    override val label: String?
        get() = if (componentJson.has("label")) componentJson["label"].asText() else null

    override val customId: String?
        get() = if (componentJson.has("custom_id")) componentJson["custom_id"].asText() else null

    override val emoji: Emoji?
        get() = if (componentJson.has("emoji")) EmojiImpl(ydwk, componentJson["emoji"]) else null

    override val style: ButtonStyle
        get() = ButtonStyle.fromInt(componentJson["style"].asInt())

    override val type: ComponentType
        get() = ComponentType.BUTTON
}
