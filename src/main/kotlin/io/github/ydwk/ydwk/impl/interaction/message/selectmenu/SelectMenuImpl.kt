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
package io.github.ydwk.ydwk.impl.interaction.message.selectmenu

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.ydwk.impl.interaction.message.ComponentImpl
import io.github.ydwk.ydwk.interaction.message.Component
import io.github.ydwk.ydwk.interaction.message.ComponentType
import io.github.ydwk.ydwk.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.ydwk.interaction.message.selectmenu.types.string.StringSelectMenuOption
import io.github.ydwk.ydwk.util.GetterSnowFlake

open class SelectMenuImpl(
    ydwk: YDWK,
    json: JsonNode,
    interactionId: GetterSnowFlake,
    open val component: Component,
    private val componentJson: JsonNode
) : SelectMenu, ComponentInteractionImpl(ydwk, json, interactionId) {

    constructor(
        componentInteractionImpl: ComponentInteractionImpl,
        component: Component
    ) : this(
        componentInteractionImpl.ydwk,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId,
        component,
        component.json)

    override val customId: String
        get() = componentJson["custom_id"].asText()

    override val placeholder: String?
        get() =
            if (componentJson.has("placeholder")) componentJson["placeholder"].asText() else null

    override val minValues: Int
        get() = componentJson["min_values"].asInt()

    override val maxValues: Int
        get() = componentJson["max_values"].asInt()

    override val isDisabled: Boolean
        get() = componentJson["disabled"].asBoolean()

    data class StringSelectMenuCreator(
        val customId: String,
        val options: List<StringSelectMenuOption>,
        val placeholder: String? = null,
        val minValues: Int? = null,
        val maxValues: Int? = null,
        val disabled: Boolean = false,
        override val json: ObjectNode = JsonNodeFactory.instance.objectNode()
    ) : SelectMenuCreator {

        init {
            json.put("type", ComponentType.STRING_SELECT_MENU.getType())
            json.put("custom_id", customId)
            json.putArray("options").addAll(options.map { it.json })
            if (placeholder != null) json.put("placeholder", placeholder)
            if (minValues != null) json.put("min_values", minValues)
            if (maxValues != null) json.put("max_values", maxValues)
            if (disabled) json.put("disabled", true)
        }
    }

    sealed interface SelectMenuCreator : ComponentImpl.ComponentCreator
}
