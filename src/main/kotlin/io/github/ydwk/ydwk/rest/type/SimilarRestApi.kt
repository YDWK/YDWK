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
package io.github.ydwk.ydwk.rest.type

import io.github.ydwk.ydwk.rest.cf.CompletableFutureManager
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import okhttp3.Headers

interface SimilarRestApi {
    fun header(name: String, value: String): SimilarRestApi

    fun addHeader(name: String, value: String): SimilarRestApi

    fun removeHeader(name: String): SimilarRestApi

    fun headers(headers: Headers): SimilarRestApi

    fun addReason(reason: String?): SimilarRestApi

    fun addQueryParameter(key: String, value: String): SimilarRestApi

    fun execute()

    fun <T : Any> execute(function: Function<CompletableFutureManager, T>): CompletableFuture<T>

    fun executeWithNoResult(): CompletableFuture<Void>
}
