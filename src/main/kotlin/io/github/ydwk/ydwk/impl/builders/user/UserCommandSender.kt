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
package io.github.ydwk.ydwk.impl.builders.user

import io.github.ydwk.ydwk.builders.user.UserCommandBuilder
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.rest.EndPoint
import okhttp3.RequestBody.Companion.toRequestBody

class UserCommandSender(
    val ydwk: YDWKImpl,
    private val guildIds: MutableList<String>,
    val applicationId: String,
    private val userCommands: MutableList<UserCommandBuilder>
) {

    init {
        ydwk.logger.info("Sending User Commands to Discord")

        val currentGlobalUserCommandsNameAndId = getCurrentGlobalUserCommandsNameAndIds()
        val currentGuildUserCommandsNameAndId: Map<String, Map<Long, String>> =
            if (guildIds.isNotEmpty()) {
                getCurrentGuildUserCommandsNameAndIds()
            } else {
                emptyMap()
            }

        val globalUserCommands: MutableMap<String, UserCommandBuilder> = HashMap()
        val guildUserCommands: MutableMap<String, UserCommandBuilder> = HashMap()

        userCommands.forEach { user ->
            if (!user.specificGuildOnly) {
                globalUserCommands[user.name] = user
            } else {
                guildUserCommands[user.name] = user
            }
        }

        val globalUserCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val guildUserCommandsIdsToDelete: MutableList<Long> = mutableListOf()
        val globalUserCommandsToAdd: MutableList<UserCommandBuilder> = mutableListOf()
        val guildUserCommandsToAdd = mutableListOf<UserCommandBuilder>()

        if (currentGlobalUserCommandsNameAndId.isNotEmpty()) {
            currentGlobalUserCommandsNameAndId.forEach { (id, name) ->
                if (globalUserCommands.containsKey(name)) {
                    globalUserCommandsToAdd.add(globalUserCommands[name]!!)
                } else if (!globalUserCommands.containsKey(name)) {
                    globalUserCommandsIdsToDelete.add(id)
                } else {
                    globalUserCommandsToAdd.add(globalUserCommands[name]!!)
                }
            }

            globalUserCommands.forEach { (name, user) ->
                if (!currentGlobalUserCommandsNameAndId.containsValue(name)) {
                    globalUserCommandsToAdd.add(user)
                }
            }
        } else {
            globalUserCommandsToAdd.addAll(globalUserCommands.values)
        }

        if (currentGuildUserCommandsNameAndId.isNotEmpty()) {
            currentGuildUserCommandsNameAndId.forEach { (_, nameAndId) ->
                nameAndId.forEach { (id, name) ->
                    if (guildUserCommands.containsKey(name)) {
                        guildUserCommandsToAdd.add(guildUserCommands[name]!!)
                    } else if (!guildUserCommands.containsKey(name)) {
                        guildUserCommandsIdsToDelete.add(id)
                    } else {
                        guildUserCommandsToAdd.add(guildUserCommands[name]!!)
                    }
                }
            }

            guildUserCommands.forEach { (name, user) ->
                currentGuildUserCommandsNameAndId.forEach { (_, nameAndId) ->
                    if (!nameAndId.containsValue(name)) {
                        guildUserCommandsToAdd.add(user)
                    }
                }
            }
        } else {
            guildUserCommandsToAdd.addAll(guildUserCommands.values)
        }

        // being rate limited, do 5 at a time
        val globalUserCommandsToAddChunks = globalUserCommandsToAdd.chunked(1)
        val guildUserCommandsToAddChunks = guildUserCommandsToAdd.chunked(1)

        if (globalUserCommandsToAddChunks.isNotEmpty()) {
            var amountAdded = 0
            globalUserCommandsToAddChunks.forEach {
                if (amountAdded >= 4) {
                    ydwk.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                    Thread.sleep(25000)
                    amountAdded = 0
                    createGlobalUserCommands(it)
                } else {
                    amountAdded++
                    createGlobalUserCommands(it)
                }
            }
        }

        if (guildUserCommandsToAddChunks.isNotEmpty()) {
            var amountAdded = 0
            guildUserCommandsToAddChunks.forEach { chunk ->
                guildIds.forEach { guildId ->
                    if (amountAdded >= 4) {
                        ydwk.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                        Thread.sleep(25000)
                        createGuildUserCommands(guildId, chunk)
                    } else {
                        amountAdded++
                        createGuildUserCommands(guildId, chunk)
                    }
                }
            }
        }

        val globalUserCommandsIdsToDeleteChunks = globalUserCommandsIdsToDelete.chunked(1)
        val guildUserCommandsIdsToDeleteChunks = guildUserCommandsIdsToDelete.chunked(1)

        if (globalUserCommandsIdsToDeleteChunks.isNotEmpty()) {
            var amountDeleted = 0
            globalUserCommandsIdsToDeleteChunks.forEach { chunk ->
                if (amountDeleted >= 4) {
                    ydwk.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                    Thread.sleep(25000)
                    amountDeleted = 0
                    deleteGlobalUserCommands(chunk)
                } else {
                    amountDeleted++
                    deleteGlobalUserCommands(chunk)
                }
            }
        }

        if (guildUserCommandsIdsToDeleteChunks.isNotEmpty()) {
            guildIds.forEach { _ ->
                var amountDeleted = 0
                guildUserCommandsIdsToDeleteChunks.forEach { chunk ->
                    if (amountDeleted >= 4) {
                        ydwk.logger.debug("Sleeping for 25 seconds to avoid rate limit")
                        Thread.sleep(25000)
                        amountDeleted = 0
                        deleteGuildUserCommands(chunk)
                    } else {
                        amountDeleted++
                        deleteGuildUserCommands(chunk)
                    }
                }
            }
        }
    }

    private fun getCurrentGlobalUserCommandsNameAndIds(): Map<Long, String> {
        return ydwk.restApiManager
            .get(EndPoint.ApplicationCommandsEndpoint.GET_GLOBAL_COMMANDS, applicationId)
            .execute { it ->
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    return@execute emptyMap()
                } else {
                    return@execute jsonBody.associate { it["id"].asLong() to it["name"].asText() }
                }
            }
            .get()
    }

    private fun getCurrentGuildUserCommandsNameAndIds(): Map<String, Map<Long, String>> {
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

    private fun deleteGlobalUserCommands(ids: List<Long>) {
        ids.forEach { id ->
            ydwk.logger.debug("Deleting global User command with id $id")
            ydwk.restApiManager
                .delete(
                    EndPoint.ApplicationCommandsEndpoint.DELETE_GLOBAL_COMMAND,
                    applicationId,
                    id.toString())
                .executeWithNoResult()
        }
    }

    private fun deleteGuildUserCommands(ids: List<Long>) {
        ids.forEach { id ->
            guildIds.forEach { guildId ->
                ydwk.logger.debug("Deleting guild user command $id")
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

    private fun createGlobalUserCommands(userCommands: List<UserCommandBuilder>) {
        userCommands.forEach { user ->
            ydwk.logger.debug("Sending global User command ${user.name} to Discord")
            ydwk.restApiManager
                .post(
                    user.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND,
                    applicationId)
                .executeWithNoResult()
        }
    }

    private fun createGuildUserCommands(guildId: String, userCommands: List<UserCommandBuilder>) {
        userCommands.forEach { user ->
            ydwk.logger.debug("Sending User command ${user.name} to guild $guildId")
            ydwk.restApiManager
                .post(
                    user.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND,
                    applicationId,
                    guildId)
                .executeWithNoResult()
        }
    }
}
