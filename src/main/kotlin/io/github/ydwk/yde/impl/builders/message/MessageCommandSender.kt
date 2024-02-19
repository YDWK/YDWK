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
package io.github.ydwk.yde.impl.builders.message

import io.github.ydwk.yde.builders.message.MessageCommandBuilder
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.rest.EndPoint
import kotlinx.coroutines.*
import okhttp3.RequestBody.Companion.toRequestBody

class MessageCommandSender(
    val yde: YDEImpl,
    private val guildIds: MutableList<String>,
    val applicationId: String,
    private val messageCommands: MutableList<MessageCommandBuilder>
) {

    init {
        yde.logger.info("Sending Message Commands to Discord")

        val currentGlobalMessageCommandsNameAndId: Map<Long, String> = runBlocking {
            getCurrentGlobalMessageCommandsNameAndIds()
        }

        val currentGuildMessageCommandsNameAndId: Map<String, Map<Long, String>> = runBlocking {
            if (guildIds.isNotEmpty()) {
                getCurrentGuildMessageCommandsNameAndIds()
            } else {
                emptyMap()
            }
        }

        val globalMessageCommands: MutableMap<String, MessageCommandBuilder> = HashMap()
        val guildMessageCommands: MutableMap<String, MessageCommandBuilder> = HashMap()

        messageCommands.forEach { message ->
            if (!message.specificGuildOnly) {
                globalMessageCommands[message.name] = message
            } else {
                guildMessageCommands[message.name] = message
            }
        }

        val globalMessageCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val guildMessageCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val globalMessageCommandsToAdd: MutableList<MessageCommandBuilder> = mutableListOf()
        val guildMessageCommandsToAdd = mutableListOf<MessageCommandBuilder>()

        if (currentGlobalMessageCommandsNameAndId.isNotEmpty()) {
            currentGlobalMessageCommandsNameAndId.forEach { (id, name) ->
                if (globalMessageCommands.containsKey(name)) {
                    globalMessageCommandsToAdd.add(globalMessageCommands[name]!!)
                } else if (!globalMessageCommands.containsKey(name)) {
                    globalMessageCommandsIdsToDelete.add(id)
                } else {
                    globalMessageCommandsToAdd.add(globalMessageCommands[name]!!)
                }
            }

            globalMessageCommands.forEach { (name, message) ->
                if (!currentGlobalMessageCommandsNameAndId.containsValue(name)) {
                    globalMessageCommandsToAdd.add(message)
                }
            }
        } else {
            globalMessageCommandsToAdd.addAll(globalMessageCommands.values)
        }

        if (currentGuildMessageCommandsNameAndId.isNotEmpty()) {
            currentGuildMessageCommandsNameAndId.forEach { (_, nameAndId) ->
                nameAndId.forEach { (id, name) ->
                    if (guildMessageCommands.containsKey(name)) {
                        guildMessageCommandsToAdd.add(guildMessageCommands[name]!!)
                    } else if (!guildMessageCommands.containsKey(name)) {
                        guildMessageCommandsIdsToDelete.add(id)
                    } else {
                        guildMessageCommandsToAdd.add(guildMessageCommands[name]!!)
                    }
                }
            }

            guildMessageCommands.forEach { (name, message) ->
                currentGuildMessageCommandsNameAndId.forEach { (_, nameAndId) ->
                    if (!nameAndId.containsValue(name)) {
                        guildMessageCommandsToAdd.add(message)
                    }
                }
            }
        } else {
            guildMessageCommandsToAdd.addAll(guildMessageCommands.values)
        }

        // being rate limited, do 5 at a time
        val globalMessageCommandsToAddChunks = globalMessageCommandsToAdd.chunked(1)
        val guildMessageCommandsToAddChunks = guildMessageCommandsToAdd.chunked(1)

        if (globalMessageCommandsToAddChunks.isNotEmpty()) {
            var amountAdded = 0
            globalMessageCommandsToAddChunks.forEach {
                if (amountAdded >= 4) {
                    yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                    Thread.sleep(25000)
                    amountAdded = 0
                    createGlobalMessageCommands(it)
                } else {
                    amountAdded++
                    createGlobalMessageCommands(it)
                }
            }
        }

        if (guildMessageCommandsToAddChunks.isNotEmpty()) {
            var amountAdded = 0
            guildMessageCommandsToAddChunks.forEach { chunk ->
                guildIds.forEach { guildId ->
                    if (amountAdded >= 4) {
                        yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                        Thread.sleep(25000)
                        createGuildMessageCommands(guildId, chunk)
                    } else {
                        amountAdded++
                        createGuildMessageCommands(guildId, chunk)
                    }
                }
            }
        }

        val globalMessageCommandsIdsToDeleteChunks = globalMessageCommandsIdsToDelete.chunked(1)
        val guildMessageCommandsIdsToDeleteChunks = guildMessageCommandsIdsToDelete.chunked(1)

        if (globalMessageCommandsIdsToDeleteChunks.isNotEmpty()) {
            var amountDeleted = 0
            globalMessageCommandsIdsToDeleteChunks.forEach { chunk ->
                if (amountDeleted >= 4) {
                    yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                    Thread.sleep(25000)
                    amountDeleted = 0
                    deleteGlobalMessageCommands(chunk)
                } else {
                    amountDeleted++
                    deleteGlobalMessageCommands(chunk)
                }
            }
        }

        if (guildMessageCommandsIdsToDeleteChunks.isNotEmpty()) {
            guildIds.forEach { _ ->
                var amountDeleted = 0
                guildMessageCommandsIdsToDeleteChunks.forEach { chunk ->
                    if (amountDeleted >= 4) {
                        yde.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                        Thread.sleep(25000)
                        amountDeleted = 0
                        deleteGuildMessageCommands(chunk)
                    } else {
                        amountDeleted++
                        deleteGuildMessageCommands(chunk)
                    }
                }
            }
        }
    }

    private suspend fun getCurrentGlobalMessageCommandsNameAndIds(): Map<Long, String> {
        return withContext(Dispatchers.IO) {
            yde.restApiManager
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
                .await()
        }
    }

    private suspend fun getCurrentGuildMessageCommandsNameAndIds(): Map<String, Map<Long, String>> {
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

    private fun deleteGlobalMessageCommands(ids: List<Long>) {
        ids.forEach { id ->
            yde.logger.debug("Deleting global Message command with id $id")
            yde.restApiManager
                .delete(
                    EndPoint.ApplicationCommandsEndpoint.DELETE_GLOBAL_COMMAND,
                    applicationId,
                    id.toString())
                .executeWithNoResult()
        }
    }

    private fun deleteGuildMessageCommands(ids: List<Long>) {
        ids.forEach { id ->
            guildIds.forEach { guildId ->
                yde.logger.debug("Deleting guild Message command $id")
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

    private fun createGlobalMessageCommands(messageCommands: List<MessageCommandBuilder>) {
        messageCommands.forEach { message ->
            yde.logger.debug("Sending global Message command ${message.name} to Discord")
            yde.restApiManager
                .post(
                    message.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND,
                    applicationId)
                .executeWithNoResult()
        }
    }

    private fun createGuildMessageCommands(
        guildId: String,
        messageCommands: List<MessageCommandBuilder>
    ) {
        messageCommands.forEach { message ->
            yde.logger.debug("Sending Message command ${message.name} to guild $guildId")
            yde.restApiManager
                .post(
                    message.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND,
                    applicationId,
                    guildId)
                .executeWithNoResult()
        }
    }
}
