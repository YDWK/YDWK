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

class SlashSubCommandGroup(
    val name: String,
    val description: String,
) {
    private val subCommands: MutableList<SlashSubCommand> = mutableListOf()

    fun addSubCommand(subCommand: SlashSubCommand): SlashSubCommandGroup {
        subCommands.add(subCommand)
        return this
    }

    fun addSubCommands(subCommands: List<SlashSubCommand>): SlashSubCommandGroup {
        this.subCommands.addAll(subCommands)
        return this
    }

    fun addSubCommands(vararg subCommands: SlashSubCommand): SlashSubCommandGroup {
        this.subCommands.addAll(subCommands)
        return this
    }

    fun toJson(): JsonNode {
        val mapper = ObjectMapper()
        val node = mapper.createObjectNode()
        node.put("name", name)
        node.put("description", description)
        node.put("type", SlashOptionType.SUB_COMMAND_GROUP.toInt())
        val subCommandsNode = mapper.createArrayNode()
        subCommands.forEach { subCommandsNode.add(it.toJson()) }
        node.set<JsonNode>("options", subCommandsNode)
        return node
    }
}
