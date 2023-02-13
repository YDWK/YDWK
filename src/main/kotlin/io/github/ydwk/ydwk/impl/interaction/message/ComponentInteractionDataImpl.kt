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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.interaction.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Emoji
import io.github.ydwk.ydwk.impl.entities.EmojiImpl
import io.github.ydwk.ydwk.interaction.message.ComponentInteractionData
import io.github.ydwk.ydwk.interaction.message.ComponentType
import io.github.ydwk.ydwk.util.EntityToStringBuilder

class ComponentInteractionDataImpl(override val ydwk: YDWK, override val json: JsonNode) :
    ComponentInteractionData {
    override val customId: String
        get() = json["custom_id"].asText()

    override val componentType: ComponentType
        get() = ComponentType.fromInt(json["component_type"].asInt())
    override val values: List<ComponentInteractionData.SelectOptionValue>?
        get() = json["values"]?.map { ComponentInteractionDataImpl.SelectOptionValueImpl(ydwk, it) }

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).toString()
    }

    class SelectOptionValueImpl(override val ydwk: YDWK, override val json: JsonNode) :
        ComponentInteractionData.SelectOptionValue {
        override val label: String
            get() = json["label"].asText()

        override val value: String
            get() = json["value"].asText()

        override val description: String?
            get() = if (json.has("description")) json["description"].asText() else null

        override val emoji: Emoji?
            get() = if (json.has("emoji")) EmojiImpl(ydwk, json["emoji"]) else null

        override val default: Boolean?
            get() = if (json.has("default")) json["default"].asBoolean() else null

        override fun toString(): String {
            return EntityToStringBuilder(ydwk, this).toString()
        }
    }
}
