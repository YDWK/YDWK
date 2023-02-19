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
package io.github.ydwk.ydwk.evm.listeners

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.event.events.guild.GuildCreateEvent
import io.github.ydwk.ydwk.evm.event.events.guild.GuildDeleteEvent
import io.github.ydwk.ydwk.evm.event.events.guild.update.*

interface GuildListeners : IEventListener {
    /**
     * Listens to GuildCreateEvent
     *
     * @param event The GuildCreateEvent
     */
    fun onGuildCreate(event: GuildCreateEvent) {}

    /**
     * Listens to GuildNameUpdateEvent
     *
     * @param event The GuildNameUpdateEvent
     */
    fun onGuildNameUpdate(event: GuildNameUpdateEvent) {}

    /**
     * Listens to GuildIconUpdateEvent
     *
     * @param event The GuildIconUpdateEvent
     */
    fun onGuildIconUpdate(event: GuildIconUpdateEvent) {}

    /**
     * Listens to GuildSplashUpdateEvent
     *
     * @param event The GuildSplashUpdateEvent
     */
    fun onGuildSplashUpdate(event: GuildSplashUpdateEvent) {}

    /**
     * Listens to GuildCreateEvent
     *
     * @param event The GuildCreateEvent
     */
    fun onGuildDiscoverySplashUpdate(event: GuildDiscoverySplashUpdateEvent) {}

    /**
     * Listens to GuildOwnerUpdateEvent
     *
     * @param event The GuildOwnerUpdateEvent
     */
    fun onGuildOwnerUpdate(event: GuildOwnerUpdateEvent) {}

    /**
     * Listens to PermissionUpdateEvent
     *
     * @param event The PermissionUpdateEvent
     */
    fun onPermissionUpdate(event: GuildPermissionsUpdateEvent) {}

    /**
     * Listens to GuildAfkChannelUpdateEvent
     *
     * @param event The GuildAfkChannelUpdateEvent
     */
    fun onGuildAfkChannelUpdate(event: GuildAfkChannelUpdateEvent) {}

    /**
     * Listens to GuildAfkTimeoutUpdateEvent
     *
     * @param event The GuildAfkTimeoutUpdateEvent
     */
    fun onGuildAfkTimeoutUpdate(event: GuildAfkTimeoutUpdateEvent) {}

    /**
     * Listens to GuildWidgetEnabledUpdateEvent
     *
     * @param event The GuildWidgetEnabledUpdateEvent
     */
    fun onGuildWidgetEnabledUpdate(event: GuildWidgetEnabledUpdateEvent) {}

    /**
     * Listens to GuildWidgetChannelUpdateEvent
     *
     * @param event The GuildWidgetChannelUpdateEvent
     */
    fun onGuildWidgetChannelUpdate(event: GuildWidgetChannelUpdateEvent) {}

    /**
     * Listens to GuildVerificationLevelUpdateEvent
     *
     * @param event The GuildVerificationLevelUpdateEvent
     */
    fun onGuildVerificationLevelUpdate(event: GuildVerificationLevelUpdateEvent) {}

    /**
     * Listens to GuildDefaultMessageNotificationLevelUpdateEvent
     *
     * @param event The GuildDefaultMessageNotificationLevelUpdateEvent
     */
    fun onGuildDefaultMessageNotificationLevelUpdate(
        event: GuildDefaultMessageNotificationLevelUpdateEvent
    ) {}

    /**
     * Listens to GuildExplicitContentFilterLevelUpdateEvent
     *
     * @param event The GuildExplicitContentFilterLevelUpdateEvent
     */
    fun onGuildExplicitContentFilterLevelUpdate(
        event: GuildExplicitContentFilterLevelUpdateEvent
    ) {}

    /**
     * Listens to GuildMfaLevelUpdateEvent
     *
     * @param event The GuildMfaLevelUpdateEvent
     */
    fun onGuildMfaLevelUpdate(event: GuildMfaLevelUpdateEvent) {}

    /**
     * Listens to GuildApplicationIdUpdateEvent
     *
     * @param event The GuildApplicationIdUpdateEvent
     */
    fun onGuildApplicationIdUpdate(event: GuildApplicationIdUpdateEvent) {}

