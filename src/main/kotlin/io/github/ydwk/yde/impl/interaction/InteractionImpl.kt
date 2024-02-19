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
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.MessageImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl
import io.github.ydwk.yde.impl.interaction.application.type.MessageCommandImpl
import io.github.ydwk.yde.impl.interaction.application.type.SlashCommandImpl
import io.github.ydwk.yde.impl.interaction.application.type.UserCommandImpl
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.sub.GenericCommandData
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

class InteractionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : Interaction {

    override val applicationId: GetterSnowFlake =
        GetterSnowFlake.of(json["application_id"].asLong())

    override val type: InteractionType = InteractionType.fromInt(json["type"].asInt())

    override val guild: Guild? =
        if (json.has("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null

    override val channel: Channel? =
        if (json.has("channel_id")) yde.getChannelById(json["channel_id"].asLong()) else null

    override val member: Member?
        get() {
            if (json.has("member")) {
                if (json.has("member")) {
                    // TODO : Not updating when the bot joins a new guild
                    val member = MemberImpl(yde as YDEImpl, json["member"], guild!!)
                    return yde.memberCache.getOrPut(member)
                }
            }
            return null
        }

    override val user: User
        get() {
            return if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), yde)
            else if (json.has("member"))
                UserImpl(json["member"]["user"], json["member"]["user"]["id"].asLong(), yde)
            else throw IllegalStateException("No user or member found in interaction")
        }

    override val token: String = json["token"].asText()

    override val version: Int = json["version"].asInt()

    override val message: Message? =
        if (json.has("message")) MessageImpl(yde, json["message"], json["message"]["id"].asLong())
        else null

    override val permissions: Long? =
        if (json.has("permissions")) json["permissions"].asLong() else null

    override val locale: String? = if (json.has("locale")) json["locale"].asText() else null

    override val guildLocale: String? =
        if (json.has("guild_locale")) json["guild_locale"].asText() else null

    override val data: GenericCommandData? =
        when (type) {
            InteractionType.APPLICATION_COMMAND -> {
                // get type
                when (ApplicationCommandType.fromInt(json["data"]["type"].asInt())) {
                    ApplicationCommandType.CHAT_INPUT ->
                        SlashCommandImpl(yde, json["data"], idAsLong, this)
                    ApplicationCommandType.USER ->
                        UserCommandImpl(yde, json["data"], idAsLong, this)
                    ApplicationCommandType.MESSAGE ->
                        MessageCommandImpl(yde, json["data"], idAsLong, this)
                    else ->
                        throw IllegalStateException(
                            "Unknown ApplicationCommandType ${json["data"]["type"].asInt()}")
                }
            }
            else -> null
        }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
