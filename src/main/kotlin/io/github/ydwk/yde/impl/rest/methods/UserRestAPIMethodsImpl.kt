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
package io.github.ydwk.yde.impl.rest.methods

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.channel.DmChannelImpl
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.methods.UserRestAPIMethods
import kotlinx.coroutines.CompletableDeferred
import okhttp3.RequestBody.Companion.toRequestBody

class UserRestAPIMethodsImpl(val yde: YDE) : UserRestAPIMethods {
    val restApiManager = yde.restApiManager

    override fun createDm(id: Long): CompletableDeferred<DmChannel> {
        return restApiManager
            .post(
                yde.objectMapper
                    .createObjectNode()
                    .put("recipient_id", id)
                    .toString()
                    .toRequestBody(),
                EndPoint.UserEndpoint.CREATE_DM)
            .execute() {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    DmChannelImpl(yde, jsonBody, jsonBody["id"].asLong())
                }
            }
    }

    override fun requestUser(id: Long): CompletableDeferred<User> {
        return this.restApiManager.get(EndPoint.UserEndpoint.GET_USER, id.toString()).execute() {
            val jsonBody = it.jsonBody
            if (jsonBody == null) {
                throw IllegalStateException("json body is null")
            } else {
                UserImpl(jsonBody, jsonBody["id"].asLong(), yde)
            }
        }
    }

    override fun requestUsers(): CompletableDeferred<List<User>> {
        return this.restApiManager.get(EndPoint.UserEndpoint.GET_USERS).execute { it ->
            val jsonBody = it.jsonBody
            jsonBody?.map { UserImpl(it, it["id"].asLong(), yde) }
                ?: throw IllegalStateException("json body is null")
        }
    }
}
