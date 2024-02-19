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
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.guild.GuildScheduledEvent
import io.github.ydwk.yde.entities.guild.schedule.EntityMetadata
import io.github.ydwk.yde.entities.guild.schedule.EntityType
import io.github.ydwk.yde.entities.guild.schedule.PrivacyLevel
import io.github.ydwk.yde.entities.guild.schedule.ScheduledEventStatus
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.guild.schedule.EntityMetadataImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake
import java.time.ZonedDateTime

class GuildScheduledEventImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : GuildScheduledEvent {
    override val guild: Guild
        get() = yde.getGuildById(json["guild_id"].asText())!!

    override val channel: GuildChannel?
        get() =
            if (json.has("channel_id")) yde.getGuildChannelById(json["channel_id"].asText())
            else null

    override val creator: User?
        get() = if (json.has("creator")) yde.getUserById(json["creator"]["id"].asText()) else null

    override val description: String?
        get() = if (json.has("description")) json["description"].asText() else null

    override val scheduledStart: ZonedDateTime
        get() = ZonedDateTime.parse(json["scheduled_start_time"].asText())

    override val scheduledEnd: ZonedDateTime?
        get() =
            if (json.has("scheduled_end_time"))
                ZonedDateTime.parse(json["scheduled_end_time"].asText())
            else null

    override val privacyLevel: PrivacyLevel
        get() = PrivacyLevel.getValue(json["privacy_level"].asInt())

    override val status: ScheduledEventStatus
        get() = ScheduledEventStatus.getValue(json["status"].asInt())

    override val entityType: EntityType
        get() = EntityType.getValue(json["entity_type"].asInt())

    override val entityId: GetterSnowFlake?
        get() = if (json.has("entity_id")) GetterSnowFlake.of(json["entity_id"].asLong()) else null

    override val entityMetadata: EntityMetadata?
        get() =
            if (json.hasNonNull("entity_metadata")) EntityMetadataImpl(yde, json["entity_metadata"])
            else null

    override val user: User?
        get() =
            if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), yde) else null

    override val subscriberCount: Int
        get() = json["subscriber_count"].asInt()

    override val coverImage: String?
        get() = if (json.has("cover_image")) json["cover_image"].asText() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(name).toString()
    }

    override var name: String
        get() = json["name"].asText()
        set(value) {}
}
