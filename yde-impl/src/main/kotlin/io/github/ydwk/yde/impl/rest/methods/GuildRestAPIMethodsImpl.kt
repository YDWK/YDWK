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
package io.github.ydwk.yde.impl.rest.methods

import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.AuditLog
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.entities.guild.Ban
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.json
import io.github.ydwk.yde.rest.methods.GuildRestAPIMethods
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.rest.toTextContent
import io.github.ydwk.yde.util.GetterSnowFlake
import kotlin.time.Duration

class GuildRestAPIMethodsImpl(val yde: YDE) : GuildRestAPIMethods {
    override suspend fun banUser(
        guildId: Long,
        userId: Long,
        deleteMessageDuration: Duration,
        reason: String?
    ): RestResult<NoResult> {
        return yde.restApiManager
            .put(
                yde.objectMapper
                    .createObjectNode()
                    .put("delete_message_seconds", deleteMessageDuration.inWholeSeconds)
                    .toString()
                    .toTextContent(),
                EndPoint.GuildEndpoint.BAN,
                guildId.toString(),
                userId.toString())
            .addReason(reason)
            .executeWithNoResult()
    }

    override suspend fun unbanUser(
        guildId: Long,
        userId: Long,
        reason: String?
    ): RestResult<NoResult> {
        return yde.restApiManager
            .delete(EndPoint.GuildEndpoint.BAN, guildId.toString(), userId.toString())
            .addReason(reason)
            .executeWithNoResult()
    }

    override suspend fun kickMember(
        guildId: Long,
        userId: Long,
        reason: String?
    ): RestResult<NoResult> {
        return yde.restApiManager
            .delete(EndPoint.GuildEndpoint.KICK, guildId.toString(), userId.toString())
            .addReason(reason)
            .executeWithNoResult()
    }

    override suspend fun requestedBanList(guildId: Long): RestResult<List<Ban>> {
        return yde.restApiManager
            .get(EndPoint.GuildEndpoint.GET_BANS, guildId.toString())
            .execute { it ->
                val jsonBody = it.json(yde)
                jsonBody.map { yde.entityInstanceBuilder.buildBan(it) }
            }
    }

    override suspend fun requestedAuditLog(
        guildId: Long,
        userId: GetterSnowFlake?,
        limit: Int,
        before: GetterSnowFlake?,
        actionType: AuditLogType?
    ): RestResult<AuditLog> {
        val rest = yde.restApiManager

        if (userId != null) {
            rest.addQueryParameter("user_id", userId.asString)
        }

        if (before != null) {
            rest.addQueryParameter("before", before.asString)
        }

        if (actionType != null) {
            rest.addQueryParameter("action_type", actionType.getType().toString())
        }

        return rest
            .addQueryParameter("limit", limit.toString())
            .get(EndPoint.GuildEndpoint.GET_AUDIT_LOGS, guildId.toString())
            .execute {
                val jsonBody = it.json(yde)
                yde.entityInstanceBuilder.buildAuditLog(jsonBody)
            }
    }

    override suspend fun requestedMembers(guild: Guild, limit: Int?): RestResult<List<Member>> {
        return yde.restApiManager
            .addQueryParameter("limit", limit.toString())
            .get(EndPoint.GuildEndpoint.GET_MEMBERS, guild.id.toString())
            .execute {
                val jsonBody = it.json(yde)
                val members: ArrayNode = jsonBody as ArrayNode
                val memberList = mutableListOf<Member>()
                for (member in members) {
                    memberList.add(yde.entityInstanceBuilder.buildMember(member, guild))
                }
                memberList
            }
    }

    override suspend fun requestedGuild(guildId: Long): RestResult<Guild> {
        return this.yde.restApiManager
            .get(EndPoint.GuildEndpoint.GET_GUILD, guildId.toString())
            .execute {
                val jsonBody = it.json(yde)
                yde.entityInstanceBuilder.buildGuild(jsonBody)
            }
    }

    override suspend fun requestedGuilds(): RestResult<List<Guild>> {
        return this.yde.restApiManager.get(EndPoint.GuildEndpoint.GET_GUILDS).execute() { it ->
            val jsonBody = it.json(yde)
            jsonBody.map { yde.entityInstanceBuilder.buildGuild(it) }
        }
    }
}
