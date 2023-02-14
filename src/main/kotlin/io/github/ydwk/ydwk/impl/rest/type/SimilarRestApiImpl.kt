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
 * https://www.apache.org/licenses/LICENSE-2.0
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
import io.github.ydwk.ydwk.rest.result.NoResult
import io.github.ydwk.ydwk.rest.type.SimilarRestApi
import io.github.ydwk.ydwk.ws.util.formatInstant
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import okhttp3.*
import org.slf4j.LoggerFactory

open class SimilarRestApiImpl(
    private val ydwk: YDWKImpl,
    private val builder: Request.Builder,
    private val client: OkHttpClient,
) : SimilarRestApi {
    private val logger = LoggerFactory.getLogger(SimilarRestApi::class.java)

    override fun header(name: String, value: String): SimilarRestApi {
        builder.header(name, value)
        return this
    }

    override fun addHeader(name: String, value: String): SimilarRestApi {
        builder.addHeader(name, value)
        return this
    }

    override fun removeHeader(name: String): SimilarRestApi {
        builder.removeHeader(name)
        return this
    }

    override fun headers(headers: Headers): SimilarRestApi {
        builder.headers(headers)
        return this
    }

    override fun addReason(reason: String?): SimilarRestApi {
        if (reason != null) {
            addHeader(
                "X-Audit-Log-Reason",
                URLEncoder.encode(reason, StandardCharsets.UTF_8.name()).replace("+", " "))
        }
        return this
    }

    override fun execute() {
        try {
            client.newCall(builder.build()).execute().use { response ->
                if (!response.isSuccessful) {
                    val code = response.code
                    error(response.body, code, null, null)
                } else {
                    responseBody = response.body
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        } finally {
            responseBody?.close()
        }
    }

    override fun <T : Any> execute(
        function: Function<CompletableFutureManager, T>,
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
                                error(response.body, code, null, queue)
                            }
                            responseBody = response.body
                            val manager = CompletableFutureManager(response, ydwk)
                            val result = function.apply(manager)
                            queue.complete(result)
                        }
                    })
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        } finally {
            responseBody?.close()
        }
        return queue
    }

    override fun executeWithNoResult(): CompletableFuture<NoResult> {
        val queue = CompletableFuture<NoResult>()
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
                                error(response.body, code, queue, null)
                            }
                            responseBody = response.body
                            queue.complete(NoResult(formatInstant(Instant.now())))
                        }
                    })
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        } finally {
            responseBody?.close()
        }
        return queue
    }

    fun error(
        body: ResponseBody,
        code: Int,
        queueWithNoResult: CompletableFuture<NoResult>?,
        queueWithResult: CompletableFuture<*>?
    ) {
        if (HttpResponseCode.fromInt(code) == HttpResponseCode.TOO_MANY_REQUESTS) {
            handleRateLimit(body, queueWithNoResult, queueWithResult)
        } else if (HttpResponseCode.fromInt(code) != HttpResponseCode.UNKNOWN) {
            handleHttpResponse(body, code)
        } else if (JsonErrorCode.fromInt(code) != JsonErrorCode.UNKNOWN) {
            handleJsonError(body, code)
        } else {
            logger.error("Unknown error occurred while executing request")
        }
    }

    private fun handleRateLimit(
        body: ResponseBody,
        queueWithNoResult: CompletableFuture<NoResult>?,
        queueWithResult: CompletableFuture<*>?
    ) {
        val jsonNode = ydwk.objectMapper.readTree(body.string())
        val retryAfter = jsonNode.get("retry_after").asLong()
        val global = jsonNode.get("global").asBoolean()
        if (global) {
            logger.debug("Global rate limit reached, retrying in $retryAfter ms")
            try {
                Thread.sleep(retryAfter)
            } catch (e: InterruptedException) {
                logger.error("Error while sleeping", e)
            }
            completeReTry(queueWithNoResult, queueWithResult)
        } else {
            logger.debug("Rate limit reached, retrying in $retryAfter ms")
            try {
                Thread.sleep(retryAfter)
            } catch (e: InterruptedException) {
                logger.error("Error while sleeping", e)
            }
            completeReTry(queueWithNoResult, queueWithResult)
        }
    }

    private fun completeReTry(
        queueWithNoResult: CompletableFuture<NoResult>?,
        queueWithResult: CompletableFuture<*>?
    ) {
        if (queueWithNoResult != null) {
            executeWithNoResult().thenAccept { queueWithNoResult.complete(null) }
            logger.debug(HttpResponseCode.OK.getMessage())
        } else if (queueWithResult != null) {
            execute { queueWithResult.complete(null) }
            logger.debug(HttpResponseCode.OK.getMessage())
        } else {
            execute()
            logger.debug(HttpResponseCode.OK.getMessage())
        }
    }

    private fun handleHttpResponse(body: ResponseBody, code: Int) {
        val error = HttpResponseCode.fromInt(code)
        val codeAndName = error.getCode().toString() + " " + error.name
        var reason = error.getMessage()
        if (body.toString().isNotEmpty())
            reason +=
                " This body contains more detail : " +
                    ydwk.objectMapper.readTree(body.string()).toPrettyString()
        ydwk.logger.error("Error while executing request: $codeAndName $reason")
    }

    private fun handleJsonError(body: ResponseBody, code: Int) {
        val jsonCode = JsonErrorCode.fromInt(code).getCode()
        var jsonMessage = JsonErrorCode.fromInt(code).getMeaning()
        if (body.toString().isNotEmpty())
            jsonMessage +=
                " This body contains more detail : " +
                    ydwk.objectMapper.readTree(body.string()).toPrettyString()

        ydwk.logger.error("Error while executing request: $jsonCode $jsonMessage")
    }

    var responseBody: ResponseBody? = null
}
