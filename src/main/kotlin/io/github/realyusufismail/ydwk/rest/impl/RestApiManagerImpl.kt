/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.rest.impl

import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.rest.EndPoint
import io.github.realyusufismail.ydwk.rest.RestApiManager
import io.github.realyusufismail.ydwk.rest.impl.type.*
import io.github.realyusufismail.ydwk.rest.type.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class RestApiManagerImpl(private val ydwkImpl: YDWKImpl, private val client: OkHttpClient) :
    RestApiManager {
    override fun get(endPoint: EndPoint.IEnumEndpoint, vararg params: String): GetRestApi {
        val builder = Request.Builder().url(getEndpoint(endPoint, *params)).get()

        return GetRestApiImpl(ydwkImpl, client, builder)
    }

    override fun post(
        body: RequestBody,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String
    ): PostRestApi {
        val builder = Request.Builder().url(getEndpoint(endPoint, *params)).post(body)
        return PostRestApiImpl(ydwkImpl, client, builder)
    }

    override fun put(
        body: RequestBody,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String
    ): PutRestApi {
        val builder = Request.Builder().url(getEndpoint(endPoint, *params)).put(body)
        return PutRestApiImpl(ydwkImpl, client, builder)
    }

    override fun delete(
        body: RequestBody?,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String
    ): DeleteRestApi {
        val builder = Request.Builder().url(getEndpoint(endPoint, *params)).delete(body)
        return DeleteRestApiImpl(ydwkImpl, client, builder)
    }

    override fun patch(
        body: RequestBody,
        endPoint: EndPoint.IEnumEndpoint,
        vararg params: String
    ): PatchRestApi {
        val builder = Request.Builder().url(getEndpoint(endPoint, *params)).patch(body)
        return PatchRestApiImpl(ydwkImpl, client, builder)
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
}
