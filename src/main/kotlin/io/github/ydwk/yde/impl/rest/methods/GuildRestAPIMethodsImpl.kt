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
package io.github.ydwk.yde.impl.rest.methods

import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.AuditLog
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.entities.guild.Ban
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.AuditLogImpl
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.yde.impl.entities.guild.BanImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.methods.GuildRestAPIMethods
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.GetterSnowFlake
import kotlin.time.Duration
import kotlinx.coroutines.CompletableDeferred
import okhttp3.RequestBody.Companion.toRequestBody

class GuildRestAPIMethodsImpl(val yde: YDE) : GuildRestAPIMethods {
    override fun banUser(
        guildId: Long,
        userId: Long,
        deleteMessageDuration: Duration,
        reason: String?
    ): CompletableDeferred<NoResult> {
        return yde.restApiManager
            .put(
                yde.objectMapper
                    .createObjectNode()
                    .put("delete_message_seconds", deleteMessageDuration.inWholeSeconds)
                    .toString()
                    .toRequestBody(),
                EndPoint.GuildEndpoint.BAN,
                guildId.toString(),
                userId.toString())
            .addReason(reason)
            .executeWithNoResult()
    }

    override fun unbanUser(
        guildId: Long,
        userId: Long,
        reason: String?
    ): CompletableDeferred<NoResult> {
        return yde.restApiManager
            .delete(EndPoint.GuildEndpoint.BAN, guildId.toString(), userId.toString())
            .addReason(reason)
            .executeWithNoResult()
    }

    override fun kickMember(
        guildId: Long,
        userId: Long,
        reason: String?
    ): CompletableDeferred<NoResult> {
        return yde.restApiManager
            .delete(EndPoint.GuildEndpoint.KICK, guildId.toString(), userId.toString())
            .addReason(reason)
            .executeWithNoResult()
    }

    override fun requestedBanList(guildId: Long): CompletableDeferred<List<Ban>> {
        return yde.restApiManager
            .get(EndPoint.GuildEndpoint.GET_BANS, guildId.toString())
            .execute { it ->
                val jsonBody = it.jsonBody
                jsonBody?.map { BanImpl(yde, it) }
                    ?: throw IllegalStateException("Response body is null")
            }
    }

    override fun requestedAuditLog(
        guildId: Long,
        userId: GetterSnowFlake?,
        limit: Int,
        before: GetterSnowFlake?,
        actionType: AuditLogType?
    ): CompletableDeferred<AuditLog> {
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
            .execute() {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    AuditLogImpl(yde, jsonBody)
                }
            }
    }

    override fun requestedMembers(guild: Guild, limit: Int?): CompletableDeferred<List<Member>> {
        return yde.restApiManager
            .addQueryParameter("limit", limit.toString())
            .get(EndPoint.GuildEndpoint.GET_MEMBERS, guild.id.toString())
            .execute() {
                val jsonBody = it.jsonBody
                val members: ArrayNode = jsonBody as ArrayNode
                val memberList = mutableListOf<Member>()
                for (member in members) {
                    memberList.add(MemberImpl(yde as YDEImpl, member, guild))
                }
                memberList
            }
    }

    override fun requestedGuild(guildId: Long): CompletableDeferred<Guild> {
        return this.yde.restApiManager
            .get(EndPoint.GuildEndpoint.GET_GUILD, guildId.toString())
            .execute() {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    GuildImpl(yde, jsonBody, jsonBody["id"].asLong())
                }
            }
    }

    override fun requestedGuilds(): CompletableDeferred<List<Guild>> {
        return this.yde.restApiManager.get(EndPoint.GuildEndpoint.GET_GUILDS).execute() { it ->
            val jsonBody = it.jsonBody
            jsonBody?.map { GuildImpl(yde, it, it["id"].asLong()) }
                ?: throw IllegalStateException("json body is null")
        }
    }
}