    /**
     * Listens to GuildSystemChannelUpdateEvent
     *
     * @param event The GuildSystemChannelUpdateEvent
     */
    fun onGuildSystemChannelUpdate(event: GuildSystemChannelUpdateEvent) {}

    /**
     * Listens to GuildSystemChannelFlagsUpdateEvent
     *
     * @param event The GuildSystemChannelFlagsUpdateEvent
     */
    fun onGuildSystemChannelFlagsUpdate(event: GuildSystemChannelFlagsUpdateEvent) {}

    /**
     * Listens to GuildRulesChannelUpdateEvent
     *
     * @param event The GuildRulesChannelUpdateEvent
     */
    fun onGuildRulesChannelUpdate(event: GuildRulesChannelUpdateEvent) {}

    /**
     * Listens to GuildMaxPresencesUpdateEvent
     *
     * @param event The GuildMaxPresencesUpdateEvent
     */
    fun onGuildMaxPresencesUpdate(event: GuildMaxPresencesUpdateEvent) {}

    /**
     * Listens to GuildVanityUrlUpdateEvent
     *
     * @param event The GuildVanityUrlUpdateEvent
     */
    fun onGuildVanityUrlUpdate(event: GuildVanityUrlUpdateEvent) {}

    /**
     * Listens to GuildDescriptionUpdateEvent
     *
     * @param event The GuildDescriptionUpdateEvent
     */
    fun onGuildDescriptionUpdate(event: GuildDescriptionUpdateEvent) {}

    /**
     * Listens to GuildBannerUpdateEvent
     *
     * @param event The GuildBannerUpdateEvent
     */
    fun onGuildBannerUpdate(event: GuildBannerUpdateEvent) {}

    /**
     * Listens to GuildPremiumTierUpdateEvent
     *
     * @param event The GuildPremiumTierUpdateEvent
     */
    fun onGuildPremiumTierUpdate(event: GuildPremiumTierUpdateEvent) {}

    /**
     * Listens to GuildPremiumSubscriptionCountUpdateEvent
     *
     * @param event The GuildPremiumSubscriptionCountUpdateEvent
     */
    fun onGuildPremiumSubscriptionCountUpdate(event: GuildPremiumSubscriptionCountUpdateEvent) {}

    /**
     * Listens to GuildPreferredLocaleUpdateEvent
     *
     * @param event The GuildPreferredLocaleUpdateEvent
     */
    fun onGuildPreferredLocaleUpdate(event: GuildPreferredLocaleUpdateEvent) {}

    /**
     * Listens to GuildPublicUpdatesChannelUpdateEvent
     *
     * @param event The GuildPublicUpdatesChannelUpdateEvent
     */
    fun onGuildPublicUpdatesChannelUpdate(event: GuildPublicUpdatesChannelUpdateEvent) {}

    /**
     * Listens to GuildMaxVideoChannelUsersUpdateEvent
     *
     * @param event The GuildMaxVideoChannelUsersUpdateEvent
     */
    fun onGuildMaxVideoChannelUsersUpdate(event: GuildMaxVideoChannelUsersUpdateEvent) {}

    /**
     * Listens to GuildApproximateMemberCountUpdateEvent
     *
     * @param event The GuildApproximateMemberCountUpdateEvent
     */
    fun onGuildApproximateMemberCountUpdate(event: GuildApproximateMemberCountUpdateEvent) {}

    /**
     * Listens to GuildWelcomeScreenUpdateEvent
     *
     * @param event The GuildWelcomeScreenUpdateEvent
     */
    fun onGuildWelcomeScreenUpdate(event: GuildWelcomeScreenUpdateEvent) {}

    /**
     * Listens to GuildNSFWLevelUpdateEvent
     *
     * @param event The GuildNSFWLevelUpdateEvent
     */
    fun onGuildNSFWLevelUpdate(event: GuildNSFWLevelUpdateEvent) {}

    /**
     * Listens to GuildStickersUpdateEvent
     *
     * @param event The GuildStickersUpdateEvent
     */
    fun onGuildStickersUpdate(event: GuildStickersUpdateEvent) {}

    /**
     * Listens to GuildBoostProgressBarEnabledUpdateEvent
     *
     * @param event The GuildBoostProgressBarEnabledUpdateEvent
     */
    fun onGuildBoostProgressBarEnabledUpdate(event: GuildBoostProgressBarEnabledUpdateEvent) {}

