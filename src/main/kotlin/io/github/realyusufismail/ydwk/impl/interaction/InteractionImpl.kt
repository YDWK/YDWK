/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.impl.interaction

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.entities.Message
import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.impl.entities.UserImpl
import io.github.realyusufismail.ydwk.impl.entities.guild.MemberImpl
import io.github.realyusufismail.ydwk.impl.interaction.application.ApplicationCommandDataImpl
import io.github.realyusufismail.ydwk.impl.interaction.message.MessageComponentDataImpl
import io.github.realyusufismail.ydwk.interaction.Interaction
import io.github.realyusufismail.ydwk.interaction.sub.GenericCommandData
import io.github.realyusufismail.ydwk.interaction.sub.InteractionType
import io.github.realyusufismail.ydwk.util.GetterSnowFlake

class InteractionImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : Interaction {

    override val applicationId: GetterSnowFlake =
        GetterSnowFlake.of(json["application_id"].asLong())

    override val type: InteractionType = InteractionType.fromInt(json["type"].asInt())

    override val guildId: GetterSnowFlake? =
        if (json.has("guild_id")) GetterSnowFlake.of(json["guild_id"].asLong()) else null

    override val channelId: GetterSnowFlake? =
        if (json.has("channel_id")) GetterSnowFlake.of(json["channel_id"].asLong()) else null

    override val member: Member? =
        if (json.has("member"))
            ydwk.getGuild(guildId!!.asLong)?.let { MemberImpl(ydwk, json["member"], it) }
        else null

    override val user: User? =
        if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), ydwk) else null

    override val token: String = json["token"].asText()

    override val version: Int = json["version"].asInt()

    override val message: Message? = TODO("Not yet implemented")

    override val permissions: Long? =
        if (json.has("permissions")) json["permissions"].asLong() else null

    override val locale: String? = if (json.has("locale")) json["locale"].asText() else null

    override val guildLocale: String? =
        if (json.has("guild_locale")) json["guild_locale"].asText() else null

    override val data: GenericCommandData? =
        when (type) {
            InteractionType.APPLICATION_COMMAND ->
                ApplicationCommandDataImpl(ydwk, json["data"], idAsLong, user, member)
            InteractionType.MESSAGE_COMPONENT -> MessageComponentDataImpl(ydwk, json["data"])
            else -> {
                (ydwk as YDWKImpl).logger.warn("Unknown interaction type: $type")
                null
            }
        }
}