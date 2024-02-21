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
package io.github.ydwk.yde.impl.builders.util

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.cf.CompletableFutureManager
import io.github.ydwk.yde.util.LOOM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Rework this to be more efficient and clean
suspend fun getCommandNameAndIds(yde: YDE, applicationId: String): Map<Long, String> {
    return withContext(Dispatchers.LOOM) {
        yde.restApiManager
            .get(EndPoint.ApplicationCommandsEndpoint.GET_GLOBAL_COMMANDS, applicationId)
            .execute { execute(it) }
            .await()
    }
}

suspend fun getCurrentGuildCommandsNameAndIds(
    yde: YDE,
    guildIds: List<String>,
    applicationId: String
): Map<String, Map<Long, String>> {
    return withContext(Dispatchers.LOOM) {
        guildIds.associateWith { guildId ->
            yde.restApiManager
                .get(
                    EndPoint.ApplicationCommandsEndpoint.GET_GUILD_COMMANDS, applicationId, guildId)
                .execute { execute(it) }
                .await()
        }
    }
}

fun execute(it: CompletableFutureManager): Map<Long, String> {
    val jsonBody = it.jsonBody
    if (jsonBody == null) {
        return@execute emptyMap()
    } else {
        return@execute jsonBody.associate { it["id"].asLong() to it["name"].asText() }
    }
}
