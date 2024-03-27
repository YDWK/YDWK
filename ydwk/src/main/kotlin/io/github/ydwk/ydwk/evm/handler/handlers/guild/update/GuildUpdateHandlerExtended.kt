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
package io.github.ydwk.ydwk.evm.handler.handlers.guild.update

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Sticker
import io.github.ydwk.yde.entities.guild.enums.*
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.guild.update.*
import io.github.ydwk.ydwk.evm.handler.handlers.guild.GuildUpdateHandler
import io.github.ydwk.ydwk.impl.YDWKImpl
import org.slf4j.LoggerFactory

class GuildUpdateHandlerExtended(ydwk: YDWKImpl, json: JsonNode) : GuildUpdateHandler(ydwk, json) {
    private val logger = LoggerFactory.getLogger(GuildUpdateHandlerExtended::class.java)

    fun onUpdate(
        guild: Guild,
        oldName: String,
        newName: String,
        oldIcon: String?,
        newIcon: String?,
        oldSplash: String?,
        newSplash: String?,
        oldDiscoverySplash: String?,
        newDiscoverySplash: String?,
        oldOwnerId: Long,
        newOwnerId: Long,
        oldPermissions: String?,
        newPermissions: String,
        oldAfkChannelId: Long?,
        newAfkChannelId: Long?,
        oldAfkTimeout: Int,
        newAfkTimeout: Int,
        oldWidgetEnabled: Boolean?,
        newWidgetEnabled: Boolean,
        oldWidgetChannelId: Long?,
        newWidgetChannelId: Long?,
        oldVerificationLevel: Int,
        newVerificationLevel: Int,
        oldDefaultMessageNotifications: Int,
        newDefaultMessageNotifications: Int,
        oldExplicitContentFilter: Int,
        newExplicitContentFilter: Int,
        oldMfaLevel: Int,
        newMfaLevel: Int,
        oldApplicationId: Long?,
        newApplicationId: Long?,
        oldSystemChannelId: Long?,
        newSystemChannelId: Long?,
        oldSystemChannelFlags: Int,
        newSystemChannelFlags: Int,
        oldRulesChannelId: Long?,
        newRulesChannelId: Long?,
        oldMaxPresences: Int?,
        newMaxPresences: Int?,
        oldMaxMembers: Int,
        newMaxMembers: Int,
        oldVanityUrlCode: String?,
        newVanityUrlCode: String?,
        oldDescription: String?,
        newDescription: String?,
        oldBanner: String?,
        newBanner: String?,
        oldPremiumTier: Int,
        newPremiumTier: Int,
        oldPremiumSubscriptionCount: Int,
        newPremiumSubscriptionCount: Int,
        oldPreferredLocale: String,
        newPreferredLocale: String,
        oldPublicUpdatesChannelId: Long?,
        newPublicUpdatesChannelId: Long?,
        oldMaxVideoChannelUsers: Int?,
        newMaxVideoChannelUsers: Int?,
        oldApproximateMemberCount: Int?,
        newApproximateMemberCount: Int?,
        oldApproximatePresenceCount: Int?,
        newApproximatePresenceCount: Int?,
        oldWelcomeScreen: JsonNode?,
        newWelcomeScreen: JsonNode?,
        oldNSFWLevel: Int,
        newNSFWLevel: Int,
        oldStickers: List<Sticker>,
        newStickers: JsonNode?,
        wasBoostProgressBarEnabled: Boolean,
        newBoostProgressBarEnabled: Boolean,
        oldEmoji: List<Emoji>,
        newEmoji: JsonNode?,
        oldFeatures: Set<GuildFeature>,
        newFeatures: JsonNode,
    ) {
        if (oldName != newName) {
            onNameChange(guild, oldName, newName)
        }

        if (oldIcon != newIcon) {
            onIconChange(guild, oldIcon, newIcon)
        }

        if (oldSplash != newSplash) {
            onSplashChange(guild, oldSplash, newSplash)
        }

        if (oldDiscoverySplash != newDiscoverySplash) {
            onDiscoverySplashChange(guild, oldDiscoverySplash, newDiscoverySplash)
        }

        if (oldOwnerId != newOwnerId) {
            onOwnerChange(guild, oldOwnerId, newOwnerId)
        }

        if (oldPermissions != newPermissions) {
            onPermissionsChange(guild, oldPermissions, newPermissions)
        }

        if (oldAfkChannelId != newAfkChannelId) {
            onAfkChannelChange(guild, oldAfkChannelId, newAfkChannelId)
        }

        if (oldAfkTimeout != newAfkTimeout) {
            onAfkTimeoutChange(guild, oldAfkTimeout, newAfkTimeout)
        }

        if (oldWidgetEnabled != newWidgetEnabled) {
            onWidgetEnabledChange(guild, oldWidgetEnabled, newWidgetEnabled)
        }

        if (oldWidgetChannelId != newWidgetChannelId) {
            onWidgetChannelChange(guild, oldWidgetChannelId, newWidgetChannelId)
        }

        if (oldVerificationLevel != newVerificationLevel) {
            onVerificationLevelChange(guild, oldVerificationLevel, newVerificationLevel)
        }

        if (oldDefaultMessageNotifications != newDefaultMessageNotifications) {
            onDefaultMessageNotificationsChange(
                guild, oldDefaultMessageNotifications, newDefaultMessageNotifications)
        }

        if (oldExplicitContentFilter != newExplicitContentFilter) {
            onExplicitContentFilterChange(guild, oldExplicitContentFilter, newExplicitContentFilter)
        }

        if (oldMfaLevel != newMfaLevel) {
            onMfaLevelChange(guild, oldMfaLevel, newMfaLevel)
        }

        if (oldApplicationId != newApplicationId) {
            onApplicationIdChange(guild, oldApplicationId, newApplicationId)
        }

        if (oldSystemChannelId != newSystemChannelId) {
            onSystemChannelChange(guild, oldSystemChannelId, newSystemChannelId)
        }

        if (oldSystemChannelFlags != newSystemChannelFlags) {
            onSystemChannelFlagsChange(guild, oldSystemChannelFlags, newSystemChannelFlags)
        }

        if (oldRulesChannelId != newRulesChannelId) {
            onRulesChannelChange(guild, oldRulesChannelId, newRulesChannelId)
        }

        if (oldMaxPresences != newMaxPresences) {
            onMaxPresencesChange(guild, oldMaxPresences, newMaxPresences)
        }

        if (oldMaxMembers != newMaxMembers) {
            onMaxMembersChange(guild, oldMaxMembers, newMaxMembers)
        }

        if (oldVanityUrlCode != newVanityUrlCode) {
            onVanityUrlCodeChange(guild, oldVanityUrlCode, newVanityUrlCode)
        }

        if (oldDescription != newDescription) {
            onDescriptionChange(guild, oldDescription, newDescription)
        }

        if (oldBanner != newBanner) {
            onBannerChange(guild, oldBanner, newBanner)
        }

        if (oldPremiumTier != newPremiumTier) {
            onPremiumTierChange(guild, oldPremiumTier, newPremiumTier)
        }

        if (oldPremiumSubscriptionCount != newPremiumSubscriptionCount) {
            onPremiumSubscriptionCountChange(
                guild, oldPremiumSubscriptionCount, newPremiumSubscriptionCount)
        }

        if (oldPreferredLocale != newPreferredLocale) {
            onPreferredLocaleChange(guild, oldPreferredLocale, newPreferredLocale)
        }

        if (oldPublicUpdatesChannelId != newPublicUpdatesChannelId) {
            onPublicUpdatesChannelChange(
                guild, oldPublicUpdatesChannelId, newPublicUpdatesChannelId)
        }

        if (oldMaxVideoChannelUsers != newMaxVideoChannelUsers) {
            onMaxVideoChannelUsersChange(guild, oldMaxVideoChannelUsers, newMaxVideoChannelUsers)
        }

        if (oldApproximateMemberCount != newApproximateMemberCount) {
            onApproximateMemberCountChange(
                guild, oldApproximateMemberCount, newApproximateMemberCount)
        }

        if (oldApproximatePresenceCount != newApproximatePresenceCount) {
            onApproximatePresenceCountChange(
                guild, oldApproximatePresenceCount, newApproximatePresenceCount)
        }

        if (oldWelcomeScreen != newWelcomeScreen) {
            onWelcomeScreenChange(guild, oldWelcomeScreen, newWelcomeScreen)
        }

        if (oldNSFWLevel != newNSFWLevel) {
            onNSFWLevelChange(guild, oldNSFWLevel, newNSFWLevel)
        }

        if (oldStickers != newStickers) {
            onStickersChange(guild, oldStickers, newStickers)
        }

        if (wasBoostProgressBarEnabled != newBoostProgressBarEnabled) {
            onBoostProgressBarEnabledChange(
                guild, wasBoostProgressBarEnabled, newBoostProgressBarEnabled)
        }

        if (oldEmoji != newEmoji) {
            onEmojiChange(guild, oldEmoji, newEmoji)
        }

        if (oldFeatures != newFeatures) {
            onFeaturesChange(guild, oldFeatures, newFeatures)
        }
    }

