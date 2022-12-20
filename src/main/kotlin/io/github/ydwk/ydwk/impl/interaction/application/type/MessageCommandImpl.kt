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
package io.github.ydwk.ydwk.impl.interaction.application.type

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.impl.interaction.application.ApplicationCommandImpl
import io.github.ydwk.ydwk.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.ydwk.interaction.Interaction
import io.github.ydwk.ydwk.interaction.application.sub.Reply
import io.github.ydwk.ydwk.interaction.application.type.MessageCommand

class MessageCommandImpl(ydwk: YDWK, json: JsonNode, idAsLong: Long, interaction: Interaction) :
    ApplicationCommandImpl(ydwk, json, idAsLong, interaction), MessageCommand {
    override val targetMessage: Message
        get() =
            MessageImpl(
                ydwk,
                json["data"]["resolved"]["messages"],
                json["data"]["resolved"]["messages"]["id"].asLong())

    override fun reply(content: String): Reply {
        return ReplyImpl(ydwk, content, null, interaction.id, token)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(ydwk, null, embed, interaction.id, token)
    }
}
