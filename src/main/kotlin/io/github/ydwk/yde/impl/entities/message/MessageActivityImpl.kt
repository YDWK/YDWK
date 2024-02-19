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
package io.github.ydwk.yde.impl.entities.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.message.MessageActivity
import io.github.ydwk.yde.entities.message.activity.MessageActivityType
import io.github.ydwk.yde.util.EntityToStringBuilder

class MessageActivityImpl(override val yde: YDE, override val json: JsonNode) : MessageActivity {
    override val type: MessageActivityType
        get() = MessageActivityType.fromInt(json.get("type").asInt())

    override val partyId: String?
        get() = if (json.has("party_id")) json.get("party_id").asText() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
