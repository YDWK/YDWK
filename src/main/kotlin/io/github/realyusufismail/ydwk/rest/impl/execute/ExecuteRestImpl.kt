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
package io.github.realyusufismail.ydwk.rest.impl.execute

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.rest.execute.ExecuteRest
import okhttp3.ResponseBody

class ExecuteRestImpl(private val ydwk: YDWKImpl, private val response: ResponseBody) :
    ExecuteRest {

    override val json: JsonNode
        get() {
            println(response.string())
            return ydwk.objectMapper.readTree(response.string())
        }

    override val jsonAsString: String
        get() = json.toString()

    override val jsonAsPrettyString: String
        get() = json.toPrettyString()

    override val jsonAsBytes: ByteArray
        get() = jsonAsString.toByteArray()

    override val jsonAsArray: Array<String>
        get() = json.map { it.asText() }.toTypedArray()

    override val jsonAsList: List<String>
        get() = json.map { it.asText() }.toList()
}
