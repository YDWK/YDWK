/*
 * Copyright 2022 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.entities

import io.github.ydwk.ydwk.entities.audit.AuditLogType
import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.entities.guild.Ban
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.guild.WelcomeScreen
import io.github.ydwk.ydwk.entities.guild.enums.*
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.NameAbleEntity
import io.github.ydwk.ydwk.util.SnowFlake
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration

/** This class is used to represent a discord guild object. */
interface Guild : SnowFlake, NameAbleEntity, GenericEntity {
    /**
     * Used to get the guild's icon hash
     *
     * @return the guild's icon hash
     */
    var icon: String?

    /**
     * Used to get the guild's splash hash
     *
     * @return the guild's splash hash
     */
    var splash: String?

    /**
     * Used to get the guild's discovery splash hash
     *
     * @return the guild's discovery splash hash
     */
    var discoverySplash: String?

    /**
     * Used to check if this is user is owner of the guild
     *
     * @return true if the user is owner of the guild
     */
    var isOwner: Boolean?

    /**
     * Used to get the guild's owner id
     *
     * @return the guild's owner id
     */
    var ownerId: GetterSnowFlake

    /**
     * Used to get the guild's permissions
     *
     * @return the guild's permissions
     */
    var permissions: String?

    /**
     * Used to get the guild's afk channel id
     *
     * @return the guild's afk channel id
     */
    var afkChannelId: GetterSnowFlake?

    /**
     * Used to get the guild's afk timeout
     *
     * @return the guild's afk timeout
     */
    var afkTimeout: Int

    /**
     * Used to check if the guild's widget is enabled
     *
     * @return true if the guild's widget is enabled
     */
    var isWidgetEnabled: Boolean?

    /**
     * Used to get the guild's widget channel id
     *
     * @return the guild's widget channel id
     */
    var widgetChannelId: GetterSnowFlake?

    /**
     * Used to get the guild's verification level
     *
     * @return the guild's verification level
     */
    var verificationLevel: VerificationLevel

    /**
     * Used to get the guild's default message notification level
     *
     * @return the guild's default message notification level
     */
    var defaultMessageNotificationsLevel: MessageNotificationLevel

    /**
     * Used to get the guild's explicit content filter level
     *
     * @return the guild's explicit content filter level
     */
    var explicitContentFilterLevel: ExplicitContentFilterLevel

    /**
     * Used to get the guild's roles
     *
     * @return the guild's roles
     */
    var roles: List<Role>

    /**
     * Used to get the guild's emojis
     *
     * @return the guild's emojis
     */
    var emojis: List<Emoji>

    /**
     * Used to get the guild's features
     *
     * @return the guild's features
     */
    var features: Set<GuildFeature>

    /**
     * Used to get the guild's mfa level
     *
     * @return the guild's mfa level
     */
    var mfaLevel: MFALevel

    /**
     * Used to get the guild's application id
     *
     * @return the guild's application id
     */
    var applicationId: GetterSnowFlake?

    /**
     * Used to get the guild's system channel id
     *
     * @return the guild's system channel id
     */
    var systemChannelId: GetterSnowFlake?

    /**
     * Used to get the guild's system channel flags
     *
     * @return the guild's system channel flags
     */
    var systemChannelFlags: SystemChannelFlag

    /**
     * Used to get the guild's rules channel id
     *
     * @return the guild's rules channel id
     */
    var rulesChannelId: GetterSnowFlake?

    /**
     * Gets the maximum number of presences for the guild (null is always returned, apart from the
     * largest of guilds)
     *
     * @return the maximum number of presences for the guild
     */
    var maxPresences: Int?

    /**
     * Gets the maximum number of members for the guild
     *
     * @return the maximum number of members for the guild
     */
    var maxMembers: Int

    /**
     * Gets the vanity url code for the guild
     *
     * @return the vanity url code for the guild
     */
    var vanityUrlCode: String?

    /**
     * Gets the description for the guild
     *
     * @return the description for the guild
     */
    var description: String?

    /**
     * Gets the banner hash for the guild
     *
     * @return the banner hash for the guild
     */
    var banner: String?

