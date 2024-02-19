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
import io.github.ydwk.yde.entities.Sticker
import io.github.ydwk.yde.entities.guild.enums.*
import io.github.ydwk.yde.impl.entities.EmojiImpl
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.yde.impl.entities.StickerImpl
import io.github.ydwk.yde.impl.entities.guild.WelcomeScreenImpl
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.guild.update.*
import io.github.ydwk.ydwk.evm.handler.handlers.guild.GuildUpdateHandler
import io.github.ydwk.ydwk.impl.YDWKImpl
import org.slf4j.LoggerFactory

class GuildUpdateHandlerExtended(ydwk: YDWKImpl, json: JsonNode) : GuildUpdateHandler(ydwk, json) {
    private val logger = LoggerFactory.getLogger(GuildUpdateHandlerExtended::class.java)

    fun onUpdate(
        guild: GuildImpl,
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

    private fun onNameChange(guild: GuildImpl, oldName: String, newName: String) {
        guild.name = newName
        ydwk.emitEvent(GuildNameUpdateEvent(ydwk, guild, oldName, newName))
        logger.debug("Guild name changed from $oldName to $newName")
    }

    private fun onIconChange(guild: GuildImpl, oldIcon: String?, newIcon: String?) {
        guild.icon = newIcon
        ydwk.emitEvent(GuildIconUpdateEvent(ydwk, guild, oldIcon, newIcon))
        logger.debug("Guild icon changed from $oldIcon to $newIcon")
    }

    private fun onSplashChange(guild: GuildImpl, oldSplash: String?, newSplash: String?) {
        guild.splash = newSplash
        ydwk.emitEvent(GuildSplashUpdateEvent(ydwk, guild, oldSplash, newSplash))
        logger.debug("Guild splash changed from $oldSplash to $newSplash")
    }

    private fun onDiscoverySplashChange(
        guild: GuildImpl,
        oldDiscoverySplash: String?,
        newDiscoverySplash: String?,
    ) {
        guild.discoverySplash = newDiscoverySplash
        ydwk.emitEvent(
            GuildDiscoverySplashUpdateEvent(ydwk, guild, oldDiscoverySplash, newDiscoverySplash))
        logger.debug(
            "Guild discovery splash changed from $oldDiscoverySplash to $newDiscoverySplash")
    }

    private fun onOwnerChange(guild: GuildImpl, oldOwnerId: Long, newOwnerId: Long) {
        guild.ownerId = GetterSnowFlake.of(newOwnerId)
        ydwk.emitEvent(GuildOwnerUpdateEvent(ydwk, guild, oldOwnerId, newOwnerId))
        logger.debug("Guild owner changed from $oldOwnerId to $newOwnerId")
    }

    private fun onPermissionsChange(
        guild: GuildImpl,
        oldPermissions: String?,
        newPermissions: String?,
    ) {
        guild.permissions = newPermissions
        ydwk.emitEvent(GuildPermissionsUpdateEvent(ydwk, guild, oldPermissions, newPermissions))
        logger.debug("Guild permissions changed from $oldPermissions to $newPermissions")
    }

    private fun onAfkChannelChange(
        guild: GuildImpl,
        oldAfkChannelId: Long?,
        newAfkChannelId: Long?,
    ) {
        guild.afkChannelId = newAfkChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(GuildAfkChannelUpdateEvent(ydwk, guild, oldAfkChannelId, newAfkChannelId))
        logger.debug("Guild afk channel changed from $oldAfkChannelId to $newAfkChannelId")
    }

    private fun onAfkTimeoutChange(guild: GuildImpl, oldAfkTimeout: Int, newAfkTimeout: Int) {
        guild.afkTimeout = newAfkTimeout
        ydwk.emitEvent(GuildAfkTimeoutUpdateEvent(ydwk, guild, oldAfkTimeout, newAfkTimeout))
        logger.debug("Guild afk timeout changed from $oldAfkTimeout to $newAfkTimeout")
    }

    private fun onWidgetEnabledChange(
        guild: GuildImpl,
        wasWidgetEnabled: Boolean?,
        isWidgetEnabled: Boolean?,
    ) {
        guild.isWidgetEnabled = isWidgetEnabled
        ydwk.emitEvent(
            GuildWidgetEnabledUpdateEvent(ydwk, guild, wasWidgetEnabled, isWidgetEnabled))
        logger.debug("Guild widget enabled changed from $wasWidgetEnabled to $isWidgetEnabled")
    }

    private fun onWidgetChannelChange(
        guild: GuildImpl,
        oldWidgetChannelId: Long?,
        newWidgetChannelId: Long?,
    ) {
        guild.widgetChannelId = newWidgetChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildWidgetChannelUpdateEvent(ydwk, guild, oldWidgetChannelId, newWidgetChannelId))
        logger.debug("Guild widget channel changed from $oldWidgetChannelId to $newWidgetChannelId")
    }

