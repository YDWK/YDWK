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

/**
 * A builder for Slash Commands
 *
 * @param name The name of the slash command.
 * @param description The description of the slash command.
 * @param guildOnly Whether the slash command can only be used in guilds.
 * @param specificGuildOnly Whether the slash command can only be used in a specific guild/s.
 */
class Slash(
    val name: String,
    val description: String,
    private val guildOnly: Boolean = false,
    val specificGuildOnly: Boolean = false
) {
    private var options: MutableList<SlashOption> = mutableListOf()

    fun addOption(option: SlashOption): Slash {
        options.add(option)
        return this
    }

    fun addOptions(options: List<SlashOption>): Slash {
        this.options.addAll(options)
        return this
    }

    fun addOptions(vararg options: SlashOption): Slash {
        this.options.addAll(options)
        return this
    }

    fun toJson(): JsonNode {
        val json = ObjectMapper().createObjectNode()
        json.put("name", name)
        json.put("description", description)
        json.put("type", ApplicationCommandType.CHAT_INPUT.toInt())
        if (guildOnly) {
            json.put("dm_permission", false)
        }
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
