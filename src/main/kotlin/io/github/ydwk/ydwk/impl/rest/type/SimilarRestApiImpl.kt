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
package io.github.ydwk.ydwk.impl.rest.type

import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.rest.cf.CompletableFutureManager
import io.github.ydwk.ydwk.rest.error.HttpResponseCode
import io.github.ydwk.ydwk.rest.error.JsonErrorCode
import io.github.ydwk.ydwk.rest.type.SimilarRestApi
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import okhttp3.*

open class SimilarRestApiImpl(
    private val ydwk: YDWKImpl,
    private val builder: Request.Builder,
    private val client: OkHttpClient,
) : SimilarRestApi {

    override fun header(name: String, value: String) {
        builder.header(name, value)
    }

    override fun addHeader(name: String, value: String) {
        builder.addHeader(name, value)
    }

    override fun removeHeader(name: String) {
        builder.removeHeader(name)
    }

    override fun headers(headers: Headers) {
        builder.headers(headers)
    }

    override fun cacheControl(cacheControl: CacheControl): Request.Builder {
        return builder.cacheControl(cacheControl)
    }

    override fun execute() {
        try {
            client.newCall(builder.build()).execute().use { response ->
                if (!response.isSuccessful) {
                    val code = response.code
                    error(code)
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        }
    }

    override fun <T : Any> execute(
        function: Function<CompletableFutureManager, T>
    ): CompletableFuture<T> {
        val queue = CompletableFuture<T>()
        try {
            client
                .newCall(builder.build())
                .enqueue(
                    object : Callback {
                        override fun onFailure(call: Call, e: java.io.IOException) {
                            queue.completeExceptionally(e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (!response.isSuccessful) {
                                val code = response.code
                                error(code)
                            }
                            val manager = CompletableFutureManager(response, ydwk)
                            val result = function.apply(manager)
                            queue.complete(result)
                        }
                    })
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        }
        return queue
    }

    override fun executeWithNoResult(): CompletableFuture<Void> {
        val queue = CompletableFuture<Void>()
        try {
            client
                .newCall(builder.build())
                .enqueue(
                    object : Callback {
                        override fun onFailure(call: Call, e: java.io.IOException) {
                            queue.completeExceptionally(e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (!response.isSuccessful) {
                                val code = response.code
                                error(code)
                            }
                            queue.complete(null)
                        }
                    })
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        }
        return queue
    }

    fun error(code: Int) {
        if (HttpResponseCode.fromCode(code) != HttpResponseCode.UNKNOWN) {
            val error = HttpResponseCode.fromCode(code)
            val codeAndName = error.getCode().toString() + " " + error.name
            val reason = error.getMessage()
            ydwk.logger.error("Error while executing request: $codeAndName $reason")
        } else if (JsonErrorCode.fromCode(code) != JsonErrorCode.UNKNOWN) {
            val jsonCode = JsonErrorCode.fromCode(code).getCode
            val jsonMessage = JsonErrorCode.fromCode(code).getMessage

            ydwk.logger.error("Error while executing request: $jsonCode $jsonMessage")
        }
    }

    var responseBody: ResponseBody? = null
}
