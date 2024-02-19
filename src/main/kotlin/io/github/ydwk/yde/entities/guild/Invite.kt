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

import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.guild.invite.TargetType
import io.github.ydwk.yde.entities.util.GenericEntity
import java.time.ZonedDateTime

interface Invite : GenericEntity {

    /**
     * The invite code (unique ID).
     *
     * @return the invite code.
     */
    val code: String

    /**
     * The guild this invite is for.
     *
     * @return the guild.
     */
    val guild: Guild

    /**
     * The channel this invite is for.
     *
     * @return the channel.
     */
    val channel: GuildChannel

    /**
     * The inviter of the invite.
     *
     * @return the inviter.
     */
    val inviter: User?

    /**
     * The target type of the invite.
     *
     * @return the target type.
     */
    val targetType: TargetType

    /**
     * The target user of the invite.
     *
     * @return the target user.
     */
    val targetUser: User?

    /**
     * The target application of the invite.
     *
     * @return the target application.
     */
    val targetApplication: Application?

    /**
     * The approximate presence count of the invite.
     *
     * @return the approximate presence count.
     */
    val approximatePresenceCount: Int

    /**
     * The approximate number of members in the guild.
     *
     * @return the approximate number of members.
     */
    val approximateMemberCount: Int

    /**
     * The expiration date of the invite.
     *
     * @return the expiration date.
     */
    val expirationDate: ZonedDateTime

    /**
     * The guild scheduled event this invite is for.
     *
     * @return the guild scheduled event.
     */
    val guildScheduledEvent: GuildScheduledEvent
}
