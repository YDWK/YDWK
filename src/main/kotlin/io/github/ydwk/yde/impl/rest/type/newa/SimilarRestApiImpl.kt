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
package io.github.ydwk.yde.impl.rest.type.newa

import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.rest.error.HttpResponseCode
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.rest.type.RestResult
import io.github.ydwk.yde.rest.type.SimilarRestApiNew
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

open class SimilarRestApiImpl(
    private val yde: YDEImpl,
    private val builder: HttpRequestBuilder,
    private val client: HttpClient,
) : SimilarRestApiNew {
    private val logger = LoggerFactory.getLogger(SimilarRestApiImpl::class.java)

    override fun header(name: String, value: String): SimilarRestApiNew {
        builder.headers[name] = value
        return this
    }

    override fun addHeader(name: String, value: String): SimilarRestApiNew {
        builder.headers.append(name, value)
        return this
    }

    override fun removeHeader(name: String): SimilarRestApiNew {
        builder.headers.remove(name)
        return this
    }

    override fun headers(headers: Headers): SimilarRestApiNew {
        builder.headers.appendAll(headers)
        return this
    }

    override fun addReason(reason: String?): SimilarRestApiNew {
        if (reason != null) {
            addHeader(
                "X-Audit-Log-Reason",
                URLEncoder.encode(reason, StandardCharsets.UTF_8.name()).replace("+", " "))
        }
        return this
    }

    override suspend fun execute(): RestResult<HttpResponse> {
        return try {
            val response = client.request(builder = builder)
            RestResult.Success(response)
        } catch (e: Exception) {
            logger.error("Error while executing request", e)
            RestResult.Error("Error while executing request: ${e.message}")
        }
    }

    override suspend fun <T : Any> execute(function: suspend (HttpResponse) -> T): RestResult<T> {
        return try {
            val response = client.request(builder = builder)

            checkRateLimit(response).let {
                if (it) {
                    handleRateLimitForResult(response, function)
                } else {
                    RestResult.Success(function(response))
                }
            }
        } catch (e: Exception) {
            logger.error("Error while executing request", e)
            RestResult.Error("Error while executing request: ${e.message}")
        }
    }

    override suspend fun executeWithNoResult(): RestResult<NoResult> {
        return try {
            val response = client.request(builder = builder)

            checkRateLimit(response).let {
                if (it) {
                    handleRateLimitForNoResult(response)
                } else {
                    RestResult.Success(NoResult(Instant.now().toString()))
                }
            }
        } catch (e: Exception) {
            logger.error("Error while executing request", e)
            RestResult.Error("Error while executing request: ${e.message}")
        }
    }

    private fun checkRateLimit(response: HttpResponse): Boolean {
        return response.status.value == HttpResponseCode.TOO_MANY_REQUESTS.getCode()
    }

    private suspend fun handleRateLimitForNoResult(response: HttpResponse): RestResult<NoResult> {
        val retryAfter = response.headers["Retry-After"]?.toIntOrNull() ?: 0
        delay(retryAfter.toLong())
        return executeWithNoResult()
    }

    private suspend fun <T> handleRateLimitForResult(
        response: HttpResponse,
        function: suspend (HttpResponse) -> T
    ): RestResult<T> {
        val retryAfter = response.headers["Retry-After"]?.toIntOrNull() ?: 0
        delay(retryAfter.toLong())
        return try {
            val result = function(response)
            RestResult.Success(result)
        } catch (e: Exception) {
            logger.error("Error while executing request after rate limit", e)
            RestResult.Error("Error while executing request after rate limit: ${e.message}")
        }
    }
}
