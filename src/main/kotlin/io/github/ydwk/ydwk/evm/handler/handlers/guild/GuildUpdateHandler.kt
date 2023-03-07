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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.evm.handler.handlers.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.impl.YDWKImpl
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.evm.handler.handlers.guild.update.GuildUpdateHandlerExtended

open class GuildUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val guild: GuildImpl? = ydwk.getGuildById(json["id"].asLong()) as GuildImpl?

        if (guild == null) {
            ydwk.logger.warn("GuildUpdateHandler: Guild ${json["id"].asLong()} not found in cache")
            ydwk.cache[json["id"].asText(), GuildImpl(ydwk, json, json["id"].asLong())] =
                CacheIds.GUILD
            return
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
        val oldDefaultMessageNotifications = guild.defaultMessageNotificationsLevel.getValue()
        val oldExplicitContentFilter = guild.explicitContentFilterLevel.getValue()
        val oldMfaLevel = guild.mfaLevel.getValue()
        val oldApplicationId = guild.applicationId
        val oldSystemChannelId = guild.systemChannelId
        val oldSystemChannelFlags = guild.systemChannelFlags.getValue()
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
        val oldNSFWLevel = guild.nsfwLevel.getLevel()
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

        GuildUpdateHandlerExtended(ydwk, json)
            .onUpdate(
                guild,
                oldName,
                newName,
                oldIcon,
                newIcon,
                oldSplash,
                newSplash,
                oldDiscoverySplash,
                newDiscoverySplash,
                oldOwnerId.asLong,
                newOwnerId,
                oldPermissions,
                newPermissions,
                oldAfkChannelId?.asLong,
                newAfkChannelId,
                oldAfkTimeout,
                newAfkTimeout,
                oldWidgetEnabled,
                newWidgetEnabled,
                oldWidgetChannelId?.asLong,
                newWidgetChannelId,
                oldVerificationLevel.getLevel(),
                newVerificationLevel,
                oldDefaultMessageNotifications,
                newDefaultMessageNotifications,
                oldExplicitContentFilter,
                newExplicitContentFilter,
                oldMfaLevel,
                newMfaLevel,
                oldApplicationId?.asLong,
                newApplicationId,
                oldSystemChannelId?.asLong,
                newSystemChannelId,
                oldSystemChannelFlags,
                newSystemChannelFlags,
                oldRulesChannelId?.asLong,
                newRulesChannelId,
                oldMaxPresences,
                newMaxPresences,
                oldMaxMembers,
                newMaxMembers,
                oldVanityUrlCode,
                newVanityUrlCode,
                oldDescription,
                newDescription,
                oldBanner,
                newBanner,
                oldPremiumTier.getValue(),
                newPremiumTier,
                oldPremiumSubscriptionCount,
                newPremiumSubscriptionCount,
                oldPreferredLocale,
                newPreferredLocale,
                oldPublicUpdatesChannelId?.asLong,
                newPublicUpdatesChannelId,
                oldMaxVideoChannelUsers,
                newMaxVideoChannelUsers,
                oldApproximateMemberCount,
                newApproximateMemberCount,
                oldApproximatePresenceCount,
                newApproximatePresenceCount,
                oldWelcomeScreen?.json,
                newWelcomeScreen,
                oldNSFWLevel,
                newNSFWLevel,
                oldStickers,
                newStickers,
                wasBoostProgressBarEnabled,
                newBoostProgressBarEnabled,
                oldEmoji,
                newEmoji,
                oldFeatures,
                newFeatures)
    }
}
