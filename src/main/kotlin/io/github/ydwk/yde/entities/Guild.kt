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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.getter.guild.GuildChannelGetter
import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.entities.channel.guild.message.text.GuildTextChannel
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.entities.guild.Ban
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.WelcomeScreen
import io.github.ydwk.yde.entities.guild.enums.*
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake
import kotlin.time.Duration
import kotlinx.coroutines.CompletableDeferred

/** This class is used to represent a discord guild object. */
interface Guild : SnowFlake, NameAbleEntity, GenericEntity {
    /**
     * The guild's icon hash.
     *
     * @return the guild's icon hash.
     */
    var icon: String?

    /**
     * The guild's splash hash.
     *
     * @return the guild's splash hash.
     */
    var splash: String?

    /**
     * The guild's discovery splash hash.
     *
     * @return the guild's discovery splash hash.
     */
    var discoverySplash: String?

    /**
     * Checks if this is user is owner of the guild.
     *
     * @return true if the user is owner of the guild.
     */
    var isOwner: Boolean?

    /**
     * The guild's owner id.
     *
     * @return the guild's owner id.
     */
    var ownerId: GetterSnowFlake

    /**
     * The guild's permissions.
     *
     * @return the guild's permissions.
     */
    var permissions: String?

    /**
     * The guild's afk channel id.
     *
     * @return the guild's afk channel id.
     */
    var afkChannelId: GetterSnowFlake?

    /**
     * The guild's afk timeout.
     *
     * @return the guild's afk timeout.
     */
    var afkTimeout: Int

    /**
     * Checks if the guild's widget is enabled.
     *
     * @return true if the guild's widget is enabled
     */
    var isWidgetEnabled: Boolean?

    /**
     * The guild's widget channel id.
     *
     * @return the guild's widget channel id.
     */
    var widgetChannelId: GetterSnowFlake?

    /**
     * The guild's verification level.
     *
     * @return the guild's verification level.
     */
    var verificationLevel: VerificationLevel

    /**
     * The guild's default message notification level.
     *
     * @return the guild's default message notification level.
     */
    var defaultMessageNotificationsLevel: MessageNotificationLevel

    /**
     * The guild's explicit content filter level.
     *
     * @return the guild's explicit content filter level.
     */
    var explicitContentFilterLevel: ExplicitContentFilterLevel

    /**
     * The guild's roles.
     *
     * @return the guild's roles.
     */
    var roles: List<Role>

    /**
     * The guild's emojis.
     *
     * @return the guild's emojis.
     */
    var emojis: List<Emoji>

    /**
     * The guild's features.
     *
     * @return the guild's features.
     */
    var features: Set<GuildFeature>

    /**
     * The guild's mfa level.
     *
     * @return the guild's mfa level.
     */
    var mfaLevel: MFALevel

    /**
     * The guild's application id.
     *
     * @return the guild's application id.
     */
    var applicationId: GetterSnowFlake?

    /**
     * The guild's system channel id.
     *
     * @return the guild's system channel id.
     */
    var systemChannelId: GetterSnowFlake?

    /**
     * The guild's system channel flags.
     *
     * @return the guild's system channel flags.
     */
    var systemChannelFlags: SystemChannelFlag

    /**
     * The guild's rules channel id.
     *
     * @return the guild's rules channel id.
     */
    var rulesChannelId: GetterSnowFlake?

    /**
     * The maximum number of presences for the guild (null is always returned, apart from the
     * largest of guilds).
     *
     * @return the maximum number of presences for the guild.
     */
    var maxPresences: Int?

    /**
     * The maximum number of members for the guild.
     *
     * @return the maximum number of members for the guild.
     */
    var maxMembers: Int

    /**
     * The vanity url code for the guild.
     *
     * @return the vanity url code for the guild.
     */
    var vanityUrlCode: String?

    /**
     * The description for the guild.
     *
     * @return the description for the guild.
     */
    var description: String?

    /**
     * The banner hash for the guild.
     *
     * @return the banner hash for the guild.
     */
    var banner: String?

