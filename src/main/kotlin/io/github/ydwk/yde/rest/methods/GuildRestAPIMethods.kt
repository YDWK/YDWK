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
package io.github.ydwk.yde.rest.methods

import io.github.ydwk.yde.entities.AuditLog
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.entities.guild.Ban
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.GetterSnowFlake
import kotlin.time.Duration
import kotlinx.coroutines.CompletableDeferred

interface GuildRestAPIMethods {

    //////////////////// Moderation Actions ////////////////////////
    /**
     * Bans a user from the guild.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [CompletableDeferred<NoResult>] that will ban the user.
     */
    fun banUser(
        guildId: Long,
        userId: Long,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null,
    ): CompletableDeferred<NoResult>

    /**
     * Unbans a user from the guild.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @param reason The reason for the unban.
     * @return A [CompletableDeferred<NoResult>] that will unban the user.
     */
    fun unbanUser(
        guildId: Long,
        userId: Long,
        reason: String? = null,
    ): CompletableDeferred<NoResult>

    /**
     * Kicks a member from the guild.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @param reason The reason for the kick.
     * @return A [CompletableDeferred<NoResult>] that will kick the user.
     */
    fun kickMember(
        guildId: Long,
        userId: Long,
        reason: String? = null
    ): CompletableDeferred<NoResult>

    /**
     * Requests the ban list for the guild.
     *
     * @param guildId The id of the guild.
     * @return A [CompletableDeferred] that will request the ban list.
     */
    fun requestedBanList(guildId: Long): CompletableDeferred<List<Ban>>

    //////////////// Guild Request Methods ///////////////////////

    /**
     * Request the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @param actionType The type of action to filter by.
     * @return A [CompletableDeferred] that will request the audit log.
     */
    fun requestedAuditLog(
        guildId: Long,
        userId: GetterSnowFlake? = null,
        limit: Int = 50,
        before: GetterSnowFlake? = null,
        actionType: AuditLogType? = null,
    ): CompletableDeferred<AuditLog>

    /**
     * Request to get all the members within the guild.
     *
     * @return A [CompletableDeferred] that will request the members.
     */
    fun requestedMembers(guild: Guild): CompletableDeferred<List<Member>> =
        requestedMembers(guild, null)

    /**
     * Request to get the amount of members specified by the limit.
     *
     * @param limit The amount of members to retrieve.
     * @return A [CompletableDeferred] that will request the members.
     */
    fun requestedMembers(guild: Guild, limit: Int?): CompletableDeferred<List<Member>>

    /**
     * Request to get a guild by its id.
     *
     * @param guildId The id of the guild.
     * @return A [CompletableDeferred] that will request the guild.
     */
    fun requestedGuild(guildId: Long): CompletableDeferred<Guild>

    /**
     * Request to get all the guilds the bot is in.
     *
     * @return A [CompletableDeferred] that will request the guilds.
     */
    fun requestedGuilds(): CompletableDeferred<List<Guild>>
}
