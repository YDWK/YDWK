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
package io.github.ydwk.yde.impl.entities.message.embed

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.message.embed.Author
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import java.net.URL

internal class AuthorImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val name: String,
    override val url: URL?,
    override val iconUrl: String?,
    override val proxyIconUrl: String?
) : Author, ToStringEntityImpl<Author>(yde, Author::class.java)
