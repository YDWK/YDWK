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
package io.github.ydwk.ydwk.evm

import io.github.ydwk.ydwk.evm.backend.ClassWalker
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.backend.update.IEventUpdate
import io.github.ydwk.ydwk.evm.event.Event
import io.github.ydwk.ydwk.evm.event.events.ban.GuildBanAddEvent
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelCreateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelDeleteEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.category.CategoryNameUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.message.*
import io.github.ydwk.ydwk.evm.event.events.channel.update.stage.StateChannelTopicUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.text.TextChannelSlowModeUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelBitrateUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelNameUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelRateLimitPerUserUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelUserLimitUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.DisconnectEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ReadyEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ResumeEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ShutDownEvent
import io.github.ydwk.ydwk.evm.event.events.guild.GuildCreateEvent
import io.github.ydwk.ydwk.evm.event.events.guild.GuildDeleteEvent
import io.github.ydwk.ydwk.evm.event.events.guild.update.*
import io.github.ydwk.ydwk.evm.event.events.interaction.*
import io.github.ydwk.ydwk.evm.event.events.user.*
import io.github.ydwk.ydwk.evm.event.events.voice.VoiceConnectionEvent
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

abstract class ListenerAdapter : IEventListener {

    /** Listens to all events */
    open fun onGenericEvent(event: GenericEvent) {}

    /** Listens to all event updates */
    open fun onGenericUpdate(eventUpdate: IEventUpdate<*, *>) {}

    /**
     * Listens to ready event
     *
     * @param event The ready event
     */
    open fun onReady(event: ReadyEvent) {}

    /**
     * Listens to Disconnect event
     *
     * @param event The disconnect event
     */
    open fun onDisconnect(event: DisconnectEvent) {}

    /**
     * Listens to Resume event
     *
     * @param event The resume event
     */
    open fun onResume(event: ResumeEvent) {}

    /**
     * Listens to ShutDown event
     *
     * @param event The shutdown event
     */
    open fun onShutDown(event: ShutDownEvent) {}

    // interactions

    /**
     * Listens to SlashCommandEvent
     *
     * @param event The SlashCommandEvent
     */
    open fun onSlashCommand(event: SlashCommandEvent) {}

    /**
     * Listens to AutoCompleteSlashCommandEvent
     *
     * @param event The AutoCompleteSlashCommandEvent
     */
    open fun onAutoCompleteSlashCommand(event: AutoCompleteSlashCommandEvent) {}

    /**
     * Listens to Model Event
     *
     * @param event The Model Event
     */
    open fun onModel(event: ModelEvent) {}

    /**
     * Listens to Ping Event
     *
     * @param event The Ping Event
     */
    open fun onPing(event: PingEvent) {}

    /**
     * Listens to Message Component Event
     *
     * @param event The Message Component Event
     */
    open fun onMessageComponent(event: MessageComponentEvent) {}

    // Channel
    /**
     * Listens to ChannelCreateEvent
     *
     * @param event The ChannelCreateEvent
     */
    open fun onChannelCreate(event: ChannelCreateEvent) {}

    /**
     * Listens to ChannelDeleteEvent
     *
     * @param event The ChannelDeleteEvent
     */
    open fun onChannelDelete(event: ChannelDeleteEvent) {}

    // text/news channel

    /**
     * Listens to MessageChannelNameUpdateEvent
     *
     * @param event The MessageChannelNameUpdateEvent
     */
    open fun onMessageChannelNameUpdate(event: MessageChannelNameUpdateEvent) {}

    /**
     * Listens to MessageChannelTopicUpdateEvent
     *
     * @param event The MessageChannelTopicUpdateEvent
     */
    open fun onMessageChannelTopicUpdate(event: MessageChannelTopicUpdateEvent) {}

    /**
     * Listens to MessageChannelNSFWUpdateEvent
     *
     * @param event The MessageChannelNSFWUpdateEvent
     */
    open fun onMessageChannelNsfwUpdate(event: MessageChannelNsfwUpdateEvent) {}

