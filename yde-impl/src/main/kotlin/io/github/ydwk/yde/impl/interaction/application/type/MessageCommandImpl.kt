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
package io.github.ydwk.yde.impl.interaction.application.type

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.message.Embed
import io.github.ydwk.yde.impl.interaction.application.ApplicationCommandImpl
import io.github.ydwk.yde.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.application.sub.Reply
import io.github.ydwk.yde.interaction.application.type.MessageCommand

class MessageCommandImpl(
    yde: YDE,
    json: JsonNode,
    interaction: Interaction,
    override val targetMessage: Message
) :
    ApplicationCommandImpl(
        yde.entityInstanceBuilder.buildApplicationCommand(
            json, ApplicationCommandType.MESSAGE, interaction),
        interaction),
    MessageCommand {
    override fun reply(content: String): Reply {
        return ReplyImpl(yde, content, null, interaction.id, token)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(yde, null, embed, interaction.id, token)
    }
}
