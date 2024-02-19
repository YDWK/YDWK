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
package io.github.ydwk.yde.impl.interaction.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.message.button.ButtonStyle
import io.github.ydwk.yde.util.Checks
import io.github.ydwk.yde.util.EntityToStringBuilder

open class ComponentImpl(override val yde: YDE, override val json: JsonNode) : Component {

    override val type: ComponentType
        get() = ComponentType.fromInt(json["type"].asInt())

    override val disabled: Boolean
        get() = json["disabled"].asBoolean()

    override val messageCompatible: Boolean
        get() = type.isMessageCompatible()

    override val modalCompatible: Boolean
        get() = type.isModalCompatible()

    override val customId: String?
        get() = if (json.has("custom_id")) json["custom_id"].asText() else null

    override val children: List<Component>
        get() = json["components"].map { ComponentImpl(yde, it) }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }

    data class ActionRowCreator(
        val components: MutableList<ComponentCreator>,
        override val json: ArrayNode = JsonNodeFactory.instance.arrayNode(),
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
        val url: String? = null,
        override val json: ObjectNode = JsonNodeFactory.instance.objectNode(),
    ) : ComponentCreator {

        init {
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
        }
    }

    interface ComponentCreator {
        val json: JsonNode
    }
}
