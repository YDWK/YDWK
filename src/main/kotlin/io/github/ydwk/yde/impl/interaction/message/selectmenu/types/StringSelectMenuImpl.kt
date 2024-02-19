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
package io.github.ydwk.yde.impl.interaction.message.selectmenu.types

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.yde.impl.interaction.message.ComponentImpl
import io.github.ydwk.yde.impl.interaction.message.selectmenu.SelectMenuImpl
import io.github.ydwk.yde.impl.interaction.message.selectmenu.types.string.StringSelectMenuOptionImpl
import io.github.ydwk.yde.interaction.message.selectmenu.types.StringSelectMenu
import io.github.ydwk.yde.interaction.message.selectmenu.types.string.StringSelectMenuOption
import io.github.ydwk.yde.util.GetterSnowFlake

class StringSelectMenuImpl(
    yde: YDE,
    json: JsonNode,
    interactionId: GetterSnowFlake,
) : StringSelectMenu, SelectMenuImpl(yde, json, interactionId) {
    constructor(
        componentInteractionImpl: ComponentInteractionImpl,
    ) : this(
        componentInteractionImpl.yde,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId)

    override val options: List<StringSelectMenuOption>
        get() = componentJson["options"].map { StringSelectMenuOptionImpl(yde, it) }

    data class StringSelectMenuOptionCreator(
        val label: String,
        val value: String,
        val description: String? = null,
        val emoji: Emoji? = null,
        val default: Boolean = false,
        override val json: ObjectNode = JsonNodeFactory.instance.objectNode(),
    ) : StringSelectMenuCreator {
        init {
            json.put("label", label)
            json.put("value", value)
            if (description != null) json.put("description", description)
            if (emoji != null) json.set<JsonNode>("emoji", emoji.json)
            json.put("default", default)
        }
    }

    sealed interface StringSelectMenuCreator : ComponentImpl.ComponentCreator
}
