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
package io.github.ydwk.ydwk.evm.handler.handlers.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.guild.enums.*
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.EmojiImpl
import io.github.ydwk.ydwk.impl.entities.StickerImpl
import io.github.ydwk.ydwk.impl.entities.guild.WelcomeScreenImpl
import io.github.ydwk.ydwk.util.GetterSnowFlake
import java.util.*

class GuildUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val guild = ydwk.getGuildById(json["id"].asLong()) ?: return

        if (!ydwk.cache.contains(guild.id, CacheIds.GUILD)) {
            ydwk.logger.warn("GuildUpdateHandler: Guild ${guild.id} is not cached, will add it")
            ydwk.cache[guild.id, guild] = CacheIds.GUILD
        }

        val oldName = guild.name
        val oldIcon = guild.icon
        val oldSplash = guild.splash
        val oldDiscoverySplash = guild.discoverySplash
        val oldOwnerId = guild.ownerId
        val oldPermissions = guild.permissions
        val oldAfkChannelId = guild.afkChannelId
        val oldAfkTimeout = guild.afkTimeout
        val oldWidgetEnabled = guild.isWidgetEnabled
        val oldWidgetChannelId = guild.widgetChannelId
        val oldVerificationLevel = guild.verificationLevel
        val oldDefaultMessageNotifications = guild.defaultMessageNotificationsLevel.value
        val oldExplicitContentFilter = guild.explicitContentFilterLevel.value
        val oldMfaLevel = guild.mfaLevel.value
        val oldApplicationId = guild.applicationId
        val oldSystemChannelId = guild.systemChannelId
        val oldSystemChannelFlags = guild.systemChannelFlags.value
        val oldRulesChannelId = guild.rulesChannelId
        val oldMaxPresences = guild.maxPresences
        val oldMaxMembers = guild.maxMembers
        val oldVanityUrlCode = guild.vanityUrlCode
        val oldDescription = guild.description
        val oldBanner = guild.banner
        val oldPremiumTier = guild.premiumTier
        val oldPremiumSubscriptionCount = guild.premiumSubscriptionCount
        val oldPreferredLocale = guild.preferredLocale
        val oldPublicUpdatesChannelId = guild.publicUpdatesChannelId
        val oldMaxVideoChannelUsers = guild.maxVideoChannelUsers
        val oldApproximateMemberCount = guild.approximateMemberCount
        val oldApproximatePresenceCount = guild.approximatePresenceCount
        val oldWelcomeScreen = guild.welcomeScreen
        val oldNSFWLevel = guild.nsfwLevel.level
        val oldStickers = guild.stickers
        val wasBoostProgressBarEnabled = guild.isBoostProgressBarEnabled
        val oldEmoji = guild.emojis
        val oldFeatures = guild.features

        val newName = json["name"].asText()
        val newIcon = json["icon"].asText()
        val newSplash = json["splash"].asText()
        val newDiscoverySplash = json["discovery_splash"].asText()
        val newOwnerId = json["owner_id"].asLong()
        val newPermissions = json["permissions"].asText()
        val newAfkChannelId = json["afk_channel_id"].asLong()
        val newAfkTimeout = json["afk_timeout"].asInt()
        val newWidgetEnabled = json["widget_enabled"].asBoolean()
        val newWidgetChannelId = json["widget_channel_id"].asLong()
        val newVerificationLevel = json["verification_level"].asInt()
        val newDefaultMessageNotifications = json["default_message_notifications"].asInt()
        val newExplicitContentFilter = json["explicit_content_filter"].asInt()
        val newMfaLevel = json["mfa_level"].asInt()
        val newApplicationId = json["application_id"].asLong()
        val newSystemChannelId = json["system_channel_id"].asLong()
        val newSystemChannelFlags = json["system_channel_flags"].asInt()
        val newRulesChannelId = json["rules_channel_id"].asLong()
        val newMaxPresences = json["max_presences"].asInt()
        val newMaxMembers = json["max_members"].asInt()
        val newVanityUrlCode = json["vanity_url_code"].asText()
        val newDescription = json["description"].asText()
        val newBanner = json["banner"].asText()
        val newPremiumTier = json["premium_tier"].asInt()
        val newPremiumSubscriptionCount = json["premium_subscription_count"].asInt()
        val newPreferredLocale = json["preferred_locale"].asText()
        val newPublicUpdatesChannelId = json["public_updates_channel_id"].asLong()
        val newMaxVideoChannelUsers = json["max_video_channel_users"].asInt()
        val newApproximateMemberCount = json["approximate_member_count"].asInt()
        val newApproximatePresenceCount = json["approximate_presence_count"].asInt()
        val newWelcomeScreen = json["welcome_screen"]
        val newNSFWLevel = json["nsfw_level"].asInt()
        val newStickers = json["stickers"]
        val newBoostProgressBarEnabled = json["premium_progress_bar_enabled"].asBoolean()
        val newEmoji = json["emojis"]
        val newFeatures = json["features"]

        when {
            !Objects.deepEquals(oldName, newName) -> guild.name = newName
            if (oldIcon != null) !Objects.deepEquals(oldIcon, newIcon) else newIcon != null ->
                guild.icon = newIcon
            if (oldSplash != null) !Objects.deepEquals(oldSplash, newSplash)
            else newSplash != null -> guild.splash = newSplash
            if (oldDiscoverySplash != null)
                !Objects.deepEquals(oldDiscoverySplash, newDiscoverySplash)
            else newDiscoverySplash != null -> guild.discoverySplash = newDiscoverySplash
            oldOwnerId.asLong != newOwnerId -> guild.ownerId = GetterSnowFlake.of(newOwnerId)
            !Objects.deepEquals(oldPermissions, newPermissions) ->
                guild.permissions = newPermissions
            if (oldAfkChannelId != null) oldAfkChannelId.asLong != newAfkChannelId
            else newAfkChannelId != 0L -> guild.afkChannelId = GetterSnowFlake.of(newAfkChannelId)
            oldAfkTimeout != newAfkTimeout -> guild.afkTimeout = newAfkTimeout
            oldWidgetEnabled != newWidgetEnabled -> guild.isWidgetEnabled = newWidgetEnabled
            if (oldWidgetChannelId != null) oldWidgetChannelId.asLong != newWidgetChannelId
            else newWidgetChannelId != 0L ->
                guild.widgetChannelId = GetterSnowFlake.of(newWidgetChannelId)
            oldVerificationLevel.level != newVerificationLevel ->
                guild.verificationLevel = VerificationLevel.fromLevel(newVerificationLevel)
            oldDefaultMessageNotifications != newDefaultMessageNotifications ->
                guild.defaultMessageNotificationsLevel =
                    MessageNotificationLevel.fromValue(newDefaultMessageNotifications)
            oldExplicitContentFilter != newExplicitContentFilter ->
                guild.explicitContentFilterLevel =
                    ExplicitContentFilterLevel.fromValue(newExplicitContentFilter)
            oldMfaLevel != newMfaLevel -> guild.mfaLevel = MFALevel.fromValue(newMfaLevel)
            if (oldApplicationId != null) oldApplicationId.asLong != newApplicationId
            else newApplicationId != 0L ->
                guild.applicationId = GetterSnowFlake.of(newApplicationId)
            if (oldSystemChannelId != null) oldSystemChannelId.asLong != newSystemChannelId
            else newSystemChannelId != 0L ->
                guild.systemChannelId = GetterSnowFlake.of(newSystemChannelId)
            oldSystemChannelFlags != newSystemChannelFlags ->
                guild.systemChannelFlags = SystemChannelFlag.fromValue(newSystemChannelFlags)
            if (oldRulesChannelId != null) oldRulesChannelId.asLong != newRulesChannelId
            else newRulesChannelId != 0L ->
                guild.rulesChannelId = GetterSnowFlake.of(newRulesChannelId)
            if (oldMaxPresences != null) oldMaxPresences != newMaxPresences
            else newMaxPresences != 0 -> guild.maxPresences = newMaxPresences
            oldMaxMembers != newMaxMembers -> guild.maxMembers = newMaxMembers
            if (oldVanityUrlCode != null) !Objects.deepEquals(oldVanityUrlCode, newVanityUrlCode)
            else newVanityUrlCode != null -> guild.vanityUrlCode = newVanityUrlCode
            if (oldDescription != null) !Objects.deepEquals(oldDescription, newDescription)
            else newDescription != null -> guild.description = newDescription
            if (oldBanner != null) !Objects.deepEquals(oldBanner, newBanner)
            else newBanner != null -> guild.banner = newBanner
            oldPremiumTier.value != newPremiumTier ->
                guild.premiumTier = PremiumTier.fromValue(newPremiumTier)
            oldPremiumSubscriptionCount != newPremiumSubscriptionCount ->
                guild.premiumSubscriptionCount = newPremiumSubscriptionCount
            oldPreferredLocale != newPreferredLocale -> guild.preferredLocale = newPreferredLocale
            if (oldPublicUpdatesChannelId != null)
                oldPublicUpdatesChannelId.asLong != newPublicUpdatesChannelId
            else newPublicUpdatesChannelId != 0L ->
                guild.publicUpdatesChannelId = GetterSnowFlake.of(newPublicUpdatesChannelId)
            if (oldMaxVideoChannelUsers != null) oldMaxVideoChannelUsers != newMaxVideoChannelUsers
            else newMaxVideoChannelUsers != 0 ->
                guild.maxVideoChannelUsers = newMaxVideoChannelUsers
            if (oldApproximateMemberCount != null)
                oldApproximateMemberCount != newApproximateMemberCount
            else newApproximateMemberCount != 0 ->
                guild.approximateMemberCount = newApproximateMemberCount
            if (oldApproximatePresenceCount != null)
                oldApproximatePresenceCount != newApproximatePresenceCount
            else newApproximatePresenceCount != 0 ->
                guild.approximatePresenceCount = newApproximatePresenceCount
            if (oldWelcomeScreen != null) !Objects.deepEquals(oldWelcomeScreen, newWelcomeScreen)
            else newWelcomeScreen != null ->
                guild.welcomeScreen = WelcomeScreenImpl(ydwk, newWelcomeScreen)
            oldNSFWLevel != newNSFWLevel -> guild.nsfwLevel = NSFWLeveL.fromValue(newNSFWLevel)
            oldStickers != newStickers ->
                guild.stickers =
                    newStickers.map { it: JsonNode -> StickerImpl(ydwk, it, it["id"].asLong()) }
            wasBoostProgressBarEnabled != newBoostProgressBarEnabled ->
                guild.isBoostProgressBarEnabled = newBoostProgressBarEnabled
            oldEmoji != newEmoji ->
                guild.emojis = newEmoji.map { it: JsonNode -> EmojiImpl(ydwk, it) }
            oldFeatures != newFeatures ->
                guild.features =
                    newFeatures.map { it: JsonNode -> GuildFeature.fromString(it.asText()) }.toSet()
        }
    }
}
