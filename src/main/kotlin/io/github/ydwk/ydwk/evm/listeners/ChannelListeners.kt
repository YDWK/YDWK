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
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelCreateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelDeleteEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.category.CategoryNameUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.forum.*
import io.github.ydwk.ydwk.evm.event.events.channel.update.guild.GuildChannelParentUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.guild.GuildChannelPositionUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.message.*
import io.github.ydwk.ydwk.evm.event.events.channel.update.stage.StateChannelTopicUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.text.TextChannelSlowModeUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelBitrateUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelNameUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelRateLimitPerUserUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelUserLimitUpdateEvent

interface ChannelListeners : IEventListener {
    /**
     * Listens to ChannelCreateEvent
     *
     * @param event The ChannelCreateEvent
     */
    fun onChannelCreate(event: ChannelCreateEvent) {}

    /**
     * Listens to ChannelDeleteEvent
     *
     * @param event The ChannelDeleteEvent
     */
    fun onChannelDelete(event: ChannelDeleteEvent) {}

    /**
     * Listens to GuildChannelParentUpdateEvent.
     *
     * @param event The GuildChannelParentUpdateEvent
     */
    fun onGuildChannelParentUpdate(event: GuildChannelParentUpdateEvent) {}

    /**
     * Listens to GuildChannelPositionUpdateEvent
     *
     * @param event The GuildChannelPositionUpdateEvent
     */
    fun onGuildChannelPositionUpdate(event: GuildChannelPositionUpdateEvent) {}

    // text/news channel

    /**
     * Listens to MessageChannelNameUpdateEvent
     *
     * @param event The MessageChannelNameUpdateEvent
     */
    fun onMessageChannelNameUpdate(event: MessageChannelNameUpdateEvent) {}

    /**
     * Listens to MessageChannelTopicUpdateEvent
     *
     * @param event The MessageChannelTopicUpdateEvent
     */
    fun onMessageChannelTopicUpdate(event: MessageChannelTopicUpdateEvent) {}

    /**
     * Listens to MessageChannelNSFWUpdateEvent
     *
     * @param event The MessageChannelNSFWUpdateEvent
     */
    fun onMessageChannelNsfwUpdate(event: MessageChannelNsfwUpdateEvent) {}

    /**
     * Listens to MessageChannelLastMessageIdUpdateEvent
     *
     * @param event The MessageChannelLastMessageIdUpdateEvent
     */
    fun onMessageChannelLastMessageIdUpdate(event: MessageChannelLastMessageIdUpdateEvent) {}

    /**
     * Listens to MessageChannelLastPinTimestampUpdateEvent
     *
     * @param event The MessageChannelLastPinTimestampUpdateEvent
     */
    fun onMessageChannelLastPinTimestampUpdate(event: MessageChannelLastPinTimestampUpdateEvent) {}

    /**
     * Listens to MessageChannelDefaultAutoArchiveDurationUpdateEvent
     *
     * @param event The MessageChannelDefaultAutoArchiveDurationUpdateEvent
     */
    fun onMessageChannelDefaultAutoArchiveDurationUpdate(
        event: MessageChannelDefaultAutoArchiveDurationUpdateEvent
    ) {}

    /**
     * Listens to MessageChannelPermissionOverwritesUpdateEvent
     *
     * @param event The MessageChannelPermissionOverwritesUpdateEvent
     */
    fun onMessageChannelPermissionOverwritesUpdate(
        event: MessageChannelPermissionOverwritesUpdateEvent
    ) {}

    /**
     * Listens to TextChannelSlowModeUpdateEvent
     *
     * @param event The TextChannelSlowModeUpdateEvent
     */
    fun onTextChannelSlowModeUpdate(event: TextChannelSlowModeUpdateEvent) {}

    /**
     * Listens to CategoryNameUpdateEvent
     *
     * @param event The CategoryNameUpdateEvent
     */
    fun onCategoryNameUpdate(event: CategoryNameUpdateEvent) {}

    // vc/stage channel