    /**
     * Listens to MessageChannelLastMessageIdUpdateEvent
     *
     * @param event The MessageChannelLastMessageIdUpdateEvent
     */
    open fun onMessageChannelLastMessageIdUpdate(event: MessageChannelLastMessageIdUpdateEvent) {}

    /**
     * Listens to MessageChannelLastPinTimestampUpdateEvent
     *
     * @param event The MessageChannelLastPinTimestampUpdateEvent
     */
    open fun onMessageChannelLastPinTimestampUpdate(
        event: MessageChannelLastPinTimestampUpdateEvent
    ) {}

    /**
     * Listens to MessageChannelDefaultAutoArchiveDurationUpdateEvent
     *
     * @param event The MessageChannelDefaultAutoArchiveDurationUpdateEvent
     */
    open fun onMessageChannelDefaultAutoArchiveDurationUpdate(
        event: MessageChannelDefaultAutoArchiveDurationUpdateEvent
    ) {}

    /**
     * Listens to MessageChannelPermissionOverwritesUpdateEvent
     *
     * @param event The MessageChannelPermissionOverwritesUpdateEvent
     */
    open fun onMessageChannelPermissionOverwritesUpdate(
        event: MessageChannelPermissionOverwritesUpdateEvent
    ) {}

    /**
     * Listens to TextChannelSlowModeUpdateEvent
     *
     * @param event The TextChannelSlowModeUpdateEvent
     */
    open fun onTextChannelSlowModeUpdate(event: TextChannelSlowModeUpdateEvent) {}

    /**
     * Listens to CategoryNameUpdateEvent
     *
     * @param event The CategoryNameUpdateEvent
     */
    open fun onCategoryNameUpdate(event: CategoryNameUpdateEvent) {}

    // vc/stage channel

    /**
     * Listens to StateChannelTopicUpdateEvent
     *
     * @param event The StateChannelTopicUpdateEvent
     */
    open fun onStateChannelTopicUpdate(event: StateChannelTopicUpdateEvent) {}

    /**
     * Listens to VoiceChannelBitrateUpdateEvent
     *
     * @param event The VoiceChannelBitrateUpdateEvent
     */
    open fun onVoiceChannelBitrateUpdate(event: VoiceChannelBitrateUpdateEvent) {}

    /**
     * Listens to VoiceChannelUserLimitUpdateEvent
     *
     * @param event The VoiceChannelUserLimitUpdateEvent
     */
    open fun onVoiceChannelUserLimitUpdate(event: VoiceChannelUserLimitUpdateEvent) {}

    /**
     * Listens to VoiceChannelRateLimitPerUserUpdateEvent
     *
     * @param event The VoiceChannelRateLimitPerUserUpdateEvent
     */
    open fun onVoiceChannelRateLimitPerUserUpdate(event: VoiceChannelRateLimitPerUserUpdateEvent) {}

    /**
     * Listens to VoiceChannelNameUpdateEvent
     *
     * @param event The VoiceChannelNameUpdateEvent
     */
    open fun onVoiceChannelNameUpdate(event: VoiceChannelNameUpdateEvent) {}

    // guild
    /**
     * Listens to GuildCreateEvent
     *
     * @param event The GuildCreateEvent
     */
    open fun onGuildCreate(event: GuildCreateEvent) {}

    /**
     * Listens to GuildNameUpdateEvent
     *
     * @param event The GuildNameUpdateEvent
     */
    open fun onGuildNameUpdate(event: GuildNameUpdateEvent) {}

    /**
     * Listens to GuildIconUpdateEvent
     *
     * @param event The GuildIconUpdateEvent
     */
    open fun onGuildIconUpdate(event: GuildIconUpdateEvent) {}

    /**
     * Listens to GuildSplashUpdateEvent
     *
     * @param event The GuildSplashUpdateEvent
     */
    open fun onGuildSplashUpdate(event: GuildSplashUpdateEvent) {}

    /**
     * Listens to GuildDiscoverySplashUpdateEvent
     *
     * @param event The GuildDiscoverySplashUpdateEvent
     */
    open fun onGuildDiscoverySplashUpdate(event: GuildDiscoverySplashUpdateEvent) {}

