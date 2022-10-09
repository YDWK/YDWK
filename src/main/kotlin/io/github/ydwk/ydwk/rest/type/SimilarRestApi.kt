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

import io.github.ydwk.ydwk.impl.entities.ChannelImpl
import io.github.ydwk.ydwk.rest.cf.CompletableFutureManager
import java.util.concurrent.CompletableFuture
import okhttp3.CacheControl
import okhttp3.Headers
import okhttp3.Request

interface SimilarRestApi {
    fun header(name: String, value: String)

    fun addHeader(name: String, value: String)

    fun removeHeader(name: String)

    fun headers(headers: Headers)

    fun cacheControl(cacheControl: CacheControl): Request.Builder

    fun execute()

    fun <T : Any> execute(
        function: (t: CompletableFutureManager) -> ChannelImpl
    ): CompletableFuture<T>
}
