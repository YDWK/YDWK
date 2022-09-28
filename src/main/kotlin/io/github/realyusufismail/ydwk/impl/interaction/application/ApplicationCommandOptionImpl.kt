/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.impl.interaction.application

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.interaction.application.ApplicationCommandOption
import io.github.realyusufismail.ydwk.interaction.application.ApplicationCommandType

class ApplicationCommandOptionImpl(override val ydwk: YDWK, override val json: JsonNode) :
    ApplicationCommandOption {
    override val name: String = json["name"].asText()

    override val type: ApplicationCommandType = ApplicationCommandType.fromInt(json["type"].asInt())

    override val value: Any? =
        when {
            json.has("value") -> {
                val value = json["value"]

                if (value.isInt) value.asInt()
                else if (value.isLong) value.asLong()
                else if (value.isTextual) value.asText()
                else if (value.isDouble) value.asDouble()
                else if (value.isFloat) value.asDouble() else null
            }
            else -> {
                null
            }
        }

    override val options: List<ApplicationCommandOption> =
        if (json.has("options")) json["options"].map { ApplicationCommandOptionImpl(ydwk, it) }
        else emptyList()

    override val focused: Boolean? = if (json.has("focused")) json["focused"].asBoolean() else null
}
