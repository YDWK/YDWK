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
package io.github.ydwk.ydwk.impl.entities.message.embed

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.message.embed.Provider
import io.github.ydwk.ydwk.util.EntityToStringBuilder

class ProviderImpl(override val ydwk: YDWK, override val json: JsonNode) : Provider {

    override val name: String?
        get() = if (json.has("name")) json["name"].asText() else null

    override val url: String?
        get() = if (json.has("url")) json["url"].asText() else null

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).name(this.name ?: "null").toString()
    }
}
