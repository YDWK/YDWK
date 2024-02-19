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
package io.github.ydwk.yde.rest.cf

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import io.github.ydwk.yde.impl.YDEImpl
import okhttp3.Response
import okhttp3.ResponseBody

class CompletableFutureManager(var response: Response, val yde: YDEImpl) {
    var jsonBody: JsonNode? = null
    private var stringBody: String? = null

    init {
        val body = response.body
        if (body.isNullOrEmpty()) {
            stringBody = null
            jsonBody = NullNode.instance
        } else {
            stringBody = body.string()
            val objectMapper = yde.objectMapper
            jsonBody =
                try {
                    objectMapper.readTree(stringBody)
                } catch (e: JsonParseException) {
                    throw RuntimeException("Error while parsing json", e)
                    null
                }

            jsonBody = if (jsonBody == null) NullNode.instance else jsonBody
        }
    }
}

private fun ResponseBody.isNullOrEmpty(): Boolean {
    return this.contentLength() == 0L
}
