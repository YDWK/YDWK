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
package io.github.ydwk.ydwk.impl.interaction

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.impl.interaction.message.ComponentInteractionDataImpl
import io.github.ydwk.ydwk.interaction.ComponentInteraction
import io.github.ydwk.ydwk.interaction.message.Component
import io.github.ydwk.ydwk.interaction.message.ComponentInteractionData
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.GetterSnowFlake

open class ComponentInteractionImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val interactionId: GetterSnowFlake
) : ComponentInteraction {

    override val interactionToken: String
        get() = json["token"].asText()

    override val type: InteractionType
        get() = InteractionType.fromInt(json["type"].asInt())

    override val message: Message
        get() = MessageImpl(ydwk, json["message"], json["message"]["id"].asLong())

    override val guild: Guild?
        get() = if (json.has("guild_id")) ydwk.getGuildById(json["guild_id"].asLong()) else null

    override val member: Member?
        get() =
            if (json.has("member")) MemberImpl((ydwk as YDWKImpl), json["member"], guild!!)
            else null

    override val user: User?
        get() =
            if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), ydwk)
            else null

    override val channel: TextChannel?
        get() =
            if (json.has("channel_id"))
                ydwk.getChannelById(json["channel_id"].asLong()) as TextChannel
            else null

    override val applicationId: GetterSnowFlake?
        get() = TODO("Not yet implemented")

    override val components: List<Component>
        get() = message.components

    override val data: ComponentInteractionData
        get() = ComponentInteractionDataImpl(ydwk, json["data"])

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).toString()
    }
}