    /**
     * The premium tier for the guild.
     *
     * @return the premium tier for the guild.
     */
    var premiumTier: PremiumTier

    /**
     * The premium subscription count for the guild.
     *
     * @return the premium subscription count for the guild.
     */
    var premiumSubscriptionCount: Int

    /**
     * The preferred locale for the guild.
     *
     * @return the preferred locale for the guild.
     */
    var preferredLocale: String

    /**
     * The public updates channel id for the guild.
     *
     * @return the public updates channel id for the guild.
     */
    var publicUpdatesChannelId: GetterSnowFlake?

    /**
     * The maximum amount of users in a video channel.
     *
     * @return the maximum amount of users in a video channel.
     */
    var maxVideoChannelUsers: Int?

    /**
     * The approximate member count for the guild.
     *
     * @return the approximate member count for the guild.
     */
    var approximateMemberCount: Int?

    /**
     * The approximate presence count for the guild.
     *
     * @return the approximate presence count for the guild.
     */
    var approximatePresenceCount: Int?

    /**
     * The welcome screen of the guild.
     *
     * @return the welcome screen of the guild.
     */
    var welcomeScreen: WelcomeScreen?

    /**
     * The guild's nsfw level.
     *
     * @return the guild's nsfw level.
     */
    var nsfwLevel: NSFWLeveL

    /**
     * The guild's custom stickers.
     *
     * @return the guild's custom stickers.
     */
    var stickers: List<Sticker>

    /**
     * Gets weather the guild has the boost progress bar enabled.
     *
     * @return weather the guild has the boost progress bar enabled.
     */
    var isBoostProgressBarEnabled: Boolean

    /**
     * Requests a list of ban's for the guild.
     *
     * @return a list of ban's for the guild.
     */
    val requestBans: CompletableDeferred<List<Ban>>
        get() {
            return yde.restAPIMethodGetters.getGuildRestAPIMethods().requestedBanList(idAsLong)
        }

    /**
     * All the current voice states for the guild.
     *
     * @return all the current voice states for the guild.
     */
    val voiceStates: List<VoiceState>

    /**
     * Creates a dm channel..
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: Long): CompletableDeferred<DmChannel> {
        return yde.restAPIMethodGetters.getUserRestAPIMethods().createDm(userId)
    }

    /**
     * Creates a dm channel.
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: String): CompletableDeferred<DmChannel> =
        createDmChannel(userId.toLong())

    /**
     * Creates a dm channel.
     *
     * @param user The user who you want to create a dm channel with.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(user: User): CompletableDeferred<DmChannel> = createDmChannel(user.id)

    /**
     * The bot as a member of the guild.
     *
     * @return The [Member] object.
     * @throws IllegalStateException If the bot is not in the guild.
     */
    val botAsMember: Member

    /**
     * Bans a user from the guild.
     *
     * @param userId The id of the user.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [CompletableDeferred] that completes when the ban is created.
     */
    fun banUser(
        userId: Long,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null,
    ): CompletableDeferred<NoResult> {
        return yde.restAPIMethodGetters
            .getGuildRestAPIMethods()
            .banUser(idAsLong, userId, deleteMessageDuration, reason)
    }

    /**
     * Bans a user from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the ban.
     * @return A [CompletableDeferred] that completes when the ban is created.
     */
    fun banUser(userId: Long, reason: String? = null) = banUser(userId, Duration.ZERO, reason)

    /**
     * Bans a user from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the ban.
     * @return A [CompletableDeferred] that completes when the ban is created.
     */
    fun banUser(userId: String, reason: String? = null) = banUser(userId, Duration.ZERO, reason)

    /**
     * Bans a user from the guild.
     *
     * @param userId The id of the user.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [CompletableDeferred] that completes when the ban is created.
     */
    fun banUser(
        userId: String,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null,
    ): CompletableDeferred<NoResult> = banUser(userId.toLong(), deleteMessageDuration, reason)

    /**
     * Bans a user from the guild.
     *
     * @param user The user to ban.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     */
    fun banUser(
        user: User,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null,
    ): CompletableDeferred<NoResult> = banUser(user.id, deleteMessageDuration, reason)

