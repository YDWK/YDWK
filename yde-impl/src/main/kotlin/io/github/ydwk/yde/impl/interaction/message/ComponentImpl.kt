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
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.interaction.message.ComponentType
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
}
