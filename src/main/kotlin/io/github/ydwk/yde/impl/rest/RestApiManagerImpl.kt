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
package io.github.ydwk.yde.impl.rest

import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.rest.type.*
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.RestApiManager
import io.github.ydwk.yde.rest.type.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class RestApiManagerImpl(
    private val token: String,
    private val yde: YDEImpl,
    private val client: HttpClient
) : RestApiManager {
    private val addQueryParameterMap: MutableMap<String, String> = mutableMapOf()

    override fun addQueryParameter(key: String, value: String): RestApiManager {
        addQueryParameterMap[key] = value
        return this
    }

    override suspend fun get(endPoint: EndPoint.IEnumEndpoint, vararg params: String): HttpResponse {
        val builder = requestBuilder(endPoint, *params)
        return client.get(builder = builder)
    }

    override fun post(
        body: RequestBody?,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String,
    ): PostRestApi {
        val builder =
            requestBuilder(endPoint, *params).post(body ?: ByteArray(0).toRequestBody(null, 0, 0))
        return PostRestApiImpl(yde, client, builder)
    }

    override fun put(
        body: RequestBody?,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String,
    ): PutRestApi {
        val builder =
            requestBuilder(endPoint, *params).put(body ?: ByteArray(0).toRequestBody(null, 0, 0))
        return PutRestApiImpl(yde, client, builder)
    }

    override fun delete(
        body: RequestBody?,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String,
    ): DeleteRestApi {
        val builder = requestBuilder(endPoint, *params).delete(body)
        return DeleteRestApiImpl(yde, client, builder)
    }

    override fun patch(
        body: RequestBody,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String,
    ): PatchRestApi {
        val builder = requestBuilder(endPoint, *params).patch(body)
        return PatchRestApiImpl(yde, client, builder)
    }

    private fun requestBuilder(
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String,
    ): HttpRequestBuilder {
        val builder = HttpRequestBuilder()

        builder.headers{
            requiredHeaders()
        }

        builder.url(getEndpoint(endPoint, *params))

        if (addQueryParameterMap.isNotEmpty()) {
            addQueryParameterMap.forEach() { (key, value) ->
                builder.url.parameters.append(key, value)
            }
        }


        return builder
    }

    private fun getEndpoint(endPoint: EndPoint.IEnumEndpoint, vararg params: String): String {
        return when {
            params.isEmpty() -> {
                if (endPoint.containsParam()) {
                    throw IllegalArgumentException(
                        "Endpoint ${endPoint.getEndpoint()} requires parameters")
                } else {
                    endPoint.getFullEndpoint()
                }
            }
            else -> {
                endPoint.getFullEndpointWithParams(*params)
            }
        }
    }

    private fun HeadersBuilder.requiredHeaders() {
        this.append("Content-Type", "application/json")
        this.append("Authorization", "Bot $token")
        this.append(
            "user-agent",
            "DiscordBot (" + yde.githubRepositoryUrl + ", " + yde.wrapperVersion + ")")
        this.append("accept-encoding", "json")
    }
}
