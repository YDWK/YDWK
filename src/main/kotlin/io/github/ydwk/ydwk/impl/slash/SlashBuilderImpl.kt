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
package io.github.ydwk.ydwk.impl.slash

import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.rest.cf.CompletableFutureManager
import io.github.ydwk.ydwk.slash.Slash
import io.github.ydwk.ydwk.slash.SlashBuilder
import io.github.ydwk.ydwk.util.Checks
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class SlashBuilderImpl(
    private val ydwk: YDWKImpl,
    private val guildIds: MutableList<String>,
    private val applicationId: String
) : SlashBuilder {
    private val slashCommands: MutableList<Slash> = mutableListOf()

    override fun addSlashCommand(slash: Slash): SlashBuilder {
        slashCommands.add(slash)
        return this
    }

    override fun addSlashCommands(slashes: List<Slash>): SlashBuilder {
        slashCommands.addAll(slashes)
        return this
    }

    override fun addSlashCommands(vararg slashes: Slash): SlashBuilder {
        slashCommands.addAll(slashes)
        return this
    }

    override fun getSlashCommands(): List<Slash> {
        return slashCommands
    }

    override fun removeSlashCommand(slash: Slash): SlashBuilder {
        slashCommands.remove(slash)
        return this
    }

    override fun removeSlashCommands(slashes: List<Slash>): SlashBuilder {
        slashCommands.removeAll(slashes)
        return this
    }

    override fun removeSlashCommands(vararg slashes: Slash): SlashBuilder {
        slashCommands.removeAll(slashes.toSet())
        return this
    }

    override fun removeAllSlashCommands(): SlashBuilder {
        slashCommands.clear()
        return this
    }

    override fun build() {
        val rest = ydwk.restApiManager

        val currentGlobalCommandIdAndNameMap: Map<Long, String> =
            rest
                .get(EndPoint.ApplicationCommandsEndpoint.GET_GLOBAL_COMMANDS, applicationId)
                .execute { it ->
                    val jsonBody = it.jsonBody
                    if (jsonBody == null) {
                        return@execute emptyMap()
                    } else {
                        return@execute jsonBody.associate {
                            it["id"].asLong() to it["name"].asText()
                        }
                    }
                }
                .get()

        // first id of the guild,
        val currentGuildCommandIdAndNameMap: MutableMap<MutableMap<Long, Long>, String> =
            mutableMapOf()
        if (guildIds.isNotEmpty()) {
            guildIds.forEach { it ->
                rest
                    .get(EndPoint.ApplicationCommandsEndpoint.GET_GUILD_COMMANDS, applicationId, it)
                    .execute(
                        fun(it: CompletableFutureManager): Map<MutableMap<Long, Long>, String> {
                            val jsonBody = it.jsonBody
                            return jsonBody?.associate {
                                mutableMapOf(it["guild_id"].asLong() to it["id"].asLong()) to
                                    it["name"].asText()
                            }
                                ?: emptyMap()
                        })
                    .get()
            }
        }

        val globalSlash = mutableListOf<Slash>()
        val guildSlash = mutableListOf<Slash>()
        val globalCommandsToDelete = mutableListOf<Long>()
        val guildCommandsToDelete: MutableMap<Long, Long> = mutableMapOf()
        val globalCommandToAdd = mutableListOf<Slash>()
        val guildCommandToAdd = mutableListOf<Slash>()

        for (slash in slashCommands) {
            Checks.checkIfCapital(slash.name, "Slash command name must be lowercase")
            Checks.checkLength(
                slash.name, 32, "Slash command name can not be longer than 32 characters")
            Checks.checkLength(
                slash.description,
                100,
                "Slash command description can not be longer than 100 characters")

            if (slash.guildOnly) {
                guildSlash.add(slash)
            } else {
                globalSlash.add(slash)
            }
        }

        for (slash in globalSlash) {
            if (currentGlobalCommandIdAndNameMap.containsValue(slash.name)) {
                ydwk.logger.debug("Global slash command ${slash.name} already exists, updating...")
                globalCommandToAdd.add(slash)
            } else if (!currentGlobalCommandIdAndNameMap.containsValue(slash.name)) {
                ydwk.logger.debug("Global slash command ${slash.name} does not exist, creating...")
                globalCommandToAdd.add(slash)
            } else {
                ydwk.logger.debug(
                    "Global slash command ${slash.name} no longer exists, deleting...")
                globalCommandsToDelete.add(
                    currentGlobalCommandIdAndNameMap.filterValues { it == slash.name }.keys.first())
            }
        }

        for (slash in guildSlash) {
            if (currentGuildCommandIdAndNameMap.containsValue(slash.name)) {
                ydwk.logger.debug("Guild slash command ${slash.name} already exists, updating...")
                guildCommandToAdd.add(slash)
            } else if (!currentGuildCommandIdAndNameMap.containsValue(slash.name)) {
                ydwk.logger.debug("Guild slash command ${slash.name} does not exist, creating...")
                guildCommandToAdd.add(slash)
            } else {
                ydwk.logger.debug("Guild slash command ${slash.name} no longer exists, deleting...")
                guildCommandsToDelete.putAll(
                    currentGuildCommandIdAndNameMap.filterValues { it == slash.name }.keys.first())
            }
        }

        for (slash in globalCommandToAdd) {
            addGlobalSlashCommand(rest, slash.toJson().toString().toRequestBody())
        }

        for (slash in guildCommandToAdd) {
            if (guildIds.isNotEmpty()) {
                guildIds.forEach { it ->
                    addGuildSlashCommand(rest, it, slash.toJson().toString().toRequestBody())
                }
            }
        }

        for (slash in globalCommandsToDelete) {
            deleteGlobalSlashCommand(rest, slash)
        }

        for (slash in guildCommandsToDelete) {
            deleteGuildSlashCommand(rest, slash.key, slash.value)
        }
    }

    private fun addGuildSlashCommand(rest: RestApiManager, guildId: String, json: RequestBody) {
        rest
            .post(
                json,
                EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND,
                applicationId,
                guildId)
            .execute()
    }

    private fun addGlobalSlashCommand(rest: RestApiManager, json: RequestBody) {
        rest
            .post(json, EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND, applicationId)
            .execute()
    }

    private fun deleteGlobalSlashCommand(rest: RestApiManager, slashId: Long) {
        rest
            .delete(
                EndPoint.ApplicationCommandsEndpoint.DELETE_GLOBAL_COMMAND,
                applicationId,
                slashId.toString())
            .execute()
    }

    private fun deleteGuildSlashCommand(rest: RestApiManager, guildId: Long, slashId: Long) {
        rest
            .delete(
                EndPoint.ApplicationCommandsEndpoint.DELETE_GUILD_COMMAND,
                applicationId,
                guildId.toString(),
                slashId.toString())
            .execute()
    }
}
