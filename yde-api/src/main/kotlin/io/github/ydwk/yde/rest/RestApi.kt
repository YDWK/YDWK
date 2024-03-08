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
package io.github.ydwk.yde.rest

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.rest.error.RestAPIException
import io.github.ydwk.yde.rest.result.NoResult
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*

/** Interface for a REST API with similar functionality. */
interface SimilarRestApi {
    /** Sets a header for the request. */
    fun header(name: String, value: String): SimilarRestApi

    /** Adds a header to the request. */
    fun addHeader(name: String, value: String): SimilarRestApi

    /** Removes a header from the request. */
    fun removeHeader(name: String): SimilarRestApi

    /** Sets multiple headers for the request. */
    fun headers(headers: Headers): SimilarRestApi

    /** Adds a reason to the request. */
    fun addReason(reason: String?): SimilarRestApi

    /** Executes the request and returns the response. */
    suspend fun execute(): RestResult<HttpResponse>

    /** Executes the request and applies a function to the response. */
    suspend fun <T : Any> execute(function: suspend (HttpResponse) -> T): RestResult<T>

    /** Executes the request and returns a result indicating success or failure. */
    suspend fun executeWithNoResult(): RestResult<NoResult>
}

/** Sealed class representing the result of a REST request. */
sealed class RestResult<out T> {
    /** Represents a successful result with data. */
    data class Success<T>(val data: T) : RestResult<T>()

    /** Represents an error result with a message. */
    data class Error(val error: RestAPIException) : RestResult<Nothing>()

    /** Transforms the result data if it's a success. */
    fun <R> map(transform: (T) -> R): RestResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
        }
    }

    /** Returns the result data if it's a success, or null otherwise. */
    fun getOrNull(): T? {
        return when (this) {
            is Success -> data
            is Error -> null
        }
    }

    /**
     * Applies a function to the result data if it's a success, or to the error message if it's an
     * error.
     */
    fun <R> mapBoth(onSuccess: (T) -> R, onError: (RestAPIException) -> R): R {
        return when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(error)
        }
    }
}

/** Extension function to parse the response body as JSON. */
suspend fun HttpResponse.json(yde: YDE): JsonNode {
    if (isNullOrEmpty()) {
        return yde.objectMapper.createObjectNode()
    }

    return yde.objectMapper.readTree(body<String>())
}

private fun HttpResponse.isNullOrEmpty(): Boolean {
    return this.contentLength() == 0L
}

/** Extension function to convert a string to a [TextContent]. */
fun String.toTextContent(): TextContent {
    return TextContent(this, ContentType.Application.Json)
}
