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
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.util.Checks

/**
 * A builder for Slash Commands
 *
 * @param name The name of the slash command.
 * @param description The description of the slash command.
 * @param guildOnly Whether the slash command can only be used in guilds.
 * @param specificGuildOnly Whether the slash command can only be used in a specific guild/s.
 */
class SlashCommandBuilder(
    val name: String,
    val description: String,
    private val guildOnly: Boolean = false,
    val specificGuildOnly: Boolean = false
) {
    private var options: MutableList<SlashOption> = mutableListOf()
    private var subCommandGroups: MutableList<SlashSubCommandGroup> = mutableListOf()
    private var subCommands: MutableList<SlashSubCommand> = mutableListOf()

    fun addOption(option: SlashOption): SlashCommandBuilder {
        options.add(option)
        return this
    }

    fun addOptions(options: List<SlashOption>): SlashCommandBuilder {
        this.options.addAll(options)
        return this
    }

    fun addOptions(vararg options: SlashOption): SlashCommandBuilder {
        this.options.addAll(options)
        return this
    }

    fun addSubCommand(subCommand: SlashSubCommand): SlashCommandBuilder {
        subCommands.add(subCommand)
        return this
    }

    fun addSubCommands(subCommands: List<SlashSubCommand>): SlashCommandBuilder {
        this.subCommands.addAll(subCommands)
        return this
    }

    fun addSubCommands(vararg subCommands: SlashSubCommand): SlashCommandBuilder {
        this.subCommands.addAll(subCommands)
        return this
    }

    fun addSubCommandGroup(subCommandGroup: SlashSubCommandGroup): SlashCommandBuilder {
        subCommandGroups.add(subCommandGroup)
        return this
    }

    fun addSubCommandGroups(subCommandGroups: List<SlashSubCommandGroup>): SlashCommandBuilder {
        this.subCommandGroups.addAll(subCommandGroups)
        return this
    }

    fun addSubCommandGroups(vararg subCommandGroups: SlashSubCommandGroup): SlashCommandBuilder {
        this.subCommandGroups.addAll(subCommandGroups)
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

        this.subCommands.forEach { it ->
            Checks.checkLength(it.name, 32, "Subcommand name can not be longer than 32 characters")
            Checks.checkLength(
                it.description, 100, "Subcommand description can not be longer than 100 characters")
            options.add(it.toJson())
        }

        this.subCommandGroups.forEach { it ->
            Checks.checkLength(
                it.name, 32, "Subcommand group name can not be longer than 32 characters")
            Checks.checkLength(
                it.description,
                100,
                "Subcommand group description can not be longer than 100 characters")
            options.add(it.toJson())
        }

        json.set<ArrayNode>("options", options)
        return json
    }
}
