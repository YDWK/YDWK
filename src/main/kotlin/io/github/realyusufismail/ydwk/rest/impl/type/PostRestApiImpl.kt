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
package io.github.realyusufismail.ydwk.rest.impl.type

import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.rest.execute.ExecuteRest
import io.github.realyusufismail.ydwk.rest.impl.execute.ExecuteRestImpl
import io.github.realyusufismail.ydwk.rest.type.PostRestApi
import okhttp3.OkHttpClient
import okhttp3.Request

class PostRestApiImpl(
    private val ydwk: YDWKImpl,
    private val client: OkHttpClient,
    private val builder: Request.Builder
) : PostRestApi, SimilarRestApiImpl(builder) {
    override val execute: ExecuteRest
        get() {
            client.newCall(builder.build()).execute().use { response ->
                return ExecuteRestImpl(ydwk, response)
            }
        }
}