    private fun onVerificationLevelChange(
        guild: GuildImpl,
        oldVerificationLevel: Int,
        newVerificationLevel: Int,
    ) {
        guild.verificationLevel = VerificationLevel.fromInt(newVerificationLevel)
        ydwk.emitEvent(
            GuildVerificationLevelUpdateEvent(
                ydwk, guild, oldVerificationLevel, VerificationLevel.fromInt(newVerificationLevel)))
        logger.debug(
            "Guild verification level changed from $oldVerificationLevel to $newVerificationLevel")
    }

    private fun onDefaultMessageNotificationsChange(
        guild: GuildImpl,
        oldDefaultMessageNotifications: Int,
        newDefaultMessageNotifications: Int,
    ) {
        guild.defaultMessageNotificationsLevel =
            MessageNotificationLevel.fromInt(newDefaultMessageNotifications)
        ydwk.emitEvent(
            GuildDefaultMessageNotificationLevelUpdateEvent(
                ydwk,
                guild,
                MessageNotificationLevel.fromInt(oldDefaultMessageNotifications),
                MessageNotificationLevel.fromInt(newDefaultMessageNotifications)))
        logger.debug(
            "Guild default message notifications changed from $oldDefaultMessageNotifications to $newDefaultMessageNotifications")
    }

    private fun onExplicitContentFilterChange(
        guild: GuildImpl,
        oldExplicitContentFilter: Int,
        newExplicitContentFilter: Int,
    ) {
        guild.explicitContentFilterLevel =
            ExplicitContentFilterLevel.fromInt(newExplicitContentFilter)
        ydwk.emitEvent(
            GuildExplicitContentFilterLevelUpdateEvent(
                ydwk,
                guild,
                ExplicitContentFilterLevel.fromInt(oldExplicitContentFilter),
                ExplicitContentFilterLevel.fromInt(newExplicitContentFilter)))
        logger.debug(
            "Guild explicit content filter changed from $oldExplicitContentFilter to $newExplicitContentFilter")
    }

    private fun onMfaLevelChange(guild: GuildImpl, oldMfaLevel: Int, newMfaLevel: Int) {
        guild.mfaLevel = MFALevel.fromInt(newMfaLevel)
        ydwk.emitEvent(
            GuildMfaLevelUpdateEvent(
                ydwk, guild, MFALevel.fromInt(oldMfaLevel), MFALevel.fromInt(newMfaLevel)))
        logger.debug("Guild mfa level changed from $oldMfaLevel to $newMfaLevel")
    }

