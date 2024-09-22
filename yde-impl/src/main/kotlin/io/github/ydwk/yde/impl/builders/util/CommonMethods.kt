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
import io.github.ydwk.yde.rest.json
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext

suspend fun getCommandNameAndIds(yde: YDE, applicationId: String): Map<Long, String> {
    return withContext(yde.coroutineDispatcher) {
        yde.restApiManager
            .get(EndPoint.ApplicationCommandsEndpoint.GET_GLOBAL_COMMANDS, applicationId)
            .execute { execute(it, yde) }
            .mapBoth({ it }, { emptyMap() })
    }
}

suspend fun getCurrentGuildCommandsNameAndIds(
    yde: YDE,
    guildIds: List<String>,
    applicationId: String
): Map<String, Map<Long, String>> {
    return withContext(yde.coroutineDispatcher) {
        guildIds.associateWith { guildId ->
            yde.restApiManager
                .get(
                    EndPoint.ApplicationCommandsEndpoint.GET_GUILD_COMMANDS, applicationId, guildId)
                .execute { execute(it, yde) }
                .mapBoth({ it }, { emptyMap() })
        }
    }
}

suspend fun execute(it: HttpResponse, yde: YDE): Map<Long, String> {
    val jsonBody = it.json(yde)
    return jsonBody.associate { it["id"].asLong() to it["name"].asText() } ?: emptyMap()
}
