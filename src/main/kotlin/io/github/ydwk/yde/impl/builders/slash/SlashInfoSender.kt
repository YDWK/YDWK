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
package io.github.ydwk.ydwk.impl.builders.slash

import io.github.ydwk.yde.builders.slash.SlashCommandBuilder
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.rest.EndPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody

class SlashInfoSender(
    val yde: YDEImpl,
    private val guildIds: MutableList<String>,
    val applicationId: String,
    slashCommands: List<SlashCommandBuilder>
) {
    init {
        yde.logger.info("Sending slash commands to Discord")

        val currentGlobalSlashCommandsNameAndId = runBlocking {
            getCurrentGlobalSlashCommandsNameAndIds()
        }

        val currentGuildSlashCommandsNameAndId: Map<String, Map<Long, String>> = runBlocking {
            if (guildIds.isNotEmpty()) {
                getCurrentGuildSlashCommandsNameAndIds()
            } else {
                emptyMap()
            }
        }

        val globalSlashCommands: MutableMap<String, SlashCommandBuilder> = HashMap()
        val guildSlashCommands: MutableMap<String, SlashCommandBuilder> = HashMap()

        slashCommands.forEach { slash ->
            if (!slash.specificGuildOnly) {
                globalSlashCommands[slash.name] = slash
            } else {
                guildSlashCommands[slash.name] = slash
            }
        }

        val globalSlashCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val guildSlashCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val globalSlashCommandsToAdd: MutableList<SlashCommandBuilder> = mutableListOf()
        val guildSlashCommandsToAdd = mutableListOf<SlashCommandBuilder>()

        if (currentGlobalSlashCommandsNameAndId.isNotEmpty()) {
            currentGlobalSlashCommandsNameAndId.forEach { (id, name) ->
                if (globalSlashCommands.containsKey(name)) {
                    globalSlashCommandsToAdd.add(globalSlashCommands[name]!!)
                } else if (!globalSlashCommands.containsKey(name)) {
                    globalSlashCommandsIdsToDelete.add(id)
                } else {
                    globalSlashCommandsToAdd.add(globalSlashCommands[name]!!)
                }
            }

            globalSlashCommands.forEach { (name, slash) ->
                if (!currentGlobalSlashCommandsNameAndId.containsValue(name)) {
                    globalSlashCommandsToAdd.add(slash)
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

            guildSlashCommands.forEach { (name, slash) ->
                currentGuildSlashCommandsNameAndId.forEach { (_, nameAndId) ->
                    if (!nameAndId.containsValue(name)) {
                        guildSlashCommandsToAdd.add(slash)
                    }
                }
            }
        } else {
            guildSlashCommandsToAdd.addAll(guildSlashCommands.values)
        }

        // being rate limited, do 5 at a time
        val globalSlashCommandsToAddChunks = globalSlashCommandsToAdd.chunked(1)
        val guildSlashCommandsToAddChunks = guildSlashCommandsToAdd.chunked(1)

        if (globalSlashCommandsToAddChunks.isNotEmpty()) {
            var amountAdded = 0
            globalSlashCommandsToAddChunks.forEach {
                if (amountAdded >= 4) {
                    yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                    Thread.sleep(25000)
                    amountAdded = 0
                    createGlobalSlashCommands(it)
                } else {
                    amountAdded++
                    createGlobalSlashCommands(it)
                }
            }
        }

        if (guildSlashCommandsToAddChunks.isNotEmpty()) {
            var amountAdded = 0
            guildSlashCommandsToAddChunks.forEach { chunk ->
                guildIds.forEach { guildId ->
                    if (amountAdded >= 4) {
                        yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                        Thread.sleep(25000)
                        createGuildSlashCommands(guildId, chunk)
                    } else {
                        amountAdded++
                        createGuildSlashCommands(guildId, chunk)
                    }
                }
            }
        }

        val globalSlashCommandsIdsToDeleteChunks = globalSlashCommandsIdsToDelete.chunked(1)
        val guildSlashCommandsIdsToDeleteChunks = guildSlashCommandsIdsToDelete.chunked(1)

        if (globalSlashCommandsIdsToDeleteChunks.isNotEmpty()) {
            var amountDeleted = 0
            globalSlashCommandsIdsToDeleteChunks.forEach { chunk ->
                if (amountDeleted >= 4) {
                    yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                    Thread.sleep(25000)
                    amountDeleted = 0
                    deleteGlobalSlashCommands(chunk)
                } else {
                    amountDeleted++
                    deleteGlobalSlashCommands(chunk)
                }
            }
        }

        if (guildSlashCommandsIdsToDeleteChunks.isNotEmpty()) {
            guildIds.forEach { _ ->
                var amountDeleted = 0
                guildSlashCommandsIdsToDeleteChunks.forEach { chunk ->
                    if (amountDeleted >= 4) {
                        yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                        Thread.sleep(25000)
                        amountDeleted = 0
                        deleteGuildSlashCommands(chunk)
                    } else {
                        amountDeleted++
                        deleteGuildSlashCommands(chunk)
                    }
                }
            }
        }
    }

    private suspend fun getCurrentGlobalSlashCommandsNameAndIds(): Map<Long, String> {
        return withContext(Dispatchers.IO) {
            yde.restApiManager
                .get(EndPoint.ApplicationCommandsEndpoint.GET_GLOBAL_COMMANDS, applicationId)
                .execute { it ->
                    yde.logger.debug("Getting current global slash commands")
                    val jsonBody = it.jsonBody
                    if (jsonBody == null) {
                        return@execute emptyMap()
                    } else {
                        return@execute jsonBody.associate {
                            it["id"].asLong() to it["name"].asText()
                        }
                    }
                }
                .await()
        }
    }

    private suspend fun getCurrentGuildSlashCommandsNameAndIds(): Map<String, Map<Long, String>> {
        return withContext(Dispatchers.IO) {
            guildIds.associateWith { guildId ->
                yde.restApiManager
                    .get(
                        EndPoint.ApplicationCommandsEndpoint.GET_GUILD_COMMANDS,
                        applicationId,
                        guildId)
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
                    .await()
            }
        }
    }

    private fun deleteGlobalSlashCommands(ids: List<Long>) {
        ids.forEach { id ->
            yde.logger.debug("Deleting global slash command with id $id")
            yde.restApiManager
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
                yde.logger.debug("Deleting guild slash command $id")
                yde.restApiManager
                    .delete(
                        EndPoint.ApplicationCommandsEndpoint.DELETE_GUILD_COMMAND,
                        applicationId,
                        guildId,
                        id.toString())
                    .executeWithNoResult()
            }
        }
    }

    private fun createGlobalSlashCommands(slashCommands: List<SlashCommandBuilder>) {
        slashCommands.forEach { slash ->
            yde.logger.debug("Sending global slash command ${slash.name} to Discord")
            yde.restApiManager
                .post(
                    slash.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND,
                    applicationId)
                .executeWithNoResult()
        }
    }

    private fun createGuildSlashCommands(
        guildId: String,
        slashCommands: List<SlashCommandBuilder>
    ) {
        slashCommands.forEach { slash ->
            yde.logger.debug("Sending slash command ${slash.name} to guild $guildId")
            yde.restApiManager
                .post(
                    slash.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND,
                    applicationId,
                    guildId)
                .executeWithNoResult()
        }
    }
}