    /**
     * Listens to GuildEmojisUpdateEvent
     *
     * @param event The GuildEmojisUpdateEvent
     */
    fun onGuildEmojisUpdate(event: GuildEmojisUpdateEvent) {}

    /**
     * Listens to GuildFeaturesUpdateEvent
     *
     * @param event The GuildFeaturesUpdateEvent
     */
    fun onGuildFeaturesUpdate(event: GuildFeaturesUpdateEvent) {}

    /**
     * Listens to GuildDeleteEvent
     *
     * @param event The GuildDeleteEvent
     */
    fun onGuildDelete(event: GuildDeleteEvent) {}

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is GuildCreateEvent -> onGuildCreate(event)
            is GuildNameUpdateEvent -> onGuildNameUpdate(event)
            is GuildIconUpdateEvent -> onGuildIconUpdate(event)
            is GuildSplashUpdateEvent -> onGuildSplashUpdate(event)
            is GuildDiscoverySplashUpdateEvent -> onGuildDiscoverySplashUpdate(event)
            is GuildOwnerUpdateEvent -> onGuildOwnerUpdate(event)
            is GuildPermissionsUpdateEvent -> onPermissionUpdate(event)
            is GuildAfkChannelUpdateEvent -> onGuildAfkChannelUpdate(event)
            is GuildAfkTimeoutUpdateEvent -> onGuildAfkTimeoutUpdate(event)
            is GuildWidgetEnabledUpdateEvent -> onGuildWidgetEnabledUpdate(event)
            is GuildWidgetChannelUpdateEvent -> onGuildWidgetChannelUpdate(event)
            is GuildVerificationLevelUpdateEvent -> onGuildVerificationLevelUpdate(event)
            is GuildDefaultMessageNotificationLevelUpdateEvent ->
                onGuildDefaultMessageNotificationLevelUpdate(event)
            is GuildExplicitContentFilterLevelUpdateEvent ->
                onGuildExplicitContentFilterLevelUpdate(event)
            is GuildMfaLevelUpdateEvent -> onGuildMfaLevelUpdate(event)
            is GuildApplicationIdUpdateEvent -> onGuildApplicationIdUpdate(event)
            is GuildSystemChannelUpdateEvent -> onGuildSystemChannelUpdate(event)
            is GuildSystemChannelFlagsUpdateEvent -> onGuildSystemChannelFlagsUpdate(event)
            is GuildRulesChannelUpdateEvent -> onGuildRulesChannelUpdate(event)
            is GuildMaxPresencesUpdateEvent -> onGuildMaxPresencesUpdate(event)
            is GuildVanityUrlUpdateEvent -> onGuildVanityUrlUpdate(event)
            is GuildDescriptionUpdateEvent -> onGuildDescriptionUpdate(event)
            is GuildBannerUpdateEvent -> onGuildBannerUpdate(event)
            is GuildPremiumTierUpdateEvent -> onGuildPremiumTierUpdate(event)
            is GuildPremiumSubscriptionCountUpdateEvent ->
                onGuildPremiumSubscriptionCountUpdate(event)
            is GuildPreferredLocaleUpdateEvent -> onGuildPreferredLocaleUpdate(event)
            is GuildPublicUpdatesChannelUpdateEvent -> onGuildPublicUpdatesChannelUpdate(event)
            is GuildMaxVideoChannelUsersUpdateEvent -> onGuildMaxVideoChannelUsersUpdate(event)
            is GuildApproximateMemberCountUpdateEvent -> onGuildApproximateMemberCountUpdate(event)
            is GuildWelcomeScreenUpdateEvent -> onGuildWelcomeScreenUpdate(event)
            is GuildNSFWLevelUpdateEvent -> onGuildNSFWLevelUpdate(event)
            is GuildStickersUpdateEvent -> onGuildStickersUpdate(event)
            is GuildBoostProgressBarEnabledUpdateEvent ->
                onGuildBoostProgressBarEnabledUpdate(event)
            is GuildEmojisUpdateEvent -> onGuildEmojisUpdate(event)
            is GuildFeaturesUpdateEvent -> onGuildFeaturesUpdate(event)
            is GuildDeleteEvent -> onGuildDelete(event)
        }
    }
}