    /**
     * Listens to GuildOwnerUpdateEvent
     *
     * @param event The GuildOwnerUpdateEvent
     */
    open fun onGuildOwnerUpdate(event: GuildOwnerUpdateEvent) {}

    /**
     * Listens to PermissionUpdateEvent
     *
     * @param event The PermissionUpdateEvent
     */
    open fun onPermissionUpdate(event: GuildPermissionsUpdateEvent) {}

    /**
     * Listens to GuildAfkChannelUpdateEvent
     *
     * @param event The GuildAfkChannelUpdateEvent
     */
    open fun onGuildAfkChannelUpdate(event: GuildAfkChannelUpdateEvent) {}

    /**
     * Listens to GuildAfkTimeoutUpdateEvent
     *
     * @param event The GuildAfkTimeoutUpdateEvent
     */
    open fun onGuildAfkTimeoutUpdate(event: GuildAfkTimeoutUpdateEvent) {}

    /**
     * Listens to GuildWidgetEnabledUpdateEvent
     *
     * @param event The GuildWidgetEnabledUpdateEvent
     */
    open fun onGuildWidgetEnabledUpdate(event: GuildWidgetEnabledUpdateEvent) {}

    /**
     * Listens to GuildWidgetChannelUpdateEvent
     *
     * @param event The GuildWidgetChannelUpdateEvent
     */
    open fun onGuildWidgetChannelUpdate(event: GuildWidgetChannelUpdateEvent) {}

    /**
     * Listens to GuildVerificationLevelUpdateEvent
     *
     * @param event The GuildVerificationLevelUpdateEvent
     */
    open fun onGuildVerificationLevelUpdate(event: GuildVerificationLevelUpdateEvent) {}

    /**
     * Listens to GuildDefaultMessageNotificationLevelUpdateEvent
     *
     * @param event The GuildDefaultMessageNotificationLevelUpdateEvent
     */
    open fun onGuildDefaultMessageNotificationLevelUpdate(
        event: GuildDefaultMessageNotificationLevelUpdateEvent
    ) {}

    /**
     * Listens to GuildExplicitContentFilterLevelUpdateEvent
     *
     * @param event The GuildExplicitContentFilterLevelUpdateEvent
     */
    open fun onGuildExplicitContentFilterLevelUpdate(
        event: GuildExplicitContentFilterLevelUpdateEvent
    ) {}

    /**
     * Listens to GuildMfaLevelUpdateEvent
     *
     * @param event The GuildMfaLevelUpdateEvent
     */
    open fun onGuildMfaLevelUpdate(event: GuildMfaLevelUpdateEvent) {}

    /**
     * Listens to GuildApplicationIdUpdateEvent
     *
     * @param event The GuildApplicationIdUpdateEvent
     */
    open fun onGuildApplicationIdUpdate(event: GuildApplicationIdUpdateEvent) {}

    /**
     * Listens to GuildSystemChannelUpdateEvent
     *
     * @param event The GuildSystemChannelUpdateEvent
     */
    open fun onGuildSystemChannelUpdate(event: GuildSystemChannelUpdateEvent) {}

    /**
     * Listens to GuildSystemChannelFlagsUpdateEvent
     *
     * @param event The GuildSystemChannelFlagsUpdateEvent
     */
    open fun onGuildSystemChannelFlagsUpdate(event: GuildSystemChannelFlagsUpdateEvent) {}

    /**
     * Listens to GuildRulesChannelUpdateEvent
     *
     * @param event The GuildRulesChannelUpdateEvent
     */
    open fun onGuildRulesChannelUpdate(event: GuildRulesChannelUpdateEvent) {}

    /**
     * Listens to GuildMaxPresencesUpdateEvent
     *
     * @param event The GuildMaxPresencesUpdateEvent
     */
    open fun onGuildMaxPresencesUpdate(event: GuildMaxPresencesUpdateEvent) {}

    /**
     * Listens to GuildVanityUrlUpdateEvent
     *
     * @param event The GuildVanityUrlUpdateEvent
     */
    open fun onGuildVanityUrlUpdate(event: GuildVanityUrlUpdateEvent) {}

