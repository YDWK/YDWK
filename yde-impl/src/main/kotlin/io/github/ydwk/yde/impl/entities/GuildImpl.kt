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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.*
import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.guild.Ban
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.WelcomeScreen
import io.github.ydwk.yde.entities.guild.enums.*
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.json
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.GetterSnowFlake
import kotlin.time.Duration
import kotlinx.coroutines.*

internal class GuildImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override var icon: String?,
    override var splash: String?,
    override var discoverySplash: String?,
    override var isOwner: Boolean?,
    override var ownerId: GetterSnowFlake,
    override var permissions: String?,
    override var afkChannelId: GetterSnowFlake?,
    override var afkTimeout: Int,
    override var isWidgetEnabled: Boolean?,
    override var widgetChannelId: GetterSnowFlake?,
    override var verificationLevel: VerificationLevel,
    override var defaultMessageNotificationsLevel: MessageNotificationLevel,
    override var explicitContentFilterLevel: ExplicitContentFilterLevel,
    override var roles: List<Role>,
    override var emojis: List<Emoji>,
    override var features: Set<GuildFeature>,
    override var mfaLevel: MFALevel,
    override var applicationId: GetterSnowFlake?,
    override var systemChannelId: GetterSnowFlake?,
    override var systemChannelFlags: SystemChannelFlag,
    override var rulesChannelId: GetterSnowFlake?,
    override var maxPresences: Int?,
    override var maxMembers: Int,
    override var vanityUrlCode: String?,
    override var description: String?,
    override var banner: String?,
    override var premiumTier: PremiumTier,
    override var premiumSubscriptionCount: Int,
    override var preferredLocale: String,
    override var publicUpdatesChannelId: GetterSnowFlake?,
    override var maxVideoChannelUsers: Int?,
    override var approximateMemberCount: Int?,
    override var approximatePresenceCount: Int?,
    override var welcomeScreen: WelcomeScreen?,
    override var nsfwLevel: NSFWLeveL,
    override var stickers: List<Sticker>,
    override var isBoostProgressBarEnabled: Boolean,
    override val voiceStates: List<VoiceState>,
    override val getUnorderedChannels: List<GuildChannel>,
    override var name: String
) : Guild, ToStringEntityImpl<Guild>(yde, Guild::class.java) {

    private suspend fun getBotAsMember(id: String, botId: String): Member {
        return yde.getMemberById(id, botId) ?: fetchMemberFromRestApi(id, botId)
    }

    private suspend fun fetchMemberFromRestApi(id: String, botId: String): Member {
        return withContext(yde.coroutineDispatcher) {
            yde.restApiManager
                .get(EndPoint.GuildEndpoint.GET_MEMBER, id, botId)
                .execute { response ->
                    val jsonBody = response.json(yde)
                    val member =
                        yde.entityInstanceBuilder.buildMember(
                            jsonBody, GetterSnowFlake.of(idAsLong), null)
                    (yde as YDEImpl).memberCache[id, jsonBody["user"]["id"].asText()] = member
                    member
                }
                .mapBoth({ it }, { throw it })
        }
    }

    override fun getRoleById(roleId: Long): Role? {
        return if (roles.any { it.idAsLong == roleId }) {
            roles.first { it.idAsLong == roleId }
        } else {
            null
        }
    }

    override suspend fun requestBans(): RestResult<List<Ban>> {
        return yde.restAPIMethodGetters.getGuildRestAPIMethods().requestedBanList(idAsLong)
    }

    override suspend fun createDmChannel(userId: Long): RestResult<DmChannel> {
        return yde.restAPIMethodGetters.getUserRestAPIMethods().createDm(userId)
    }

    override suspend fun getBotAsMember(): Member {
        return CoroutineScope(yde.coroutineDispatcher)
            .async { getBotAsMember(id, yde.bot?.id ?: throw IllegalStateException("Bot is null")) }
            .await()
    }

    override suspend fun banUser(
        userId: Long,
        deleteMessageDuration: Duration,
        reason: String?
    ): RestResult<NoResult> {
        return yde.restAPIMethodGetters
            .getGuildRestAPIMethods()
            .banUser(idAsLong, userId, deleteMessageDuration, reason)
    }

    override suspend fun unbanUser(userId: Long, reason: String?): RestResult<NoResult> {
        return yde.restAPIMethodGetters.getGuildRestAPIMethods().unbanUser(idAsLong, userId, reason)
    }

    override suspend fun kickMember(userId: Long, reason: String?): RestResult<NoResult> {
        return yde.restAPIMethodGetters
            .getGuildRestAPIMethods()
            .kickMember(idAsLong, userId, reason)
    }

    override suspend fun requestedAuditLog(
        userId: GetterSnowFlake?,
        limit: Int,
        before: GetterSnowFlake?,
        actionType: AuditLogType?
    ): RestResult<AuditLog> {
        return yde.restAPIMethodGetters
            .getGuildRestAPIMethods()
            .requestedAuditLog(idAsLong, userId, limit, before, actionType)
    }

    override fun getChannelById(channelId: Long): GuildChannel? {
        return getUnorderedChannels.firstOrNull { it.idAsLong == channelId }
    }

    override suspend fun retrieveMembers(): RestResult<List<Member>> {
        return yde.restAPIMethodGetters.getGuildRestAPIMethods().requestedMembers(idAsLong)
    }
}
