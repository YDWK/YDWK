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
package io.github.ydwk.yde.impl.rest.type

import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.rest.cf.CompletableFutureManager
import io.github.ydwk.yde.rest.error.HttpResponseCode
import io.github.ydwk.yde.rest.error.JsonErrorCode
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.rest.type.SimilarRestApi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.function.Function
import java.util.logging.Level
import java.util.logging.Logger
import kotlinx.coroutines.CompletableDeferred
import okhttp3.*
import org.slf4j.LoggerFactory

open class SimilarRestApiImpl(
    private val yde: YDEImpl,
    private val builder: Request.Builder,
    private val client: OkHttpClient,
) : SimilarRestApi {
    private val httpLogger: Logger = Logger.getLogger(OkHttpClient::class.simpleName)
    private val logger = LoggerFactory.getLogger(SimilarRestApiImpl::class.java)

    init {
        httpLogger.level = Level.ALL
    }

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
                    error<Void>(response.body, code, null, null)
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
    ): CompletableDeferred<T> {
        val queue = CompletableDeferred<T>()

        try {
            client
                .newCall(builder.build())
                .enqueue(
                    object : Callback {
                        override fun onFailure(call: Call, e: java.io.IOException) {
                            queue.completeExceptionally(e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            try {
                                if (!response.isSuccessful) {
                                    val code = response.code
                                    error(response.body, code, function, null)
                                }
                                responseBody = response.body
                                val manager = CompletableFutureManager(response, yde)
                                val result = function.apply(manager)
                                queue.complete(result)
                            } catch (e: Exception) {
                                queue.completeExceptionally(e)
                            } finally {
                                responseBody?.close() // Close the response body after using it
                            }
                        }
                    })
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        }

        return queue
    }

    override fun executeWithNoResult(): CompletableDeferred<NoResult> {
        val queue = CompletableDeferred<NoResult>()

        try {
            client
                .newCall(builder.build())
                .enqueue(
                    object : Callback {
                        override fun onFailure(call: Call, e: java.io.IOException) {
                            queue.completeExceptionally(e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            try {
                                if (!response.isSuccessful) {
                                    val code = response.code
                                    error<NoResult>(
                                        response.body,
                                        code,
                                        null,
                                        NoResult(Instant.now().toString()))
                                }
                                queue.complete(NoResult(Instant.now().toString()))
                            } catch (e: Exception) {
                                queue.completeExceptionally(e)
                            } finally {
                                responseBody?.close() // Close the response body after using it
                            }
                        }
                    })
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        }

        return queue
    }

    fun <T : Any> error(
        body: ResponseBody,
        code: Int,
        function: Function<CompletableFutureManager, T>?,
        noResult: NoResult?
    ) {
        if (HttpResponseCode.fromInt(code) == HttpResponseCode.TOO_MANY_REQUESTS) {
            handleRateLimit(body, function, noResult)
        } else if (HttpResponseCode.fromInt(code) != HttpResponseCode.UNKNOWN) {
            handleHttpResponse(body, code)
        } else if (JsonErrorCode.fromInt(code) != JsonErrorCode.UNKNOWN) {
            handleJsonError(body, code)
        } else {
            logger.error("Unknown error occurred while executing request")
        }
    }

    private fun <T : Any> handleRateLimit(
        body: ResponseBody,
        function: Function<CompletableFutureManager, T>?,
        noResult: NoResult?
    ) {
        try {
            val jsonNode = yde.objectMapper.readTree(body.string())
            val retryAfter = jsonNode.get("retry_after").asLong()
            val global = jsonNode.get("global").asBoolean()
            val message = jsonNode.get("message").asText()
            logger.error("Error while executing request: $message")
            if (global) {
                logger.error("Global rate limit reached, retrying in $retryAfter ms")
                Thread.sleep(retryAfter)
                completeReTry(function, noResult)
            } else {
                logger.error("Rate limit reached, retrying in $retryAfter ms")
                Thread.sleep(retryAfter)
                completeReTry(function, noResult)
            }
        } catch (e: Exception) {
            logger.error("Error while executing request: ${e.message}")
        } finally {
            responseBody?.close() // Close the response body after using it
        }
    }

    private fun <T : Any> completeReTry(
        function: Function<CompletableFutureManager, T>?,
        noResult: NoResult?
    ) {
        if (function != null) {
            execute(function)
        } else if (noResult != null) {
            executeWithNoResult()
        } else {
            execute()
        }
    }

    private fun handleHttpResponse(body: ResponseBody, code: Int) {
        val error = HttpResponseCode.fromInt(code)
        val codeAndName = error.getCode().toString() + " " + error.name
        var reason = error.getMessage()
        if (body.toString().isNotEmpty())
            reason +=
                " This body contains more detail : " +
                    yde.objectMapper.readTree(body.string()).toPrettyString()
        logger.error("Error while executing request: $codeAndName $reason")
    }

    private fun handleJsonError(body: ResponseBody, code: Int) {
        val jsonCode = JsonErrorCode.fromInt(code).getCode()
        var jsonMessage = JsonErrorCode.fromInt(code).getMeaning()
        if (body.toString().isNotEmpty())
            jsonMessage +=
                " This body contains more detail : " +
                    yde.objectMapper.readTree(body.string()).toPrettyString()

        logger.error("Error while executing request: $jsonCode $jsonMessage")
    }

    var responseBody: ResponseBody? = null
}
