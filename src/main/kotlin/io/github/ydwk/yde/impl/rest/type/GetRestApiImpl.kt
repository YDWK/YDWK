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
package io.github.ydwk.yde.impl.rest.type

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.rest.type.newa.SimilarRestApiImpl
import io.github.ydwk.yde.rest.type.GetRestApi
import io.ktor.client.*
import io.ktor.client.request.*
import okhttp3.OkHttpClient
import okhttp3.Request

class GetRestApiImpl(
    yde: YDEImpl,
    client: HttpClient,
    builder: HttpRequestBuilder,
) : GetRestApi, SimilarRestApiImpl(yde, builder, client)