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
package io.github.ydwk.yde.rest.type

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.rest.cf.CompletableFutureManager
import io.github.ydwk.yde.rest.result.NoResult
import java.util.function.Function
import kotlinx.coroutines.CompletableDeferred
import okhttp3.Headers

interface SimilarRestApi {
    fun header(name: String, value: String): SimilarRestApi

    fun addHeader(name: String, value: String): SimilarRestApi

    fun removeHeader(name: String): SimilarRestApi

    fun headers(headers: Headers): SimilarRestApi

    fun addReason(reason: String?): SimilarRestApi

    fun execute()

    fun <T : Any> execute(function: Function<CompletableFutureManager, T>): CompletableDeferred<T>

    fun executeWithNoResult(): CompletableDeferred<NoResult>
}

fun <T> handleApiResponse(
    response: CompletableFutureManager,
    entityBuilder: (JsonNode) -> T,
    additionalAction: (T) -> Unit = {}
): T {
    val jsonBody = response.jsonBody ?: throw IllegalStateException("Response body is null")
    val entity = entityBuilder(jsonBody)
    additionalAction(entity)
    return entity
}