    /**
     * Listens to StateChannelTopicUpdateEvent
     *
     * @param event The StateChannelTopicUpdateEvent
     */
    fun onStateChannelTopicUpdate(event: StateChannelTopicUpdateEvent) {}

    /**
     * Listens to VoiceChannelBitrateUpdateEvent
     *
     * @param event The VoiceChannelBitrateUpdateEvent
     */
    fun onVoiceChannelBitrateUpdate(event: VoiceChannelBitrateUpdateEvent) {}

    /**
     * Listens to VoiceChannelUserLimitUpdateEvent
     *
     * @param event The VoiceChannelUserLimitUpdateEvent
     */
    fun onVoiceChannelUserLimitUpdate(event: VoiceChannelUserLimitUpdateEvent) {}

    /**
     * Listens to VoiceChannelRateLimitPerUserUpdateEvent
     *
     * @param event The VoiceChannelRateLimitPerUserUpdateEvent
     */
    fun onVoiceChannelRateLimitPerUserUpdate(event: VoiceChannelRateLimitPerUserUpdateEvent) {}

    /**
     * Listens to VoiceChannelNameUpdateEvent
     *
     * @param event The VoiceChannelNameUpdateEvent
     */
    fun onVoiceChannelNameUpdate(event: VoiceChannelNameUpdateEvent) {}

    // forum channel

    /**
     * Listens to ForumChannelNameUpdateEvent
     *
     * @param event The ForumChannelNameUpdateEvent
     */
    fun onForumChannelNameUpdate(event: ForumChannelNameUpdateEvent) {}

    /**
     * Listens to ForumChannelTemplateUpdateEvent
     *
     * @param event The ForumChannelTemplateUpdateEvent
     */
    fun onForumChannelTemplateUpdate(event: ForumChannelTemplateUpdateEvent) {}

    /**
     * Listens to ForumChannelRateLimitPerUserUpdateEvent
     *
     * @param event The ForumChannelRateLimitPerUserUpdateEvent
     */
    fun onForumChannelRateLimitPerUserUpdate(event: ForumChannelRateLimitPerUserUpdateEvent) {}

    /**
     * Listens to ForumChannelPermissionOverwritesUpdateEvent
     *
     * @param event The ForumChannelPermissionOverwritesUpdateEvent
     */
    fun onForumChannelPermissionOverwritesUpdate(
        event: ForumChannelPermissionOverwritesUpdateEvent
    ) {}

    /**
     * Listens to ForumChannelNsfwUpdateEvent.
     *
     * @param event The ForumChannelNsfwUpdateEvent
     */
    fun onForumChannelNsfwUpdate(event: ForumChannelNsfwUpdateEvent) {}

    /**
     * Listens to ForumChannelLastMessageIdUpdateEvent
     *
     * @param event The ForumChannelLastMessageIdUpdateEvent
     */
    fun onForumChannelLastMessageIdUpdate(event: ForumChannelLastMessageIdUpdateEvent) {}

    /**
     * Listens to ForumChannelChannelFlagsUpdateEvent
     *
     * @param event The ForumChannelChannelFlagsUpdateEvent
     */
    fun onForumChannelChannelFlagsUpdate(event: ForumChannelChannelFlagsUpdateEvent) {}

    /**
     * Listens to ForumChannelDefaultSortOrderUpdateEvent
     *
     * @param event The ForumChannelDefaultSortOrderUpdateEvent
     */
    fun onForumChannelDefaultSortOrderUpdate(event: ForumChannelDefaultSortOrderUpdateEvent) {}

    /**
     * Listens to ForumChannelDefaultReactionEmojiUpdateEvent
     *
     * @param event The ForumChannelDefaultReactionEmojiUpdateEvent
     */
    fun onForumChannelDefaultReactionEmojiUpdate(
        event: ForumChannelDefaultReactionEmojiUpdateEvent
    ) {}

    /**
     * Listens to ForumChannelAvailableForumTagsUpdateEvent
     *
     * @param event The ForumChannelAvailableForumTagsUpdateEvent
     */
    fun onForumChannelAvailableForumTagsUpdate(event: ForumChannelAvailableForumTagsUpdateEvent) {}