    /**
     * Listens to GuildDescriptionUpdateEvent
     *
     * @param event The GuildDescriptionUpdateEvent
     */
    open fun onGuildDescriptionUpdate(event: GuildDescriptionUpdateEvent) {}

    /**
     * Listens to GuildBannerUpdateEvent
     *
     * @param event The GuildBannerUpdateEvent
     */
    open fun onGuildBannerUpdate(event: GuildBannerUpdateEvent) {}

    /**
     * Listens to GuildPremiumTierUpdateEvent
     *
     * @param event The GuildPremiumTierUpdateEvent
     */
    open fun onGuildPremiumTierUpdate(event: GuildPremiumTierUpdateEvent) {}

    /**
     * Listens to GuildPremiumSubscriptionCountUpdateEvent
     *
     * @param event The GuildPremiumSubscriptionCountUpdateEvent
     */
    open fun onGuildPremiumSubscriptionCountUpdate(
        event: GuildPremiumSubscriptionCountUpdateEvent
    ) {}

    /**
     * Listens to GuildPreferredLocaleUpdateEvent
     *
     * @param event The GuildPreferredLocaleUpdateEvent
     */
    open fun onGuildPreferredLocaleUpdate(event: GuildPreferredLocaleUpdateEvent) {}

    /**
     * Listens to GuildPublicUpdatesChannelUpdateEvent
     *
     * @param event The GuildPublicUpdatesChannelUpdateEvent
     */
    open fun onGuildPublicUpdatesChannelUpdate(event: GuildPublicUpdatesChannelUpdateEvent) {}

    /**
     * Listens to GuildMaxVideoChannelUsersUpdateEvent
     *
     * @param event The GuildMaxVideoChannelUsersUpdateEvent
     */
    open fun onGuildMaxVideoChannelUsersUpdate(event: GuildMaxVideoChannelUsersUpdateEvent) {}

    /**
     * Listens to GuildApproximateMemberCountUpdateEvent
     *
     * @param event The GuildApproximateMemberCountUpdateEvent
     */
    open fun onGuildApproximateMemberCountUpdate(event: GuildApproximateMemberCountUpdateEvent) {}

    /**
     * Listens to GuildWelcomeScreenUpdateEvent
     *
     * @param event The GuildWelcomeScreenUpdateEvent
     */
    open fun onGuildWelcomeScreenUpdate(event: GuildWelcomeScreenUpdateEvent) {}

    /**
     * Listens to GuildNSFWLevelUpdateEvent
     *
     * @param event The GuildNSFWLevelUpdateEvent
     */
    open fun onGuildNSFWLevelUpdate(event: GuildNSFWLevelUpdateEvent) {}

    /**
     * Listens to GuildStickersUpdateEvent
     *
     * @param event The GuildStickersUpdateEvent
     */
    open fun onGuildStickersUpdate(event: GuildStickersUpdateEvent) {}

    /**
     * Listens to GuildBoostProgressBarEnabledUpdateEvent
     *
     * @param event The GuildBoostProgressBarEnabledUpdateEvent
     */
    open fun onGuildBoostProgressBarEnabledUpdate(event: GuildBoostProgressBarEnabledUpdateEvent) {}

    /**
     * Listens to GuildEmojisUpdateEvent
     *
     * @param event The GuildEmojisUpdateEvent
     */
    open fun onGuildEmojisUpdate(event: GuildEmojisUpdateEvent) {}

    /**
     * Listens to GuildFeaturesUpdateEvent
     *
     * @param event The GuildFeaturesUpdateEvent
     */
    open fun onGuildFeaturesUpdate(event: GuildFeaturesUpdateEvent) {}

    /**
     * Listens to GuildDeleteEvent
     *
     * @param event The GuildDeleteEvent
     */
    open fun onGuildDelete(event: GuildDeleteEvent) {}

    // ban
    /**
     * Listens to GuildBanAddEvent
     *
     * @param event The GuildBanAddEvent
     */
    open fun onGuildBanAdd(event: GuildBanAddEvent) {}

    // user

    /**
     * Listens to UserNameUpdateEvent
     *
     * @param event The UserNameUpdateEvent
     */
    open fun onUserNameUpdate(event: UserNameUpdateEvent) {}

