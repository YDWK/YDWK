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
package io.github.ydwk.ydwk.evm.handler.handlers.guild.update

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.entities.Emoji
import io.github.ydwk.ydwk.entities.guild.enums.GuildFeature
import io.github.ydwk.ydwk.evm.event.events.guild.update.GuildIconUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.guild.update.GuildNameUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.guild.update.GuildSplashUpdateEvent
import io.github.ydwk.ydwk.evm.handler.handlers.guild.GuildUpdateHandler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.GuildImpl
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
    oldMaxMembers: Int?,
    newMaxMembers: Int?,
    oldVanityUrlCode: String?,
    newVanityUrlCode: String?,
    oldDescription: String?,
    newDescription: String?,
    oldBanner: String?,
    newBanner: String?,
    oldPremiumTier: Int,
    newPremiumTier: Int,
    oldPremiumSubscriptionCount: Int?,
    newPremiumSubscriptionCount: Int?,
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
    oldStickers: List<Any>?,
    newStickers: JsonNode?,
    wasBoostProgressBarEnabled: Boolean,
    newBoostProgressBarEnabled: Boolean,
    oldEmoji: List<Emoji>,
    newEmoji: JsonNode?,
    oldFeatures: Set<GuildFeature>,
    newFeatures: JsonNode
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
        guild,
        oldDefaultMessageNotifications,
        newDefaultMessageNotifications
      )
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
        guild,
        oldPremiumSubscriptionCount,
        newPremiumSubscriptionCount
      )
    }

    if (oldPreferredLocale != newPreferredLocale) {
      onPreferredLocaleChange(guild, oldPreferredLocale, newPreferredLocale)
    }

    if (oldPublicUpdatesChannelId != newPublicUpdatesChannelId) {
      onPublicUpdatesChannelChange(guild, oldPublicUpdatesChannelId, newPublicUpdatesChannelId)
    }

    if (oldMaxVideoChannelUsers != newMaxVideoChannelUsers) {
      onMaxVideoChannelUsersChange(guild, oldMaxVideoChannelUsers, newMaxVideoChannelUsers)
    }

    if (oldApproximateMemberCount != newApproximateMemberCount) {
      onApproximateMemberCountChange(guild, oldApproximateMemberCount, newApproximateMemberCount)
    }

    if (oldApproximatePresenceCount != newApproximatePresenceCount) {
      onApproximatePresenceCountChange(
        guild,
        oldApproximatePresenceCount,
        newApproximatePresenceCount
      )
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
      onBoostProgressBarEnabledChange(guild, wasBoostProgressBarEnabled, newBoostProgressBarEnabled)
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
}