    /**
     * Listens to ForumChannelAvailableForumTagsIdsUpdateEvent
     *
     * @param event The ForumChannelAvailableForumTagsIdsUpdateEvent
     */
    fun onForumChannelAvailableForumTagsIdsUpdate(
        event: ForumChannelAvailableForumTagsIdsUpdateEvent
    ) {}

    /**
     * Listens to ForumChannelDefaultForumLayoutViewUpdateEvent.
     *
     * @param event The ForumChannelDefaultForumLayoutViewUpdateEvent
     */
    fun onForumChannelDefaultForumLayoutViewUpdate(
        event: ForumChannelDefaultForumLayoutViewUpdateEvent
    ) {}

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is ChannelCreateEvent -> onChannelCreate(event)
            is ChannelDeleteEvent -> onChannelDelete(event)
            is GuildChannelParentUpdateEvent -> onGuildChannelParentUpdate(event)
            is GuildChannelPositionUpdateEvent -> onGuildChannelPositionUpdate(event)
            is MessageChannelNameUpdateEvent -> onMessageChannelNameUpdate(event)
            is MessageChannelTopicUpdateEvent -> onMessageChannelTopicUpdate(event)
            is MessageChannelNsfwUpdateEvent -> onMessageChannelNsfwUpdate(event)
            is MessageChannelLastMessageIdUpdateEvent -> onMessageChannelLastMessageIdUpdate(event)
            is MessageChannelLastPinTimestampUpdateEvent ->
                onMessageChannelLastPinTimestampUpdate(event)
            is MessageChannelDefaultAutoArchiveDurationUpdateEvent ->
                onMessageChannelDefaultAutoArchiveDurationUpdate(event)
            is MessageChannelPermissionOverwritesUpdateEvent ->
                onMessageChannelPermissionOverwritesUpdate(event)
            is TextChannelSlowModeUpdateEvent -> onTextChannelSlowModeUpdate(event)
            is CategoryNameUpdateEvent -> onCategoryNameUpdate(event)
            is StateChannelTopicUpdateEvent -> onStateChannelTopicUpdate(event)
            is VoiceChannelBitrateUpdateEvent -> onVoiceChannelBitrateUpdate(event)
            is VoiceChannelUserLimitUpdateEvent -> onVoiceChannelUserLimitUpdate(event)
            is VoiceChannelRateLimitPerUserUpdateEvent ->
                onVoiceChannelRateLimitPerUserUpdate(event)
            is VoiceChannelNameUpdateEvent -> onVoiceChannelNameUpdate(event)
            is ForumChannelNameUpdateEvent -> onForumChannelNameUpdate(event)
            is ForumChannelTemplateUpdateEvent -> onForumChannelTemplateUpdate(event)
            is ForumChannelRateLimitPerUserUpdateEvent ->
                onForumChannelRateLimitPerUserUpdate(event)
            is ForumChannelPermissionOverwritesUpdateEvent ->
                onForumChannelPermissionOverwritesUpdate(event)
            is ForumChannelNsfwUpdateEvent -> onForumChannelNsfwUpdate(event)
            is ForumChannelLastMessageIdUpdateEvent -> onForumChannelLastMessageIdUpdate(event)
            is ForumChannelChannelFlagsUpdateEvent -> onForumChannelChannelFlagsUpdate(event)
            is ForumChannelDefaultSortOrderUpdateEvent ->
                onForumChannelDefaultSortOrderUpdate(event)
            is ForumChannelDefaultReactionEmojiUpdateEvent ->
                onForumChannelDefaultReactionEmojiUpdate(event)
            is ForumChannelAvailableForumTagsUpdateEvent ->
                onForumChannelAvailableForumTagsUpdate(event)
            is ForumChannelAvailableForumTagsIdsUpdateEvent ->
                onForumChannelAvailableForumTagsIdsUpdate(event)
            is ForumChannelDefaultForumLayoutViewUpdateEvent ->
                onForumChannelDefaultForumLayoutViewUpdate(event)
        }
    }
}
