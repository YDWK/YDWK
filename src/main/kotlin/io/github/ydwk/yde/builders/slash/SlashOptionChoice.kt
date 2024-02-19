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
package io.github.ydwk.yde.builders.slash

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class SlashOptionChoice(val name: String, val value: String) {
    constructor(name: String, value: Int) : this(name, value.toString())
    constructor(name: String, value: Long) : this(name, value.toString())
    constructor(name: String, value: Double) : this(name, value.toString())

    fun toJson(): JsonNode {
        val json = ObjectMapper().createObjectNode()
        json.put("name", name)
        json.put("value", value)
        return json
    }
}
