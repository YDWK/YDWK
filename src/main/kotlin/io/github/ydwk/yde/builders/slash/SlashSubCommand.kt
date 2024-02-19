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

class SlashSubCommand(val name: String, val description: String) {

    private var options: MutableList<SlashOption> = mutableListOf()

    fun addOption(option: SlashOption): SlashSubCommand {
        options.add(option)
        return this
    }

    fun addOptions(options: List<SlashOption>): SlashSubCommand {
        this.options.addAll(options)
        return this
    }

    fun addOptions(vararg options: SlashOption): SlashSubCommand {
        this.options.addAll(options)
        return this
    }

    fun toJson(): JsonNode {
        val mapper = ObjectMapper()
        val node = mapper.createObjectNode()
        node.put("name", name)
        node.put("description", description)
        node.put("type", SlashOptionType.SUB_COMMAND.toInt())
        val optionsNode = mapper.createArrayNode()
        options.forEach { optionsNode.add(it.toJson()) }
        node.set<JsonNode>("options", optionsNode)
        return node
    }
}