    private fun onNameChange(guild: Guild, oldName: String, newName: String) {
        guild.name = newName
        ydwk.emitEvent(GuildNameUpdateEvent(ydwk, guild, oldName, newName))
        logger.debug("Guild name changed from $oldName to $newName")
    }

    private fun onIconChange(guild: Guild, oldIcon: String?, newIcon: String?) {
        guild.icon = newIcon
        ydwk.emitEvent(GuildIconUpdateEvent(ydwk, guild, oldIcon, newIcon))
        logger.debug("Guild icon changed from $oldIcon to $newIcon")
    }

    private fun onSplashChange(guild: Guild, oldSplash: String?, newSplash: String?) {
        guild.splash = newSplash
        ydwk.emitEvent(GuildSplashUpdateEvent(ydwk, guild, oldSplash, newSplash))
        logger.debug("Guild splash changed from $oldSplash to $newSplash")
    }

    private fun onDiscoverySplashChange(
        guild: Guild,
        oldDiscoverySplash: String?,
        newDiscoverySplash: String?,
    ) {
        guild.discoverySplash = newDiscoverySplash
        ydwk.emitEvent(
            GuildDiscoverySplashUpdateEvent(ydwk, guild, oldDiscoverySplash, newDiscoverySplash))
        logger.debug(
            "Guild discovery splash changed from $oldDiscoverySplash to $newDiscoverySplash")
    }

