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
import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.impl.interaction.application.type.SlashCommandImpl
import io.github.ydwk.ydwk.interaction.Interaction
import io.github.ydwk.ydwk.interaction.sub.GenericCommandData
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.GetterSnowFlake

class InteractionImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : Interaction {

    override val applicationId: GetterSnowFlake =
        GetterSnowFlake.of(json["application_id"].asLong())

    override val type: InteractionType = InteractionType.fromInt(json["type"].asInt())

    override val guild: Guild? =
        if (json.has("guild_id")) ydwk.getGuildById(json["guild_id"].asLong()) else null

    override val channel: Channel? =
        if (json.has("channel_id")) ydwk.getChannelById(json["channel_id"].asLong()) else null

    override val member: Member?
        get() {
            if (json.has("member")) {
                if (json.has("member")) {
                    // TODO : Not updating when the bot joins a new guild
                    val member = MemberImpl(ydwk as YDWKImpl, json["member"], guild!!)
                    return ydwk.memberCache.getOrPut(member)
                }
            }
            return null
        }

    override val user: User? =
        if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), ydwk) else null

    override val token: String = json["token"].asText()

    override val version: Int = json["version"].asInt()

    override val message: Message? =
        if (json.has("message")) MessageImpl(ydwk, json["message"], json["message"]["id"].asLong())
        else null

    override val permissions: Long? =
        if (json.has("permissions")) json["permissions"].asLong() else null

    override val locale: String? = if (json.has("locale")) json["locale"].asText() else null

    override val guildLocale: String? =
        if (json.has("guild_locale")) json["guild_locale"].asText() else null

    override val data: GenericCommandData? =
        when (type) {
            InteractionType.APPLICATION_COMMAND ->
                SlashCommandImpl(ydwk, json["data"], idAsLong, this)
            else -> {
                (ydwk as YDWKImpl).logger.warn("Unknown interaction type: $type")
                null
            }
        }

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).toString()
    }
}
