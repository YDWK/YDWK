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
package io.github.ydwk.yde.impl.rest.type

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.rest.type.GetRestApi
import okhttp3.OkHttpClient
import okhttp3.Request

class GetRestApiImpl(
    private val yde: YDEImpl,
    private val client: OkHttpClient,
    private val builder: Request.Builder,
) : GetRestApi, SimilarRestApiImpl(yde, builder, client) {
    override val execute: JsonNode
        get() {
            return try {
                val response = client.newCall(builder.build()).execute()
                responseBody = response.body
                if (response.isSuccessful) {
                    yde.objectMapper.readTree(responseBody!!.string())
                } else {
                    error<Void>(response.body, response.code, null, null)
                    null!!
                }
            } catch (e: Exception) {
                throw RuntimeException("Error while executing request", e)
            } finally {
                responseBody?.close()
            }
        }
}
