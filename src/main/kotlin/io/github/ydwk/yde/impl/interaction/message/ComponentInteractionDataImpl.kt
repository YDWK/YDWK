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
package io.github.ydwk.yde.impl.interaction.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.interaction.message.ComponentInteractionData
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.util.EntityToStringBuilder

class ComponentInteractionDataImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val customId: String,
    override val componentType: ComponentType,
    override val values: List<ComponentInteractionData.SelectOptionValue>?
) : ComponentInteractionData {
    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }

    class SelectOptionValueImpl(
        override val yde: YDE,
        override val json: JsonNode,
        override val label: String,
        override val value: String,
        override val description: String?,
        override val emoji: Emoji?,
        override val default: Boolean?
    ) : ComponentInteractionData.SelectOptionValue {

        override fun toString(): String {
            return EntityToStringBuilder(yde, this).toString()
        }
    }
}
