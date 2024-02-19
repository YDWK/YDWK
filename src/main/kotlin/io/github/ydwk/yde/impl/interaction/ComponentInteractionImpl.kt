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
package io.github.ydwk.yde.impl.interaction

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.TextChannel
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.MessageImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl
import io.github.ydwk.yde.impl.interaction.message.ComponentInteractionDataImpl
import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.interaction.message.ComponentInteractionData
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

open class ComponentInteractionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val interactionId: GetterSnowFlake,
) : ComponentInteraction {
    override val type: ComponentType
        get() = ComponentType.fromInt(json["component_type"].asInt())

    override val interactionToken: String
        get() = json["token"].asText()

    override val message: Message
        get() = MessageImpl(yde, json["message"], json["message"]["id"].asLong())

    override val guild: Guild?
        get() = if (json.has("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null

    override val member: Member?
        get() =
            if (json.has("member")) MemberImpl((yde as YDEImpl), json["member"], guild!!) else null

    override val user: User?
        get() =
            if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), yde) else null

    override val channel: TextChannel?
        get() =
            if (json.has("channel_id"))
                yde.getChannelById(json["channel_id"].asLong()) as TextChannel
            else null

    override val applicationId: GetterSnowFlake?
        get() =
            if (json.has("application_id")) GetterSnowFlake.of(json["application_id"].asLong())
            else null

    override val components: List<Component>
        get() = message.components

    override val data: ComponentInteractionData
        get() = ComponentInteractionDataImpl(yde, json["data"])

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
