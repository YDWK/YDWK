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
package io.github.ydwk.yde.builders.slash

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

class SlashOption(
    val name: String,
    val description: String,
    val type: SlashOptionType,
    val required: Boolean = false,
) {
    private var choices: MutableList<SlashOptionChoice> = mutableListOf()

    fun addChoice(choice: SlashOptionChoice): SlashOption {
        choices.add(choice)
        return this
    }

    fun addChoices(choices: List<SlashOptionChoice>): SlashOption {
        this.choices.addAll(choices)
        return this
    }

    fun addChoices(vararg choices: SlashOptionChoice): SlashOption {
        this.choices.addAll(choices)
        return this
    }

    fun toJson(): JsonNode {
        val json = ObjectMapper().createObjectNode()
        json.put("name", name)
        json.put("description", description)
        json.put("type", type.toInt())
        json.put("required", required)
        val choices: ArrayNode = ObjectMapper().createArrayNode()
        this.choices.forEach { choices.add(it.toJson()) }
        json.set<ArrayNode>("choices", choices)
        return json
    }
}
