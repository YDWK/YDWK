/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.impl.builders.user

import io.github.ydwk.yde.builders.user.UserCommandBuilder
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.builders.util.getCommandNameAndIds
import io.github.ydwk.yde.impl.builders.util.getCurrentGuildCommandsNameAndIds
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.util.LOOM
import kotlinx.coroutines.*
import okhttp3.RequestBody.Companion.toRequestBody

class UserCommandSender(
    val yde: YDEImpl,
    private val guildIds: MutableList<String>,
    val applicationId: String,
    private val userCommands: MutableList<UserCommandBuilder>
) {

    init {
        yde.logger.info("Sending User Commands to Discord")
        sendUserCommands()
    }

    private fun sendUserCommands() {
        CoroutineScope(Dispatchers.LOOM).launch {
            val currentGlobalUserCommandsNameAndId = getCurrentGlobalUserCommandsNameAndIds()
            val currentGuildUserCommandsNameAndId =
                if (guildIds.isNotEmpty()) getCurrentGuildUserCommandsNameAndIds() else emptyMap()

            val globalUserCommandsToAdd = mutableListOf<UserCommandBuilder>()
            val guildUserCommandsToAdd = mutableListOf<UserCommandBuilder>()
            val globalUserCommandsIdsToDelete = mutableListOf<Long>()
            val guildUserCommandsIdsToDelete = mutableListOf<Long>()

            // Populate lists of commands to add or delete
            populateCommandsToAddAndDelete(
                currentGlobalUserCommandsNameAndId,
                currentGuildUserCommandsNameAndId,
                globalUserCommandsToAdd,
                guildUserCommandsToAdd,
                globalUserCommandsIdsToDelete,
                guildUserCommandsIdsToDelete)

            // Add or delete global user commands
            processGlobalUserCommands(globalUserCommandsToAdd, globalUserCommandsIdsToDelete)

            // Add or delete guild user commands
            processGuildUserCommands(guildUserCommandsToAdd, guildUserCommandsIdsToDelete)
        }
    }

    private suspend fun getCurrentGlobalUserCommandsNameAndIds(): Map<Long, String> {
        return getCommandNameAndIds(yde, applicationId)
    }

    private suspend fun getCurrentGuildUserCommandsNameAndIds(): Map<String, Map<Long, String>> {
        return getCurrentGuildCommandsNameAndIds(yde, guildIds, applicationId)
    }

    private fun populateCommandsToAddAndDelete(
        currentGlobalUserCommandsNameAndId: Map<Long, String>,
        currentGuildUserCommandsNameAndId: Map<String, Map<Long, String>>,
        globalUserCommandsToAdd: MutableList<UserCommandBuilder>,
        guildUserCommandsToAdd: MutableList<UserCommandBuilder>,
        globalUserCommandsIdsToDelete: MutableList<Long>,
        guildUserCommandsIdsToDelete: MutableList<Long>
    ) {
        userCommands.forEach { user ->
            if (currentGlobalUserCommandsNameAndId.containsValue(user.name)) {
                val commandId =
                    currentGlobalUserCommandsNameAndId.filterValues { it == user.name }.keys.first()
                if (!user.specificGuildOnly) {
                    globalUserCommandsToAdd.add(user)
                } else {
                    globalUserCommandsIdsToDelete.add(commandId)
                }
            } else {
                globalUserCommandsToAdd.add(user)
            }
        }

        userCommands.forEach { user ->
            if (guildIds.isNotEmpty()) {
                val guildCommands = currentGuildUserCommandsNameAndId.flatMap { it.value.entries }
                if (guildCommands.any { it.value == user.name }) {
                    guildUserCommandsToAdd.add(user)
                } else {
                    guildUserCommandsIdsToDelete.add(
                        guildCommands.first { it.value == user.name }.key)
                }
            }
        }
    }

    private suspend fun processGlobalUserCommands(
        globalUserCommandsToAdd: List<UserCommandBuilder>,
        globalUserCommandsIdsToDelete: List<Long>
    ) {
        processUserCommands(
            globalUserCommandsToAdd,
            ::createGlobalUserCommands,
            globalUserCommandsIdsToDelete,
            ::deleteGlobalUserCommands)
    }

    private suspend fun processGuildUserCommands(
        guildUserCommandsToAdd: List<UserCommandBuilder>,
        guildUserCommandsIdsToDelete: List<Long>
    ) {
        processUserCommands(
            guildUserCommandsToAdd,
            ::createGuildUserCommands,
            guildUserCommandsIdsToDelete,
            ::deleteGuildUserCommands)
    }

    private suspend fun processUserCommands(
        userCommandsToAdd: List<UserCommandBuilder>,
        addCommandFunction: suspend (List<UserCommandBuilder>) -> Unit,
        userCommandsIdsToDelete: List<Long>,
        deleteCommandFunction: suspend (List<Long>) -> Unit
    ) {
        val commandsToAddChunks = userCommandsToAdd.chunked(5)
        val commandsIdsToDeleteChunks = userCommandsIdsToDelete.chunked(5)

        commandsToAddChunks.forEach { chunk ->
            addCommandFunction(chunk)
            delay(25000) // Delay to avoid rate limit
        }

        commandsIdsToDeleteChunks.forEach { chunk ->
            deleteCommandFunction(chunk)
            delay(25000) // Delay to avoid rate limit
        }
    }

    private fun deleteGlobalUserCommands(ids: List<Long>) {
        ids.forEach { id ->
            yde.logger.debug("Deleting global User command with id $id")
            yde.restApiManager
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
                yde.logger.debug("Deleting guild user command $id")
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

    private fun createGlobalUserCommands(userCommands: List<UserCommandBuilder>) {
        userCommands.forEach { user ->
            yde.logger.debug("Sending global User command ${user.name} to Discord")
            yde.restApiManager
                .post(
                    user.toJson().toString().toRequestBody(),
                    EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND,
                    applicationId)
                .executeWithNoResult()
        }
    }

    private fun createGuildUserCommands(userCommands: List<UserCommandBuilder>) {
        userCommands.forEach { user ->
            guildIds.forEach { guildId ->
                yde.logger.debug("Sending User command ${user.name} to guild $guildId")
                yde.restApiManager
                    .post(
                        user.toJson().toString().toRequestBody(),
                        EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND,
                        applicationId,
                        guildId)
                    .executeWithNoResult()
            }
        }
    }
}