    private fun onOwnerChange(guild: Guild, oldOwnerId: Long, newOwnerId: Long) {
        guild.ownerId = GetterSnowFlake.of(newOwnerId)
        ydwk.emitEvent(GuildOwnerUpdateEvent(ydwk, guild, oldOwnerId, newOwnerId))
        logger.debug("Guild owner changed from $oldOwnerId to $newOwnerId")
    }

    private fun onPermissionsChange(
        guild: Guild,
        oldPermissions: String?,
        newPermissions: String?,
    ) {
        guild.permissions = newPermissions
        ydwk.emitEvent(GuildPermissionsUpdateEvent(ydwk, guild, oldPermissions, newPermissions))
        logger.debug("Guild permissions changed from $oldPermissions to $newPermissions")
    }

    private fun onAfkChannelChange(
        guild: Guild,
        oldAfkChannelId: Long?,
        newAfkChannelId: Long?,
    ) {
        guild.afkChannelId = newAfkChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(GuildAfkChannelUpdateEvent(ydwk, guild, oldAfkChannelId, newAfkChannelId))
        logger.debug("Guild afk channel changed from $oldAfkChannelId to $newAfkChannelId")
    }

    private fun onAfkTimeoutChange(guild: Guild, oldAfkTimeout: Int, newAfkTimeout: Int) {
        guild.afkTimeout = newAfkTimeout
        ydwk.emitEvent(GuildAfkTimeoutUpdateEvent(ydwk, guild, oldAfkTimeout, newAfkTimeout))
        logger.debug("Guild afk timeout changed from $oldAfkTimeout to $newAfkTimeout")
    }