    /**
     * Gets the premium tier for the guild
     *
     * @return the premium tier for the guild
     */
    var premiumTier: PremiumTier

    /**
     * Gets the premium subscription count for the guild
     *
     * @return the premium subscription count for the guild
     */
    var premiumSubscriptionCount: Int

    /**
     * Gets the preferred locale for the guild
     *
     * @return the preferred locale for the guild
     */
    var preferredLocale: String

    /**
     * Gets the public updates channel id for the guild
     *
     * @return the public updates channel id for the guild
     */
    var publicUpdatesChannelId: GetterSnowFlake?

    /**
     * Gets the maximum amount of users in a video channel
     *
     * @return the maximum amount of users in a video channel
     */
    var maxVideoChannelUsers: Int?

    /**
     * Gets the approximate member count for the guild
     *
     * @return the approximate member count for the guild
     */
    var approximateMemberCount: Int?

    /**
     * Gets the approximate presence count for the guild
     *
     * @return the approximate presence count for the guild
     */
    var approximatePresenceCount: Int?

    /**
     * Gets the welcome screen of the guild
     *
     * @return the welcome screen of the guild
     */
    var welcomeScreen: WelcomeScreen?

    /**
     * Gets the guild's nsfw level
     *
     * @return the guild's nsfw level
     */
    var nsfwLevel: NSFWLeveL

    /**
     * Gets the guild's custom stickers
     *
     * @return the guild's custom stickers
     */
    var stickers: List<Sticker>

    /**
     * Gets weather the guild has the boost progress bar enabled
     *
     * @return weather the guild has the boost progress bar enabled
     */
    var isBoostProgressBarEnabled: Boolean

    /**
     * Gets a list of ban's for the guild
     *
     * @return a list of ban's for the guild
     */
    val requestBans: CompletableFuture<List<Ban>>

    /**
     * Used to create a dm channel.
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: Long): CompletableFuture<DmChannel>

    /**
     * Used to create a dm channel.
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: String): CompletableFuture<DmChannel> =
        createDmChannel(userId.toLong())

    /**
     * Used to create a dm channel.
     *
     * @param user The user who you want to create a dm channel with.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(user: User): CompletableFuture<DmChannel> = createDmChannel(user.id)

    /**
     * Used to get the bot as a member of the guild.
     *
     * @return The [Member] object.
     * @throws IllegalStateException If the bot is not in the guild.
     */
    val botAsMember: Member

    /**
     * Used to ban a user from the guild.
     *
     * @param userId The id of the user.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [CompletableFuture] that completes when the ban is created.
     */
    fun banUser(
        userId: Long,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null
    ): CompletableFuture<Void>

    /**
     * Used to ban a user from the guild.
     *
     * @param userId The id of the user.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [CompletableFuture] that completes when the ban is created.
     */
    fun banUser(
        userId: String,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null
    ): CompletableFuture<Void> = banUser(userId.toLong(), deleteMessageDuration, reason)

    /**
     * Used to ban a user from the guild.
     *
     * @param user The user to ban.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     */
    fun banUser(
        user: User,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null
    ): CompletableFuture<Void> = banUser(user.id, deleteMessageDuration, reason)

    /**
     * Used to ban a member from the guild.
     *
     * @param member The member to ban.
     * @param deleteMessageDuration The duration of the messages to delete.
     * @param reason The reason for the ban.
     * @return A [CompletableFuture] that completes when the ban is created.
     */
    fun banMember(
        member: Member,
        deleteMessageDuration: Duration = Duration.ZERO,
        reason: String? = null
    ): CompletableFuture<Void> =
        banUser(
            member.user ?: throw IllegalStateException("Member has no user"),
            deleteMessageDuration,
            reason)

    /**
     * Used to unban a user from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the unban.
     * @return A [CompletableFuture] that completes when the unban is created.
     */
    fun unbanUser(userId: Long, reason: String? = null): CompletableFuture<Void>

    /**
     * Used to unban a user from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the unban.
     * @return A [CompletableFuture] that completes when the unban is created.
     */
    fun unbanUser(userId: String, reason: String? = null): CompletableFuture<Void> =
        unbanUser(userId.toLong(), reason)

