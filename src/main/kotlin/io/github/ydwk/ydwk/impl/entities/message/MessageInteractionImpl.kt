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
package io.github.ydwk.ydwk.impl.entities.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.MessageInteraction
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.util.EntityToStringBuilder

class MessageInteractionImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : MessageInteraction {

    override val type: InteractionType
        get() = InteractionType.fromInt(json.get("type").asInt())

    override val name: String
        get() = json.get("name").asText()

    override val user: User
        get() = UserImpl(json.get("user"), json.get("user").get("id").asLong(), ydwk)

    override val member: Member?
        get() =
            if (json.has("member"))
                MemberImpl(
                    ydwk as YDWKImpl,
                    json.get("member"),
                    ydwk.getGuildById(json.get("guild_id").asLong())
                        ?: throw IllegalStateException("Bot is not in guild"))
            else null

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).name(this.name).toString()
    }
}
