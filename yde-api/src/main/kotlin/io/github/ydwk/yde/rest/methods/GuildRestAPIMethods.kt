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
package io.github.ydwk.yde.rest.methods

import io.github.ydwk.yde.entities.AuditLog
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.PartialGuild
import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.entities.guild.Ban
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.GetterSnowFlake
import kotlin.time.Duration

interface GuildRestAPIMethods {

    //////////////////// Moderation Actions ////////////////////////
    /**
     * Bans a user from the guild.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [RestResult] that will contain [NoResult] confirming the ban.
     */
    suspend fun banUser(
        guildId: Long,
        userId: Long,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null,
    ): RestResult<NoResult>

    /**
     * Unbans a user from the guild.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @param reason The reason for the unban.
     * @return A [RestResult] that will contain [NoResult] confirming the unban.
     */
    suspend fun unbanUser(
        guildId: Long,
        userId: Long,
        reason: String? = null,
    ): RestResult<NoResult>

    /**
     * Kicks a member from the guild.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @param reason The reason for the kick.
     * @return A [RestResult] that will contain [NoResult] confirming the kick.
     */
    suspend fun kickMember(
        guildId: Long,
        userId: Long,
        reason: String? = null
    ): RestResult<NoResult>

    /**
     * Requests the ban list for the guild.
     *
     * @param guildId The id of the guild.
     * @return A [RestResult] that will contain a list of [Ban]s.
     */
    suspend fun requestedBanList(guildId: Long): RestResult<List<Ban>>

    //////////////// Guild Request Methods ///////////////////////

    /**
     * Request the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @param actionType The type of action to filter by.
     * @return A [RestResult] that will contain the [AuditLog].
     */
    suspend fun requestedAuditLog(
        guildId: Long,
        userId: GetterSnowFlake? = null,
        limit: Int = 50,
        before: GetterSnowFlake? = null,
        actionType: AuditLogType? = null,
    ): RestResult<AuditLog>

    /**
     * Request to get all the members within the guild.
     *
     * @param guildId The id of the guild.
     * @return A [RestResult] that will contain a list of [Member]s.
     */
    suspend fun requestedMembers(guildId: Long): RestResult<List<Member>> =
        requestedMembers(guildId, null)

    /**
     * Request to get the amount of members specified by the limit.
     *
     * @param limit The amount of members to retrieve.
     * @return A [RestResult] that will contain a list of [Member]s.
     */
    suspend fun requestedMembers(guildId: Long, limit: Int?): RestResult<List<Member>>

    /**
     * Request to get a guild by its id.
     *
     * @param guildId The id of the guild.
     * @return A [RestResult] that will contain the requested [Guild].
     */
    suspend fun requestedGuild(guildId: Long): RestResult<Guild>

    /**
     * Request to get all the guilds the bot is in.
     *
     * @return A [RestResult] that will contain a list of [Guild]s.
     */
    @Deprecated(
        "Use requestedPartialGuilds instead",
        ReplaceWith("requestedPartialGuilds()"),
        level = DeprecationLevel.WARNING)
    suspend fun requestedGuilds(): RestResult<List<Guild>>

    /**
     * Request to get the guilds the bot is in.
     *
     * @param limit The amount of guilds to retrieve.
     * @return A [RestResult] that will contain a list of [PartialGuild]s.
     */
    suspend fun requestedPartialGuilds(limit: Int): RestResult<List<PartialGuild>>

    /**
     * Request to get the guilds the bot is in.
     *
     * @return A [RestResult] that will contain a list of [PartialGuild]s.
     */
    suspend fun requestedPartialGuilds(): RestResult<List<PartialGuild>> =
        requestedPartialGuilds(200)
}