    /**
     * Bans a member from the guild.
     *
     * @param member The member to ban.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [CompletableDeferred] that completes when the ban is created.
     */
    fun banMember(
        member: Member,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null,
    ): CompletableDeferred<NoResult> = banUser(member.user, deleteMessageDuration, reason)

    /**
     * Unbans a user from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the unban.
     * @return A [CompletableDeferred] that completes when the unban is created.
     */
    fun unbanUser(userId: Long, reason: String? = null): CompletableDeferred<NoResult> {
        return yde.restAPIMethodGetters.getGuildRestAPIMethods().unbanUser(idAsLong, userId, reason)
    }

    /**
     * Unbans a user from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the unban.
     * @return A [CompletableDeferred] that completes when the unban is created.
     */
    fun unbanUser(userId: String, reason: String? = null): CompletableDeferred<NoResult> =
        unbanUser(userId.toLong(), reason)

    /**
     * Unbans a user from the guild.
     *
     * @param user The user to unban.
     * @param reason The reason for the unban.
     * @return A [CompletableDeferred] that completes when the unban is created.
     */
    fun unbanUser(user: User, reason: String? = null): CompletableDeferred<NoResult> =
        unbanUser(user.id, reason)

    /**
     * Kicks a member from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the kick.
     * @return A [CompletableDeferred] that completes when the kick is created.
     */
    fun kickMember(userId: Long, reason: String? = null): CompletableDeferred<NoResult> {
        return yde.restAPIMethodGetters
            .getGuildRestAPIMethods()
            .kickMember(idAsLong, userId, reason)
    }

    /**
     * Kicks a member from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the kick.
     * @return A [CompletableDeferred] that completes when the kick is created.
     */
    fun kickMember(userId: String, reason: String? = null): CompletableDeferred<NoResult> =
        kickMember(userId.toLong(), reason)

    /**
     * Kicks a member from the guild.
     *
     * @param member The member to kick.
     * @param reason The reason for the kick.
     * @return A [CompletableDeferred] that completes when the kick is created.
     */
    fun kickMember(member: Member, reason: String? = null): CompletableDeferred<NoResult> =
        kickMember(member.user.id, reason)

    /**
     * Request the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @param actionType The type of action to filter by.
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(
        userId: GetterSnowFlake? = null,
        limit: Int = 50,
        before: GetterSnowFlake? = null,
        actionType: AuditLogType? = null,
    ): CompletableDeferred<AuditLog> {
        return yde.restAPIMethodGetters
            .getGuildRestAPIMethods()
            .requestedAuditLog(idAsLong, userId, limit, before, actionType)
    }

    /**
     * Request the audit log for the guild.
     *
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(): CompletableDeferred<AuditLog> =
        requestedAuditLog(GetterSnowFlake.asNull, 50, null, null)

    /**
     * Request the audit log for the guild.
     *
     * @param userId The id of the user.
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(userId: GetterSnowFlake): CompletableDeferred<AuditLog> =
        requestedAuditLog(userId, 50, null, null)

    /**
     * Request the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(userId: GetterSnowFlake, limit: Int): CompletableDeferred<AuditLog> =
        requestedAuditLog(userId, limit, null, null)

    /**
     * Request the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(
        userId: GetterSnowFlake,
        limit: Int,
        before: GetterSnowFlake,
    ): CompletableDeferred<AuditLog> = requestedAuditLog(userId, limit, before, null)

    /**
     * Request the audit log for the guild.
     *
     * @param user The user to filter by.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @param actionType The type of action to filter by.
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(
        user: User? = null,
        limit: Int = 50,
        before: GetterSnowFlake? = null,
        actionType: AuditLogType? = null,
    ): CompletableDeferred<AuditLog> = requestedAuditLog(user, limit, before, actionType)

    /**
     * Request the audit log for the guild.
     *
     * @param user The user to filter by.
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(user: User): CompletableDeferred<AuditLog> =
        requestedAuditLog(user, 50, null, null)

    /**
     * Request the audit log for the guild.
     *
     * @param user The user to filter by.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(user: User, limit: Int): CompletableDeferred<AuditLog> =
        requestedAuditLog(user, limit, null, null)

    /**
     * Request the audit log for the guild.
     *
     * @param user The user to filter by.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @return A [CompletableDeferred] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(
        user: User,
        limit: Int,
        before: GetterSnowFlake,
    ): CompletableDeferred<AuditLog> = requestedAuditLog(user, limit, before, null)

    /**
     * Gets a role from the guild.
     *
     * @param roleId The id of the role.
     * @return The role, or null if it doesn't exist.
     */
    fun getRoleById(roleId: Long): Role?

