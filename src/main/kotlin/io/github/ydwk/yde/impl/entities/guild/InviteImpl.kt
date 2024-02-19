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
package io.github.ydwk.yde.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.guild.GuildScheduledEvent
import io.github.ydwk.yde.entities.guild.Invite
import io.github.ydwk.yde.entities.guild.invite.TargetType
import io.github.ydwk.yde.impl.entities.ApplicationImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import java.time.ZonedDateTime

class InviteImpl(override val yde: YDE, override val json: JsonNode) : Invite {
    override val code: String
        get() = json["code"].asText()

    override val guild: Guild
        get() =
            yde.getGuildById(json["guild"]["id"].asText())
                ?: throw IllegalStateException("Guild is null")

    override val channel: GuildChannel
        get() =
            yde.getGuildChannelById(json["channel"]["id"].asText())
                ?: throw IllegalStateException("Channel is null")

    override val inviter: User?
        get() =
            if (json["inviter"] != null)
                UserImpl(json["inviter"], json["inviter"]["id"].asLong(), yde)
            else null

    override val targetType: TargetType
        get() = TargetType.fromValue(json["target_type"].asInt())

    override val targetUser: User?
        get() =
            if (json["target_user"] != null)
                UserImpl(json["target_user"], json["target_user"]["id"].asLong(), yde)
            else null

    override val targetApplication: Application?
        get() =
            if (json["target_application"] != null)
                ApplicationImpl(
                    json["target_application"], json["target_application"]["id"].asLong(), yde)
            else null

    override val approximatePresenceCount: Int
        get() = json["approximate_presence_count"].asInt()

    override val approximateMemberCount: Int
        get() = json["approximate_member_count"].asInt()

    override val expirationDate: ZonedDateTime
        get() = ZonedDateTime.parse(json["expires_at"].asText())

    override val guildScheduledEvent: GuildScheduledEvent
        get() =
            GuildScheduledEventImpl(
                yde, json["guild_scheduled_event"], json["guild_scheduled_event"]["id"].asLong())

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
