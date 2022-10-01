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
package io.github.realyusufismail.ydwk.impl.rest.type

import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.rest.error.HttpResponseCode
import io.github.realyusufismail.ydwk.rest.type.SimilarRestApi
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
                    if (HttpResponseCode.fromCode(code) != HttpResponseCode.UNKNOWN) {
                        val error = HttpResponseCode.fromCode(code)
                        val codeAndName = error.getCode().toString() + " " + error.name
                        val reason = error.getMessage()
                        ydwk.logger.error("Error while executing request: $codeAndName $reason")
                    }
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error while executing request", e)
        }
    }

    var responseBody: ResponseBody? = null
}
