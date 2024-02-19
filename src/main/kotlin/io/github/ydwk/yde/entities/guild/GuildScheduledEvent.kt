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
package io.github.ydwk.yde.entities.guild

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.guild.schedule.EntityMetadata
import io.github.ydwk.yde.entities.guild.schedule.EntityType
import io.github.ydwk.yde.entities.guild.schedule.PrivacyLevel
import io.github.ydwk.yde.entities.guild.schedule.ScheduledEventStatus
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake
import java.time.ZonedDateTime

interface GuildScheduledEvent : SnowFlake, GenericEntity, NameAbleEntity {
    /**
     * The guild which the scheduled event belongs to.
     *
     * @return the guild which the scheduled event belongs to.
     */
    val guild: Guild

    /**
     * The channel which the scheduled event belongs to.
     *
     * @return the channel which the scheduled event belongs to.
     */
    val channel: GuildChannel?

    /**
     * The user which created the scheduled event.
     *
     * @return the user which created the scheduled event.
     */
    val creator: User?

    /**
     * The description of the scheduled event.
     *
     * @return the description of the scheduled event.
     */
    val description: String?

    /**
     * The Scheduled start time of the scheduled event.
     *
     * @return the Scheduled start time of the scheduled event.
     */
    val scheduledStart: ZonedDateTime

    /**
     * The Scheduled end time of the scheduled event.
     *
     * @return the Scheduled end time of the scheduled event.
     */
    val scheduledEnd: ZonedDateTime?

    /**
     * The privacy level of the scheduled event.
     *
     * @return the privacy level of the scheduled event.
     */
    val privacyLevel: PrivacyLevel

    /**
     * The status of the scheduled event.
     *
     * @return the status of the scheduled event.
     */
    val status: ScheduledEventStatus

    /**
     * The entity type of the scheduled event.
     *
     * @return the entity type of the scheduled event.
     */
    val entityType: EntityType

    /**
     * The entity id of the scheduled event.
     *
     * @return the entity id of the scheduled event.
     */
    val entityId: GetterSnowFlake?

    /**
     * The entity metadata of the scheduled event.
     *
     * @return the entity metadata of the scheduled event.
     */
    val entityMetadata: EntityMetadata?

    /**
     * The user that created the scheduled event.
     *
     * @return the user that created the scheduled event.
     */
    val user: User?

    /**
     * The number of users subscribed to the scheduled event.
     *
     * @return the number of users subscribed to the scheduled event.
     */
    val subscriberCount: Int

    /**
     * The cover image hash of the scheduled event.
     *
     * @return the cover image hash of the scheduled event.
     */
    val coverImage: String?
}
