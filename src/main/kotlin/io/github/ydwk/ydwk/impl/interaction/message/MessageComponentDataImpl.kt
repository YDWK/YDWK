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
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.interaction.message.MessageComponentData
import io.github.ydwk.ydwk.interaction.message.MessageComponentType
import io.github.ydwk.ydwk.util.EntityToStringBuilder

class MessageComponentDataImpl(override val ydwk: YDWK, override val json: JsonNode) :
    MessageComponentData {

    override val customId: String = json["custom_id"].asText()

    override val componentType: MessageComponentType =
        MessageComponentType.fromInt(json["component_type"].asInt())

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).toString()
    }
}
