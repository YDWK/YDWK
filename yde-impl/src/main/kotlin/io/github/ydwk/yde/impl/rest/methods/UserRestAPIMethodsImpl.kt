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
package io.github.ydwk.yde.impl.rest.methods

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.json
import io.github.ydwk.yde.rest.methods.UserRestAPIMethods
import io.github.ydwk.yde.rest.toTextContent

class UserRestAPIMethodsImpl(val yde: YDE) : UserRestAPIMethods {
    private val restApiManager = yde.restApiManager

    override suspend fun createDm(id: Long): RestResult<DmChannel> {
        return restApiManager
            .post(
                yde.objectMapper
                    .createObjectNode()
                    .put("recipient_id", id)
                    .toString()
                    .toTextContent(),
                EndPoint.UserEndpoint.CREATE_DM)
            .execute { response ->
                val jsonBody =
                    response.json(yde) ?: throw IllegalStateException("json body is null")
                yde.entityInstanceBuilder.buildDMChannel(jsonBody)
            }
    }

    override suspend fun requestUser(id: Long): RestResult<User> {
        return restApiManager.get(EndPoint.UserEndpoint.GET_USER, id.toString()).execute { response
            ->
            val jsonBody = response.json(yde)
            yde.entityInstanceBuilder.buildUser(jsonBody)
        }
    }

    override suspend fun requestUsers(): RestResult<List<User>> {
        return restApiManager.get(EndPoint.UserEndpoint.GET_USERS).execute { response ->
            val jsonBody = response.json(yde)
            buildUsersFromJson(jsonBody)
        }
    }

    private fun buildUsersFromJson(jsonBody: JsonNode): List<User> {
        val users = mutableListOf<User>()
        for (i in 0 until jsonBody.size()) {
            users.add(yde.entityInstanceBuilder.buildUser(jsonBody[i]))
        }
        return users
    }
}
