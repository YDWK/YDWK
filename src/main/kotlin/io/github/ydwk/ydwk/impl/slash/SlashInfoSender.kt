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
import io.github.ydwk.ydwk.slash.Slash
import okhttp3.RequestBody.Companion.toRequestBody

class SlashInfoSender(
    val ydwk: YDWKImpl,
    private val guildIds: MutableList<String>,
    val applicationId: String,
    slashCommands: MutableList<Slash>
) {
    init {
        ydwk.logger.info("Sending slash commands to Discord")

        val currentGlobalSlashCommandsNameAndId = getCurrentGlobalSlashCommandsNameAndIds()
        val currentGuildSlashCommandsNameAndId: Map<String, Map<Long, String>> =
            if (guildIds.isNotEmpty()) {
                getCurrentGuildSlashCommandsNameAndIds()
            } else {
                emptyMap()
            }

        val globalSlashCommands: MutableMap<String, Slash> = HashMap()
        val guildSlashCommands: MutableMap<String, Slash> = HashMap()

        slashCommands.forEach { slash ->
            if (!slash.specificGuildOnly) {
                globalSlashCommands[slash.name] = slash
            } else {
                guildSlashCommands[slash.name] = slash
            }
        }

        val globalSlashCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val guildSlashCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val globalSlashCommandsToAdd: MutableList<Slash> = mutableListOf()
        val guildSlashCommandsToAdd = mutableListOf<Slash>()

        if (currentGlobalSlashCommandsNameAndId.isNotEmpty()) {
            currentGlobalSlashCommandsNameAndId.forEach { (id, name) ->
                if (globalSlashCommands.containsKey(name)) {
                    ydwk.logger.info("Slash command $name already exists, skipping")
                    globalSlashCommandsToAdd.add(globalSlashCommands[name]!!)
                } else if (!globalSlashCommands.containsKey(name)) {
                    ydwk.logger.info("Slash command $name does not exist, deleting")
                    globalSlashCommandsIdsToDelete.add(id)
                } else {
                    globalSlashCommandsToAdd.add(globalSlashCommands[name]!!)
                }
            }
        } else {
            globalSlashCommandsToAdd.addAll(globalSlashCommands.values)
        }

        if (currentGuildSlashCommandsNameAndId.isNotEmpty()) {
            currentGuildSlashCommandsNameAndId.forEach { (_, nameAndId) ->
                nameAndId.forEach { (id, name) ->
                    if (guildSlashCommands.containsKey(name)) {
                        guildSlashCommandsToAdd.add(guildSlashCommands[name]!!)
                    } else if (!guildSlashCommands.containsKey(name)) {
                        guildSlashCommandsIdsToDelete.add(id)
                    } else {
                        guildSlashCommandsToAdd.add(guildSlashCommands[name]!!)
                    }
                }
            }
        } else {
            guildSlashCommandsToAdd.addAll(guildSlashCommands.values)
        }

        // being rate limited, do 5 at a time
        val globalSlashCommandsToAddChunks = globalSlashCommandsToAdd.chunked(4)
        val guildSlashCommandsToAddChunks = guildSlashCommandsToAdd.chunked(4)

        if (globalSlashCommandsToAddChunks.isNotEmpty()) {
            globalSlashCommandsToAddChunks.forEach { chunk -> createGlobalSlashCommands(chunk) }
        }

        if (guildSlashCommandsToAddChunks.isNotEmpty()) {
            guildSlashCommandsToAddChunks.forEach { chunk ->
                guildIds.forEach { guildId -> createGuildSlashCommands(guildId, chunk) }
            }
        }

        val globalSlashCommandsIdsToDeleteChunks = globalSlashCommandsIdsToDelete.chunked(4)
        val guildSlashCommandsIdsToDeleteChunks = guildSlashCommandsIdsToDelete.chunked(4)

        if (globalSlashCommandsIdsToDelete.isNotEmpty()) {
            globalSlashCommandsIdsToDeleteChunks.forEach { chunk ->
                deleteGlobalSlashCommands(chunk)
            }
        }

        if (guildSlashCommandsIdsToDelete.isNotEmpty()) {
            guildIds.forEach { guildId ->
                guildSlashCommandsIdsToDeleteChunks.forEach { chunk ->
                    deleteGuildSlashCommands(chunk)
                }
            }
        }
    }

    private fun getCurrentGlobalSlashCommandsNameAndIds(): Map<Long, String> {
        return ydwk.restApiManager
            .get(EndPoint.ApplicationCommandsEndpoint.GET_GLOBAL_COMMANDS, applicationId)
            .execute { it ->
                ydwk.logger.debug("Getting current global slash commands")
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    return@execute emptyMap()
                } else {
                    return@execute jsonBody.associate { it["id"].asLong() to it["name"].asText() }
                }
            }
            .get()
    }

    private fun getCurrentGuildSlashCommandsNameAndIds(): Map<String, Map<Long, String>> {
        return guildIds.associateWith { guildId ->
            ydwk.restApiManager
                .get(
                    EndPoint.ApplicationCommandsEndpoint.GET_GUILD_COMMANDS, applicationId, guildId)
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
        }
    }

    private fun deleteGlobalSlashCommands(ids: List<Long>) {
        ids.forEach { id ->
            ydwk.logger.info("Deleting global slash command with id $id")
            ydwk.restApiManager
                .delete(
                    EndPoint.ApplicationCommandsEndpoint.DELETE_GLOBAL_COMMAND,
                    applicationId,
                    id.toString())
                .executeWithNoResult()
        }
    }

    private fun deleteGuildSlashCommands(ids: List<Long>) {
        ids.forEach { id ->
            guildIds.forEach { guildId ->
                ydwk.logger.info("Deleting guild slash command $id")
                ydwk.restApiManager
                    .delete(
                        EndPoint.ApplicationCommandsEndpoint.DELETE_GUILD_COMMAND,
                        applicationId,
                        guildId,
                        id.toString())
                    .executeWithNoResult()
            }
        }
    }

    private fun createGlobalSlashCommands(slashCommands: List<Slash>) {
        slashCommands.forEach { slash ->
            ydwk.logger.info("Sending global slash command ${slash.name} to Discord")
            ydwk.restApiManager
                .post(
                    slash.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND,
                    applicationId)
                .executeWithNoResult()
        }
    }

    private fun createGuildSlashCommands(guildId: String, slashCommands: List<Slash>) {
        slashCommands.forEach { slash ->
            ydwk.logger.info("Sending slash command ${slash.name} to guild $guildId")
            ydwk.restApiManager
                .post(
                    slash.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND,
                    applicationId,
                    guildId)
                .executeWithNoResult()
        }
    }
}
