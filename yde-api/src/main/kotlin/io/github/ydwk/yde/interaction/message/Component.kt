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
package io.github.ydwk.yde.interaction.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.message.button.ButtonStyle
import io.github.ydwk.yde.util.Checks

interface Component : GenericEntity {
    /**
     * The type of this component.
     *
     * @return The type of this component.
     */
    val type: ComponentType

    /**
     * Weather this component is disabled.
     *
     * @return Weather this component is disabled.
     */
    val disabled: Boolean

    /**
     * Weather this component is compatible with a message.
     *
     * @return Weather this component is compatible with a message.
     */
    val messageCompatible: Boolean

    /**
     * Weather this component is compatible with a modal.
     *
     * @return Weather this component is compatible with a modal.
     */
    val modalCompatible: Boolean

    /**
     * The custom id of this button if it is not a link button.
     *
     * @return The custom id of this button if it is not a link button.
     */
    val customId: String?

    /**
     * Gets a list of [Component]s that are children of this component.
     *
     * @return A list of [Component]s that are children of this component.
     */
    val children: List<Component>

    interface ComponentCreator {
        val json: JsonNode
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
}
