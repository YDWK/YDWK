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
package io.github.ydwk.yde.impl.entities.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.message.Attachment
import io.github.ydwk.yde.util.EntityToStringBuilder
import java.net.URL

class AttachmentImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : Attachment {
    override val description: String?
        get() = if (json.has("description")) json.get("description").asText() else null

    override val mediaType: String?
        get() = if (json.has("media_type")) json.get("media_type").asText() else null

    override val url: URL
        get() = URL(json.get("url").asText())

    override val proxyUrl: URL
        get() = URL(json.get("proxy_url").asText())

    override val size: Int
        get() = json.get("size").asInt()

    override val height: Int?
        get() = if (json.has("height")) json.get("height").asInt() else null

    override val width: Int?
        get() = if (json.has("width")) json.get("width").asInt() else null

    override val ephemeral: Boolean
        get() = json.get("ephemeral").asBoolean()

    override var name: String = json.get("name").asText()

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