    /**
     * Gets a role from the guild.
     *
     * @param roleId The id of the role.
     * @return The role, or null if it doesn't exist.
     */
    fun getRoleById(roleId: String): Role? = getRoleById(roleId.toLong())

    /**
     * Gets all the channels as unordered list.
     *
     * @return The channels.
     */
    val getUnorderedChannels: List<GuildChannel>

    /**
     * Gets all the channels as sorted list.
     *
     * @return The channels.
     */
    val getChannels: List<GuildChannel>
        get() = getUnorderedChannels.sortedBy { it.position }

    /**
     * Gets all the categories as sorted list.
     *
     * @return The categories.
     */
    val getCategories: List<GuildCategory>
        get() = getChannels.filterIsInstance<GuildCategory>()

    /**
     * Gets all the text channels as sorted list.
     *
     * @return The text channels.
     */
    val getTextChannels: List<GuildTextChannel>
        get() = getChannels.filterIsInstance<GuildTextChannel>()

    /**
     * Gets all the voice channels as sorted list.
     *
     * @return The voice channels.
     */
    val getVoiceChannels: List<GuildVoiceChannel>
        get() = getChannels.filterIsInstance<GuildVoiceChannel>()

    /**
     * The channel by its id.
     *
     * @param channelId The id of the channel.
     * @return The channel, or null if it doesn't exist.
     */
    fun getChannelById(channelId: Long): GuildChannel?

    /**
     * The channel by its id.
     *
     * @param channelId The id of the channel.
     * @return The channel, or null if it doesn't exist.
     */
    fun getChannelById(channelId: String): GuildChannel? = getChannelById(channelId.toLong())

    /**
     * The channel getter by its id.
     *
     * @param channelId The id of the channel.
     * @return The channel getter.
     */
    fun getChannelGetterById(channelId: Long): GuildChannelGetter? =
        getChannelById(channelId)?.guildChannelGetter

    /**
     * The channel getter by its id.
     *
     * @param channelId The id of the channel.
     * @return The channel getter.
     */
    fun getChannelGetterById(channelId: String): GuildChannelGetter? =
        getChannelGetterById(channelId.toLong())

    /**
     * Gets all the members of the guild.
     *
     * @return The members.
     */
    val retrieveMembers: CompletableDeferred<List<Member>>
        get() {
            return yde.restAPIMethodGetters.getGuildRestAPIMethods().requestedMembers(this)
        }

    /**
     * Gets all the members of the guild.
     *
     * @param limit The limit of members to retrieve.
     * @return The members.
     */
    fun retrieveMembers(limit: Int): CompletableDeferred<List<Member>> {
        return yde.restAPIMethodGetters.getGuildRestAPIMethods().requestedMembers(this, limit)
    }

    /**
     * The member by its user id.
     *
     * @param userId The id of the user.
     * @return The member, or null if it doesn't exist.
     */
    fun getMemberById(userId: Long): Member? {
        return yde.getMemberById(idAsLong, userId)
    }

    /**
     * The member by its user id.
     *
     * @param userId The id of the user.
     * @return The member, or null if it doesn't exist.
     */
    fun getMemberById(userId: String): Member? = getMemberById(userId.toLong())

    /**
     * The @everyone role. This role is always present.
     *
     * @return The @everyone role.
     */
    val everyoneRole: Role
        get() =
            getRoleById(idAsLong)
                ?: throw IllegalStateException("The @everyone role is not present.")
}
