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
package io.github.ydwk.yde.impl.entities.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.message.Embed
import io.github.ydwk.yde.entities.message.embed.*
import io.github.ydwk.yde.impl.entities.message.embed.*
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import java.awt.Color
import java.net.URL

internal class EmbedImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val title: String?,
    override val type: EmbedType?,
    override val description: String?,
    override val url: URL?,
    override val timestamp: String?,
    override val color: Color?,
    override val footer: Footer?,
    override val image: Image?,
    override val thumbnail: Thumbnail?,
    override val video: Video?,
    override val provider: Provider?,
    override val author: Author?,
    override val fields: List<Field>
) : Embed, ToStringEntityImpl<Embed>(yde, Embed::class.java)