    /**
     * Listens to UserDiscriminatorUpdateEvent
     *
     * @param event The UserDiscriminatorUpdateEvent
     */
    open fun onUserDiscriminatorUpdate(event: UserDiscriminatorUpdateEvent) {}

    /**
     * Listens to UserAvatarUpdateEvent
     *
     * @param event The UserAvatarUpdateEvent
     */
    open fun onUserAvatarUpdate(event: UserAvatarUpdateEvent) {}

    /**
     * Listens to UserSystemUpdateEvent
     *
     * @param event The UserSystemUpdateEvent
     */
    open fun onUserSystemUpdate(event: UserSystemUpdateEvent) {}

    /**
     * Listens to UserMfaEnabledUpdateEvent
     *
     * @param event The UserMfaEnabledUpdateEvent
     */
    open fun onUserMfaEnabledUpdate(event: UserMfaEnabledUpdateEvent) {}

    /**
     * Listens to UserBannerUpdateEvent
     *
     * @param event The UserBannerUpdateEvent
     */
    open fun onUserBannerUpdate(event: UserBannerUpdateEvent) {}

    /**
     * Listens to UserAccentColorUpdateEvent
     *
     * @param event The UserAccentColorUpdateEvent
     */
    open fun onUserAccentColorUpdate(event: UserAccentColorUpdateEvent) {}

    /**
     * Listens to UserLocaleUpdateEvent
     *
     * @param event The UserLocaleUpdateEvent
     */
    open fun onUserLocaleUpdate(event: UserLocaleUpdateEvent) {}

    /**
     * Listens to UserVerifiedUpdateEvent
     *
     * @param event The UserVerifiedUpdateEvent
     */
    open fun onUserVerifiedUpdate(event: UserVerifiedUpdateEvent) {}

    /**
     * Listens to UserFlagsUpdateEvent
     *
     * @param event The UserFlagsUpdateEvent
     */
    open fun onUserFlagsUpdate(event: UserFlagsUpdateEvent) {}

    // voice

    /**
     * Listens to VoiceConnectionEvent
     *
     * @param event The VoiceConnectionEvent
     */
    open fun onVoiceConnection(event: VoiceConnectionEvent) {}

    /**
     * This method is called when an event is received.
     *
     * @param event The event that was received.
     */
    override fun onEvent(event: GenericEvent) {
        onGenericEvent(event)

        if (event is IEventUpdate<*, *>) onGenericUpdate(event)

        for (clazz in ClassWalker.range(event.javaClass, Event::class.java)) {
            if (unresolved!!.contains(clazz)) continue
            val mh = methods.computeIfAbsent(clazz, Companion::findMethod)
            if (mh == null) {
                unresolved!!.add(clazz)
                continue
            }
            try {
                mh.invoke(this, event)
            } catch (throwable: Throwable) {
                if (throwable is RuntimeException) throw throwable
                if (throwable is Error) throw throwable
                throw IllegalStateException(throwable)
            }
        }
    }

    companion object {
        private val lookup = MethodHandles.lookup()
        private val methods: ConcurrentMap<Class<*>, MethodHandle> = ConcurrentHashMap()
        private var unresolved: MutableSet<Class<*>>? = null

        init {
            unresolved = ConcurrentHashMap.newKeySet()
            unresolved?.let {
                Collections.addAll(
                    it,
                    Any::class.java, // Objects aren't events
                    Event::class.java, // onEvent is final and would never be found
                    IEventUpdate::class.java, // onBasicUpdate has already been called
                    GenericEvent::class.java // onBasicEvent has already been called
                    )
            }
        }

        private fun findMethod(clazz: Class<*>): MethodHandle? {
            var name = clazz.simpleName
            val type = MethodType.methodType(Void.TYPE, clazz)
            try {
                name = "on" + name.substring(0, name.length - "Event".length)
                return lookup.findVirtual(ListenerAdapter::class.java, name, type)
            } catch (
                ignored: NoSuchMethodException) {} // this means this is probably a custom event!
            catch (ignored: IllegalAccessException) {}
            return null
        }
    }
}