    private fun onWidgetEnabledChange(
        guild: Guild,
        wasWidgetEnabled: Boolean?,
        isWidgetEnabled: Boolean?,
    ) {
        guild.isWidgetEnabled = isWidgetEnabled
        ydwk.emitEvent(
            GuildWidgetEnabledUpdateEvent(ydwk, guild, wasWidgetEnabled, isWidgetEnabled))
        logger.debug("Guild widget enabled changed from $wasWidgetEnabled to $isWidgetEnabled")
    }

    private fun onWidgetChannelChange(
        guild: Guild,
        oldWidgetChannelId: Long?,
        newWidgetChannelId: Long?,
    ) {
        guild.widgetChannelId = newWidgetChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildWidgetChannelUpdateEvent(ydwk, guild, oldWidgetChannelId, newWidgetChannelId))
        logger.debug("Guild widget channel changed from $oldWidgetChannelId to $newWidgetChannelId")
    }

    private fun onVerificationLevelChange(
        guild: Guild,
        oldVerificationLevel: Int,
        newVerificationLevel: Int,
    ) {
        guild.verificationLevel = VerificationLevel.getValue(newVerificationLevel)
        ydwk.emitEvent(
            GuildVerificationLevelUpdateEvent(
                ydwk,
                guild,
                oldVerificationLevel,
                VerificationLevel.getValue(newVerificationLevel)))
        logger.debug(
            "Guild verification level changed from $oldVerificationLevel to $newVerificationLevel")
    }

    private fun onDefaultMessageNotificationsChange(
        guild: Guild,
        oldDefaultMessageNotifications: Int,
        newDefaultMessageNotifications: Int,
    ) {
        guild.defaultMessageNotificationsLevel =
            MessageNotificationLevel.getValue(newDefaultMessageNotifications)
        ydwk.emitEvent(
            GuildDefaultMessageNotificationLevelUpdateEvent(
                ydwk,
                guild,
                MessageNotificationLevel.getValue(oldDefaultMessageNotifications),
                MessageNotificationLevel.getValue(newDefaultMessageNotifications)))
        logger.debug(
            "Guild default message notifications changed from $oldDefaultMessageNotifications to $newDefaultMessageNotifications")
    }

    private fun onExplicitContentFilterChange(
        guild: Guild,
        oldExplicitContentFilter: Int,
        newExplicitContentFilter: Int,
    ) {
        guild.explicitContentFilterLevel =
            ExplicitContentFilterLevel.getValue(newExplicitContentFilter)
        ydwk.emitEvent(
            GuildExplicitContentFilterLevelUpdateEvent(
                ydwk,
                guild,
                ExplicitContentFilterLevel.getValue(oldExplicitContentFilter),
                ExplicitContentFilterLevel.getValue(newExplicitContentFilter)))
        logger.debug(
            "Guild explicit content filter changed from $oldExplicitContentFilter to $newExplicitContentFilter")
    }

    private fun onMfaLevelChange(guild: Guild, oldMfaLevel: Int, newMfaLevel: Int) {
        guild.mfaLevel = MFALevel.getValue(newMfaLevel)
        ydwk.emitEvent(
            GuildMfaLevelUpdateEvent(
                ydwk, guild, MFALevel.getValue(oldMfaLevel), MFALevel.getValue(newMfaLevel)))
        logger.debug("Guild mfa level changed from $oldMfaLevel to $newMfaLevel")
    }

    private fun onApplicationIdChange(
        guild: Guild,
        oldApplicationId: Long?,
        newApplicationId: Long?,
    ) {
        guild.applicationId = newApplicationId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildApplicationIdUpdateEvent(ydwk, guild, oldApplicationId, newApplicationId))
        logger.debug("Guild application id changed from $oldApplicationId to $newApplicationId")
    }

    private fun onSystemChannelChange(
        guild: Guild,
        oldSystemChannelId: Long?,
        newSystemChannelId: Long?,
    ) {
        guild.systemChannelId = newSystemChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildSystemChannelUpdateEvent(ydwk, guild, oldSystemChannelId, newSystemChannelId))
        logger.debug("Guild system channel changed from $oldSystemChannelId to $newSystemChannelId")
    }

    private fun onSystemChannelFlagsChange(
        guild: Guild,
        oldSystemChannelFlags: Int,
        newSystemChannelFlags: Int,
    ) {
        guild.systemChannelFlags = SystemChannelFlag.getValue(newSystemChannelFlags)
        ydwk.emitEvent(
            GuildSystemChannelFlagsUpdateEvent(
                ydwk,
                guild,
                SystemChannelFlag.getValue(oldSystemChannelFlags),
                SystemChannelFlag.getValue(newSystemChannelFlags)))
        logger.debug(
            "Guild system channel flags changed from $oldSystemChannelFlags to $newSystemChannelFlags")
    }

    private fun onRulesChannelChange(
        guild: Guild,
        oldRulesChannelId: Long?,
        newRulesChannelId: Long?,
    ) {
        guild.rulesChannelId = newRulesChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildRulesChannelUpdateEvent(ydwk, guild, oldRulesChannelId, newRulesChannelId))
        logger.debug("Guild rules channel changed from $oldRulesChannelId to $newRulesChannelId")
    }

    private fun onMaxPresencesChange(
        guild: Guild,
        oldMaxPresences: Int?,
        newMaxPresences: Int?,
    ) {
        guild.maxPresences = newMaxPresences
        ydwk.emitEvent(GuildMaxPresencesUpdateEvent(ydwk, guild, oldMaxPresences, newMaxPresences))
        logger.debug("Guild max presences changed from $oldMaxPresences to $newMaxPresences")
    }

    private fun onMaxMembersChange(guild: Guild, oldMaxMembers: Int, newMaxMembers: Int) {
        guild.maxMembers = newMaxMembers
        ydwk.emitEvent(GuildMaxMembersUpdateEvent(ydwk, guild, oldMaxMembers, newMaxMembers))
        logger.debug("Guild max members changed from $oldMaxMembers to $newMaxMembers")
    }

    private fun onVanityUrlCodeChange(
        guild: Guild,
        oldVanityUrlCode: String?,
        newVanityUrlCode: String?,
    ) {
        guild.vanityUrlCode = newVanityUrlCode
        ydwk.emitEvent(GuildVanityUrlUpdateEvent(ydwk, guild, oldVanityUrlCode, newVanityUrlCode))
        logger.debug("Guild vanity url code changed from $oldVanityUrlCode to $newVanityUrlCode")
    }

    private fun onDescriptionChange(
        guild: Guild,
        oldDescription: String?,
        newDescription: String?,
    ) {
        guild.description = newDescription
        ydwk.emitEvent(GuildDescriptionUpdateEvent(ydwk, guild, oldDescription, newDescription))
        logger.debug("Guild description changed from $oldDescription to $newDescription")
    }

    private fun onBannerChange(guild: Guild, oldBanner: String?, newBanner: String?) {
        guild.banner = newBanner
        ydwk.emitEvent(GuildBannerUpdateEvent(ydwk, guild, oldBanner, newBanner))
        logger.debug("Guild banner changed from $oldBanner to $newBanner")
    }

    private fun onPremiumTierChange(guild: Guild, oldPremiumTier: Int, newPremiumTier: Int) {
        guild.premiumTier = PremiumTier.getValue(newPremiumTier)
        ydwk.emitEvent(GuildPremiumTierUpdateEvent(ydwk, guild, oldPremiumTier, newPremiumTier))
        logger.debug("Guild premium tier changed from $oldPremiumTier to $newPremiumTier")
    }

    private fun onPremiumSubscriptionCountChange(
        guild: Guild,
        oldPremiumSubscriptionCount: Int,
        newPremiumSubscriptionCount: Int,
    ) {
        guild.premiumSubscriptionCount = newPremiumSubscriptionCount
        ydwk.emitEvent(
            GuildPremiumSubscriptionCountUpdateEvent(
                ydwk, guild, oldPremiumSubscriptionCount, newPremiumSubscriptionCount))
        logger.debug(
            "Guild premium subscription count changed from $oldPremiumSubscriptionCount to $newPremiumSubscriptionCount")
    }

    private fun onPreferredLocaleChange(
        guild: Guild,
        oldPreferredLocale: String,
        newPreferredLocale: String,
    ) {
        guild.preferredLocale = newPreferredLocale
        ydwk.emitEvent(
            GuildPreferredLocaleUpdateEvent(ydwk, guild, oldPreferredLocale, newPreferredLocale))
        logger.debug(
            "Guild preferred locale changed from $oldPreferredLocale to $newPreferredLocale")
    }

    private fun onPublicUpdatesChannelChange(
        guild: Guild,
        oldPublicUpdatesChannelId: Long?,
        newPublicUpdatesChannelId: Long?,
    ) {
        guild.publicUpdatesChannelId = newPublicUpdatesChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildPublicUpdatesChannelUpdateEvent(
                ydwk, guild, oldPublicUpdatesChannelId, newPublicUpdatesChannelId))
        logger.debug(
            "Guild public updates channel changed from $oldPublicUpdatesChannelId to $newPublicUpdatesChannelId")
    }

    private fun onMaxVideoChannelUsersChange(
        guild: Guild,
        oldMaxVideoChannelUsers: Int?,
        newMaxVideoChannelUsers: Int?,
    ) {
        guild.maxVideoChannelUsers = newMaxVideoChannelUsers
        ydwk.emitEvent(
            GuildMaxVideoChannelUsersUpdateEvent(
                ydwk, guild, oldMaxVideoChannelUsers, newMaxVideoChannelUsers))
        logger.debug(
            "Guild max video channel users changed from $oldMaxVideoChannelUsers to $newMaxVideoChannelUsers")
    }

    private fun onApproximateMemberCountChange(
        guild: Guild,
        oldApproximateMemberCount: Int?,
        newApproximateMemberCount: Int?,
    ) {
        guild.approximateMemberCount = newApproximateMemberCount
        ydwk.emitEvent(
            GuildApproximateMemberCountUpdateEvent(
                ydwk, guild, oldApproximateMemberCount, newApproximateMemberCount))
        logger.debug(
            "Guild approximate member count changed from $oldApproximateMemberCount to $newApproximateMemberCount")
    }

    private fun onApproximatePresenceCountChange(
        guild: Guild,
        oldApproximatePresenceCount: Int?,
        newApproximatePresenceCount: Int?,
    ) {
        guild.approximatePresenceCount = newApproximatePresenceCount
        ydwk.emitEvent(
            GuildApproximatePresenceCountUpdateEvent(
                ydwk, guild, oldApproximatePresenceCount, newApproximatePresenceCount))
        logger.debug(
            "Guild approximate presence count changed from $oldApproximatePresenceCount to $newApproximatePresenceCount")
    }

    private fun onWelcomeScreenChange(
        guild: Guild,
        oldWelcomeScreen: JsonNode?,
        newWelcomeScreen: JsonNode?,
    ) {
        guild.welcomeScreen =
            newWelcomeScreen?.let { ydwk.entityInstanceBuilder.buildWelcomeScreen(it) }
        ydwk.emitEvent(
            GuildWelcomeScreenUpdateEvent(
                ydwk,
                guild,
                oldWelcomeScreen?.let { ydwk.entityInstanceBuilder.buildWelcomeScreen(it) },
                newWelcomeScreen?.let { ydwk.entityInstanceBuilder.buildWelcomeScreen(it) }))
        logger.debug(
            "Guild welcome screen changed from {} to {}", oldWelcomeScreen, newWelcomeScreen)
    }

    private fun onNSFWLevelChange(guild: Guild, oldNSFWLevel: Int, newNSFWLevel: Int) {
        guild.nsfwLevel = NSFWLeveL.getValue(newNSFWLevel)
        ydwk.emitEvent(
            GuildNSFWLevelUpdateEvent(ydwk, guild, oldNSFWLevel, NSFWLeveL.getValue(newNSFWLevel)))
        logger.debug("Guild NSFW level changed from $oldNSFWLevel to $newNSFWLevel")
    }

    private fun onStickersChange(
        guild: Guild,
        oldStickers: List<Sticker>,
        newStickers: JsonNode?,
    ) {
        guild.stickers =
            newStickers?.let { it -> it.map { ydwk.entityInstanceBuilder.buildSticker(it) } }
                ?: emptyList()
        ydwk.emitEvent(
            GuildStickersUpdateEvent(
                ydwk,
                guild,
                oldStickers,
                newStickers?.let { it -> it.map { ydwk.entityInstanceBuilder.buildSticker(it) } }
                    ?: emptyList()))
        logger.debug("Guild stickers changed from {} to {}", oldStickers, newStickers)
    }

    private fun onBoostProgressBarEnabledChange(
        guild: Guild,
        oldBoostProgressBarEnabled: Boolean,
        newBoostProgressBarEnabled: Boolean,
    ) {
        guild.isBoostProgressBarEnabled = newBoostProgressBarEnabled
        ydwk.emitEvent(
            GuildBoostProgressBarEnabledUpdateEvent(
                ydwk, guild, oldBoostProgressBarEnabled, newBoostProgressBarEnabled))
        logger.debug(
            "Guild boost progress bar enabled changed from $oldBoostProgressBarEnabled to $newBoostProgressBarEnabled")
    }

    private fun onEmojiChange(guild: Guild, oldEmojis: List<Emoji>, newEmojis: JsonNode?) {
        guild.emojis =
            newEmojis?.let { it -> it.map { ydwk.entityInstanceBuilder.buildEmoji(it) } }
                ?: emptyList() ?: emptyList()
        ydwk.emitEvent(
            GuildEmojisUpdateEvent(
                ydwk,
                guild,
                oldEmojis,
                newEmojis?.let { it -> it.map { ydwk.entityInstanceBuilder.buildEmoji(it) } }
                    ?: emptyList()))
        logger.debug("Guild emojis changed from {} to {}", oldEmojis, newEmojis)
    }

    private fun onFeaturesChange(
        guild: Guild,
        oldFeatures: Set<GuildFeature>,
        newFeatures: JsonNode?,
    ) {
        guild.features =
            newFeatures?.let { it -> it.map { GuildFeature.getValue(it.asText()) }.toSet() }
                ?: emptySet()
        ydwk.emitEvent(
            GuildFeaturesUpdateEvent(
                ydwk,
                guild,
                oldFeatures,
                newFeatures?.let { it -> it.map { GuildFeature.getValue(it.asText()) }.toSet() }
                    ?: emptySet()))
        logger.debug("Guild features changed from {} to {}", oldFeatures, newFeatures)
    }
}
