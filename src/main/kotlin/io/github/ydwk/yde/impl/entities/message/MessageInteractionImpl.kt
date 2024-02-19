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
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.message.MessageInteraction
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.EntityToStringBuilder

class MessageInteractionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : MessageInteraction {

    override val type: InteractionType
        get() = InteractionType.fromInt(json.get("type").asInt())

    override val name: String
        get() = json.get("name").asText()

    override val user: User
        get() = UserImpl(json.get("user"), json.get("user").get("id").asLong(), yde)

    override val member: Member?
        get() =
            if (json.has("member"))
                MemberImpl(
                    yde as YDEImpl,
                    json.get("member"),
                    yde.getGuildById(json.get("guild_id").asLong())
                        ?: throw IllegalStateException("Bot is not in guild"))
            else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