    /**
     * Used to unban a user from the guild.
     *
     * @param user The user to unban.
     * @param reason The reason for the unban.
     * @return A [CompletableFuture] that completes when the unban is created.
     */
    fun unbanUser(user: User, reason: String? = null): CompletableFuture<Void> =
        unbanUser(user.id, reason)

    /**
     * Used to kick a member from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the kick.
     * @return A [CompletableFuture] that completes when the kick is created.
     */
    fun kickMember(userId: Long, reason: String? = null): CompletableFuture<Void>

    /**
     * Used to kick a member from the guild.
     *
     * @param userId The id of the user.
     * @param reason The reason for the kick.
     * @return A [CompletableFuture] that completes when the kick is created.
     */
    fun kickMember(userId: String, reason: String? = null): CompletableFuture<Void> =
        kickMember(userId.toLong(), reason)

    /**
     * Used to kick a member from the guild.
     *
     * @param member The member to kick.
     * @param reason The reason for the kick.
     * @return A [CompletableFuture] that completes when the kick is created.
     */
    fun kickMember(member: Member, reason: String? = null): CompletableFuture<Void> =
        member.user?.let { kickMember(it.id, reason) }
            ?: throw IllegalStateException("Member has no user")

    /**
     * Used to get the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @param actionType The type of action to filter by.
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(
        userId: GetterSnowFlake? = null,
        limit: Int = 50,
        before: GetterSnowFlake? = null,
        actionType: AuditLogType? = null
    ): CompletableFuture<AuditLog>

    /**
     * Used to get the audit log for the guild.
     *
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(): CompletableFuture<AuditLog> =
        requestedAuditLog(GetterSnowFlake.asNull, 50, null, null)

    /**
     * Used to get the audit log for the guild.
     *
     * @param userId The id of the user.
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(userId: GetterSnowFlake): CompletableFuture<AuditLog> =
        requestedAuditLog(userId, 50, null, null)

    /**
     * Used to get the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(userId: GetterSnowFlake, limit: Int): CompletableFuture<AuditLog> =
        requestedAuditLog(userId, limit, null, null)

    /**
     * Used to get the audit log for the guild.
     *
     * @param userId The id of the user.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(
        userId: GetterSnowFlake,
        limit: Int,
        before: GetterSnowFlake
    ): CompletableFuture<AuditLog> = requestedAuditLog(userId, limit, before, null)

    /**
     * Used to get the audit log for the guild.
     *
     * @param user The user to filter by.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @param actionType The type of action to filter by.
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(
        user: User? = null,
        limit: Int = 50,
        before: GetterSnowFlake? = null,
        actionType: AuditLogType? = null
    ): CompletableFuture<AuditLog> = requestedAuditLog(user, limit, before, actionType)

    /**
     * Used to get the audit log for the guild.
     *
     * @param user The user to filter by.
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(user: User): CompletableFuture<AuditLog> = requestedAuditLog(user, 50, null, null)

    /**
     * Used to get the audit log for the guild.
     *
     * @param user The user to filter by.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(user: User, limit: Int): CompletableFuture<AuditLog> =
        requestedAuditLog(user, limit, null, null)

    /**
     * Used to get the audit log for the guild.
     *
     * @param user The user to filter by.
     * @param limit Maximum number of entries (between 1-100) to return, defaults to 50
     * @param before Entries that preceded a specific audit log entry ID.
     * @return A [CompletableFuture] that completes when the audit log is retrieved.
     */
    fun requestedAuditLog(user: User, limit: Int, before: GetterSnowFlake): CompletableFuture<AuditLog> =
        requestedAuditLog(user, limit, before, null)

    /**
     * Used to get a role from the guild.
     *
     * @param roleId The id of the role.
     * @return The role, or null if it doesn't exist.
     */
    fun getRole(roleId: Long): Role?

    /**
     * Used to get a role from the guild.
     *
     * @param roleId The id of the role.
     * @return The role, or null if it doesn't exist.
     */
    fun getRole(roleId: String): Role? = getRole(roleId.toLong())
}
