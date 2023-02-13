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
 * https://www.apache.org/licenses/LICENSE-2.0
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
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.impl.interaction.application.ApplicationCommandImpl
import io.github.ydwk.ydwk.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.ydwk.interaction.Interaction
import io.github.ydwk.ydwk.interaction.application.sub.Reply
import io.github.ydwk.ydwk.interaction.application.type.UserCommand

class UserCommandImpl(ydwk: YDWK, json: JsonNode, idAsLong: Long, interaction: Interaction) :
    ApplicationCommandImpl(ydwk, json, idAsLong, interaction), UserCommand {
    override val targetUser: User
        get() =
            UserImpl(
                json["data"]["resolved"]["users"],
                json["data"]["resolved"]["users"]["id"].asLong(),
                ydwk)

    override val targetMember: Member
        get() =
            if (guild != null) {
                MemberImpl(
                    (ydwk as YDWKImpl), json["data"]["resolved"]["members"], guild, targetUser)
            } else {
                throw IllegalStateException("This command was not executed in a guild")
            }

    override fun reply(content: String): Reply {
        return ReplyImpl(ydwk, content, null, interaction.id, token)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(ydwk, null, embed, interaction.id, token)
    }
}
