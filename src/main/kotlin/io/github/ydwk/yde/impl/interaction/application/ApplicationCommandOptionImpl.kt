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
package io.github.ydwk.yde.impl.interaction.application

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.builders.slash.SlashOptionType
import io.github.ydwk.yde.interaction.application.ApplicationCommandOption
import io.github.ydwk.yde.util.EntityToStringBuilder

class ApplicationCommandOptionImpl(override val yde: YDE, override val json: JsonNode) :
    ApplicationCommandOption {
    override var name: String = json["name"].asText()

    override val type: SlashOptionType = SlashOptionType.fromInt(json["type"].asInt())

    override val value: JsonNode = json["value"]

    override val options: List<ApplicationCommandOption> =
        if (json.has("options")) json["options"].map { ApplicationCommandOptionImpl(yde, it) }
        else emptyList()

    override val focused: Boolean? = if (json.has("focused")) json["focused"].asBoolean() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
