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
package io.github.ydwk.ydwk.impl.interaction.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.interaction.message.Component
import io.github.ydwk.ydwk.interaction.message.ComponentType
import io.github.ydwk.ydwk.interaction.message.button.ButtonStyle
import io.github.ydwk.ydwk.util.Checks
import io.github.ydwk.ydwk.util.EntityToStringBuilder

open class ComponentImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long,
) : Component {

    override val type: ComponentType
        get() = ComponentType.fromInt(json.get("type").asInt())

    override val messageCompatible: Boolean
        get() = type.isMessageCompatible()

    override val modalCompatible: Boolean
        get() = type.isModalCompatible()
    override val customId: String?
        get() = TODO("Not yet implemented")
    override val children: List<Component>
        get() = TODO("Not yet implemented")

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this)
            .add("type", type)
            .add("messageCompatible", messageCompatible)
            .add("modalCompatible", modalCompatible)
            .toString()
    }

    data class ActionRowCreator(
        val components: MutableList<ComponentCreator>,
        override val json: ArrayNode = JsonNodeFactory.instance.arrayNode()
    ) : ComponentCreator {
        init {
            Checks.customCheck(
                components.size <= 5, "Action row cannot have more than 5 components")

            json.add(
                JsonNodeFactory.instance
                    .objectNode()
                    .put("type", 1)
                    .set(
                        "components",
                        JsonNodeFactory.instance.arrayNode().apply {
                            components.forEach { add(it.json) }
                        }) as JsonNode)
        }
    }

    data class ButtonCreator(
        val style: ButtonStyle,
        val customId: String? = null,
        val label: String?,
        val link: String? = null,
        override val json: ObjectNode = JsonNodeFactory.instance.objectNode()
    ) : ComponentCreator {

        init {
            if (style == ButtonStyle.LINK && link == null) {
                throw IllegalArgumentException("Link button must have a link")
            } else if (style != ButtonStyle.LINK && link != null) {
                throw IllegalArgumentException("Non-link button must not have a link")
            }

            json.put("type", ComponentType.BUTTON.getType())
            json.put("style", style.getType())
            json.put("label", label)
            json.put("custom_id", customId)
        }
    }

    interface ComponentCreator {
        val json: JsonNode
    }
}
