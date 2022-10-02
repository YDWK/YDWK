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
package io.github.ydwk.ydwk.slash

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandType
import io.github.ydwk.ydwk.util.Checks

class Slash(val name: String, val description: String, val guildOnly: Boolean = false) {
    private var options: MutableList<SlashOption> = mutableListOf()

    fun addOption(option: SlashOption) {
        options.add(option)
    }

    fun addOptions(options: List<SlashOption>) {
        this.options.addAll(options)
    }

    fun addOptions(vararg options: SlashOption) {
        this.options.addAll(options)
    }

    fun toJson(): JsonNode {
        val json = ObjectMapper().createObjectNode()
        json.put("name", name)
        json.put("description", description)
        json.put("type", ApplicationCommandType.CHAT_INPUT.toInt())
        val options: ArrayNode = ObjectMapper().createArrayNode()
        this.options.forEach { it ->
            Checks.checkLength(
                it.name, 32, "Option command name can not be longer than 32 characters")
            Checks.checkLength(
                it.description,
                100,
                "Option command description can not be longer than 100 characters")
            options.add(it.toJson())
        }
        json.set<ArrayNode>("options", options)
        return json
    }
}