    private fun onApplicationIdChange(
        guild: GuildImpl,
        oldApplicationId: Long?,
        newApplicationId: Long?,
    ) {
        guild.applicationId = newApplicationId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildApplicationIdUpdateEvent(ydwk, guild, oldApplicationId, newApplicationId))
        logger.debug("Guild application id changed from $oldApplicationId to $newApplicationId")
    }

    private fun onSystemChannelChange(
        guild: GuildImpl,
        oldSystemChannelId: Long?,
        newSystemChannelId: Long?,
    ) {
        guild.systemChannelId = newSystemChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildSystemChannelUpdateEvent(ydwk, guild, oldSystemChannelId, newSystemChannelId))
        logger.debug("Guild system channel changed from $oldSystemChannelId to $newSystemChannelId")
    }

    private fun onSystemChannelFlagsChange(
        guild: GuildImpl,
        oldSystemChannelFlags: Int,
        newSystemChannelFlags: Int,
    ) {
        guild.systemChannelFlags = SystemChannelFlag.fromInt(newSystemChannelFlags)
        ydwk.emitEvent(
            GuildSystemChannelFlagsUpdateEvent(
                ydwk,
                guild,
                SystemChannelFlag.fromInt(oldSystemChannelFlags),
                SystemChannelFlag.fromInt(newSystemChannelFlags)))
        logger.debug(
            "Guild system channel flags changed from $oldSystemChannelFlags to $newSystemChannelFlags")
    }

    private fun onRulesChannelChange(
        guild: GuildImpl,
        oldRulesChannelId: Long?,
        newRulesChannelId: Long?,
    ) {
        guild.rulesChannelId = newRulesChannelId?.let { GetterSnowFlake.of(it) }
        ydwk.emitEvent(
            GuildRulesChannelUpdateEvent(ydwk, guild, oldRulesChannelId, newRulesChannelId))
        logger.debug("Guild rules channel changed from $oldRulesChannelId to $newRulesChannelId")
    }

    private fun onMaxPresencesChange(
        guild: GuildImpl,
        oldMaxPresences: Int?,
        newMaxPresences: Int?,
    ) {
        guild.maxPresences = newMaxPresences
        ydwk.emitEvent(GuildMaxPresencesUpdateEvent(ydwk, guild, oldMaxPresences, newMaxPresences))
        logger.debug("Guild max presences changed from $oldMaxPresences to $newMaxPresences")
    }

    private fun onMaxMembersChange(guild: GuildImpl, oldMaxMembers: Int, newMaxMembers: Int) {
        guild.maxMembers = newMaxMembers
        ydwk.emitEvent(GuildMaxMembersUpdateEvent(ydwk, guild, oldMaxMembers, newMaxMembers))
        logger.debug("Guild max members changed from $oldMaxMembers to $newMaxMembers")
    }

    private fun onVanityUrlCodeChange(
        guild: GuildImpl,
        oldVanityUrlCode: String?,
        newVanityUrlCode: String?,
    ) {
        guild.vanityUrlCode = newVanityUrlCode
        ydwk.emitEvent(GuildVanityUrlUpdateEvent(ydwk, guild, oldVanityUrlCode, newVanityUrlCode))
        logger.debug("Guild vanity url code changed from $oldVanityUrlCode to $newVanityUrlCode")
    }

    private fun onDescriptionChange(
        guild: GuildImpl,
        oldDescription: String?,
        newDescription: String?,
    ) {
        guild.description = newDescription
        ydwk.emitEvent(GuildDescriptionUpdateEvent(ydwk, guild, oldDescription, newDescription))
        logger.debug("Guild description changed from $oldDescription to $newDescription")
    }

    private fun onBannerChange(guild: GuildImpl, oldBanner: String?, newBanner: String?) {
        guild.banner = newBanner
        ydwk.emitEvent(GuildBannerUpdateEvent(ydwk, guild, oldBanner, newBanner))
        logger.debug("Guild banner changed from $oldBanner to $newBanner")
    }

    private fun onPremiumTierChange(guild: GuildImpl, oldPremiumTier: Int, newPremiumTier: Int) {
        guild.premiumTier = PremiumTier.fromInt(newPremiumTier)
        ydwk.emitEvent(GuildPremiumTierUpdateEvent(ydwk, guild, oldPremiumTier, newPremiumTier))
        logger.debug("Guild premium tier changed from $oldPremiumTier to $newPremiumTier")
    }

    private fun onPremiumSubscriptionCountChange(
        guild: GuildImpl,
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
        guild: GuildImpl,
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
        guild: GuildImpl,
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
        guild: GuildImpl,
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
        guild: GuildImpl,
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
        guild: GuildImpl,
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
        guild: GuildImpl,
        oldWelcomeScreen: JsonNode?,
        newWelcomeScreen: JsonNode?,
    ) {
        guild.welcomeScreen = newWelcomeScreen?.let { WelcomeScreenImpl(ydwk, it) }
        ydwk.emitEvent(
            GuildWelcomeScreenUpdateEvent(
                ydwk,
                guild,
                oldWelcomeScreen?.let { WelcomeScreenImpl(ydwk, it) },
                newWelcomeScreen?.let { WelcomeScreenImpl(ydwk, it) }))
        logger.debug("Guild welcome screen changed from $oldWelcomeScreen to $newWelcomeScreen")
    }

    private fun onNSFWLevelChange(guild: GuildImpl, oldNSFWLevel: Int, newNSFWLevel: Int) {
        guild.nsfwLevel = NSFWLeveL.fromInt(newNSFWLevel)
        ydwk.emitEvent(
            GuildNSFWLevelUpdateEvent(ydwk, guild, oldNSFWLevel, NSFWLeveL.fromInt(newNSFWLevel)))
        logger.debug("Guild NSFW level changed from $oldNSFWLevel to $newNSFWLevel")
    }

    private fun onStickersChange(
        guild: GuildImpl,
        oldStickers: List<Sticker>,
        newStickers: JsonNode?,
    ) {
        guild.stickers =
            newStickers?.let { it -> it.map { StickerImpl(ydwk, it, it["id"].asLong()) } }
                ?: emptyList()
        ydwk.emitEvent(
            GuildStickersUpdateEvent(
                ydwk,
                guild,
                oldStickers,
                newStickers?.let { it -> it.map { StickerImpl(ydwk, it, it["id"].asLong()) } }
                    ?: emptyList()))
        logger.debug("Guild stickers changed from $oldStickers to $newStickers")
    }

    private fun onBoostProgressBarEnabledChange(
        guild: GuildImpl,
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

    private fun onEmojiChange(guild: GuildImpl, oldEmojis: List<Emoji>, newEmojis: JsonNode?) {
        guild.emojis = newEmojis?.let { it -> it.map { EmojiImpl(ydwk, it) } } ?: emptyList()
        ydwk.emitEvent(
            GuildEmojisUpdateEvent(
                ydwk,
                guild,
                oldEmojis,
                newEmojis?.let { it -> it.map { EmojiImpl(ydwk, it) } } ?: emptyList()))
        logger.debug("Guild emojis changed from $oldEmojis to $newEmojis")
    }

    private fun onFeaturesChange(
        guild: GuildImpl,
        oldFeatures: Set<GuildFeature>,
        newFeatures: JsonNode?,
    ) {
        guild.features =
            newFeatures?.let { it -> it.map { GuildFeature.fromString(it.asText()) }.toSet() }
                ?: emptySet()
        ydwk.emitEvent(
            GuildFeaturesUpdateEvent(
                ydwk,
                guild,
                oldFeatures,
                newFeatures?.let { it -> it.map { GuildFeature.fromString(it.asText()) }.toSet() }
                    ?: emptySet()))
        logger.debug("Guild features changed from $oldFeatures to $newFeatures")
    }
}
