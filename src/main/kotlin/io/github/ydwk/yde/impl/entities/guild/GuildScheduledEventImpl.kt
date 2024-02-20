/*
 * Copyright 2024 YDWK inc.
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
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake
import java.time.ZonedDateTime

class GuildScheduledEventImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val guild: Guild,
    override val channel: GuildChannel?,
    override val creator: User?,
    override val description: String?,
    override val scheduledStart: ZonedDateTime,
    override val scheduledEnd: ZonedDateTime?,
    override val privacyLevel: PrivacyLevel,
    override val status: ScheduledEventStatus,
    override val entityType: EntityType,
    override val entityId: GetterSnowFlake?,
    override val entityMetadata: EntityMetadata?,
    override val user: User?,
    override val subscriberCount: Int,
    override val coverImage: String?,
    override var name: String,
) : GuildScheduledEvent {

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(name).toString()
    }
}
