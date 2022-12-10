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
package io.github.ydwk.ydwk.impl.interaction.message.button

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.impl.interaction.message.ComponentImpl
import io.github.ydwk.ydwk.interaction.message.button.Button
import java.net.URL

open class ButtonImpl(ydwk: YDWK, json: JsonNode, idAsLong: Long) :
    Button, ComponentImpl(ydwk, json, idAsLong) {
    override val message: Message
        get() = MessageImpl(ydwk, json["message"], json["message"]["id"].asLong())

    override val customId: String?
        get() = if (json.has("data")) json["data"]["custom_id"].asText() else null

    override val url: URL?
        get() = if (json.has("data")) URL(json["data"]["url"].asText()) else null

    override val member: Member
        get() = TODO("Not yet implemented")
}
