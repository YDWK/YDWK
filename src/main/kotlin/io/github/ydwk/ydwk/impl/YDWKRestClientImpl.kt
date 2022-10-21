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
package io.github.ydwk.ydwk.impl

import io.github.ydwk.ydwk.YDWKRestClient
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.rest.RestApiClient
import io.github.ydwk.ydwk.rest.endpoint.EndPoint
import java.util.concurrent.CompletableFuture
import okhttp3.OkHttpClient

class YDWKRestClientImpl(val OkHttpClient: OkHttpClient) : YDWKRestClient, YDWKImpl(OkHttpClient) {
    override fun requestUser(id: Long): CompletableFuture<User> {
        return this.restApiManager.get(EndPoint.UserEndpoint.GET_USER, id.toString()).execute { it
            ->
            val jsonBody = it.jsonBody
            if (jsonBody == null) {
                throw IllegalStateException("json body is null")
            } else {
                UserImpl(jsonBody, jsonBody["id"].asLong(), this)
            }
        }
    }

    override val restApiClient: RestApiClient
        get() = RestApiClient(this)

    /** Used to create a rest client. */
    fun setRestManager(token: String) {}
}
