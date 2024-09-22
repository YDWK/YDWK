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
package io.github.ydwk.ydwk.evm.handler.handlers.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.entities.channel.guild.forum.ChannelFlag
import io.github.ydwk.yde.entities.channel.guild.forum.ForumLayoutType
import io.github.ydwk.yde.entities.channel.guild.forum.GuildForumChannel
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.yde.entities.channel.guild.message.news.GuildNewsChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.GuildTextChannel
import io.github.ydwk.yde.entities.channel.guild.vc.GuildStageChannel
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.impl.entities.channel.guild.*
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.channel.update.category.CategoryNameUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.forum.*
import io.github.ydwk.ydwk.evm.event.events.channel.update.guild.GuildChannelParentUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.guild.GuildChannelPositionUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.message.*
import io.github.ydwk.ydwk.evm.event.events.channel.update.stage.StateChannelTopicUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.text.TextChannelSlowModeUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelBitrateUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelRateLimitPerUserUpdateEvent
import io.github.ydwk.ydwk.evm.event.events.channel.update.voice.VoiceChannelUserLimitUpdateEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.util.ydwk
import java.util.*

class ChannelUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        when (ChannelType.getValue(json["type"].asInt())) {
            ChannelType.TEXT -> updateTextChannel()
            ChannelType.DM -> ydwk.logger.warn("Dm is not supported")
            ChannelType.VOICE -> updateVoiceChannel()
            ChannelType.GROUP_DM -> ydwk.logger.warn("Group DMs are not supported.")
            ChannelType.CATEGORY -> updateCategory()
            ChannelType.NEWS -> updateNewsChannel()
            // TODO: Add support for thread channels
            ChannelType.NEWS_THREAD -> ydwk.logger.warn("News threads are not supported yet.")
            ChannelType.PUBLIC_THREAD -> ydwk.logger.warn("Public threads are not supported yet.")
            ChannelType.PRIVATE_THREAD -> ydwk.logger.warn("Private threads are not supported yet.")
            ChannelType.STAGE_VOICE -> updateStageChannel()
            // TODO: Add support for DIRECTORY
            ChannelType.DIRECTORY -> ydwk.logger.warn("Directories are not supported.")
            ChannelType.FORUM -> updateForumChannel()
            ChannelType.UNKNOWN -> ydwk.logger.warn("Unknown channel type ${json["type"].asInt()}")
        }
    }

    private fun updateTextChannel() {
        val channel: GuildTextChannel? =
            ydwk
                .getGuildChannelById(json["id"].asText())
                ?.guildChannelGetter
                ?.asGuildMessageChannel()
                ?.guildMessageChannelGetter
                ?.asGuildTextChannel()

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[
                    json["id"].asText(), ydwk.entityInstanceBuilder.buildGuildTextChannel(json)] =
                CacheIds.CHANNEL
            return
        }

        ydwk.logger.debug("Updating channel ${channel.id}")

        updateMessageChannel(channel)
        val oldSlowMode = channel.rateLimitPerUser
        val newSlowMode = json["rate_limit_per_user"].asInt()
        if (oldSlowMode != newSlowMode) {
            channel.rateLimitPerUser = newSlowMode
            ydwk.emitEvent(
                TextChannelSlowModeUpdateEvent(
                    ydwk, channel, channel.type, oldSlowMode, newSlowMode))
        }
    }

    private fun updateNewsChannel() {
        val channel: GuildNewsChannel? =
            ydwk
                .getGuildChannelById(json["id"].asText())
                ?.guildChannelGetter
                ?.asGuildMessageChannel()
                ?.guildMessageChannelGetter
                ?.asGuildNewsChannel()

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[
                    json["id"].asText(), ydwk.entityInstanceBuilder.buildGuildNewsChannel(json)] =
                CacheIds.CHANNEL
            return
        }

        ydwk.logger.debug("Updating channel ${channel.id}")

        updateMessageChannel(channel)
    }

    /**
     * This is only for guild text channel and guild news channel.
     *
     * @param channel the channel to update.
     */
    private fun updateMessageChannel(channel: GuildMessageChannel) {
        val oldName = channel.name
        val newName = json["name"].asText()
        if (!Objects.deepEquals(oldName, newName)) {
            channel.name = newName
            ydwk.emitEvent(MessageChannelNameUpdateEvent(ydwk, channel, oldName, newName))
        }

        val oldTopic = channel.topic
        val newTopic = json["topic"].asText()
        if (!Objects.deepEquals(oldTopic, newTopic)) {
            channel.topic = newTopic
            ydwk.emitEvent(MessageChannelTopicUpdateEvent(ydwk, channel, oldTopic, newTopic))
        }

        val wasNsfw = channel.nsfw
        val isNsfw = json["nsfw"].asBoolean()
        if (wasNsfw != isNsfw) {
            channel.nsfw = isNsfw
            ydwk.emitEvent(MessageChannelNsfwUpdateEvent(ydwk, channel, wasNsfw, isNsfw))
        }

        val oldDefaultAutoArchiveDuration = channel.defaultAutoArchiveDuration
        val newDefaultAutoArchiveDuration = json["default_auto_archive_duration"].asInt()
        if (oldDefaultAutoArchiveDuration != newDefaultAutoArchiveDuration) {
            channel.defaultAutoArchiveDuration = newDefaultAutoArchiveDuration
            ydwk.emitEvent(
                MessageChannelDefaultAutoArchiveDurationUpdateEvent(
                    ydwk, channel, oldDefaultAutoArchiveDuration, newDefaultAutoArchiveDuration))
        }

        val oldLastMessageId = channel.lastMessageId
        val newLastMessageId = json["last_message_id"].asText()
        if (!Objects.deepEquals(oldLastMessageId, newLastMessageId)) {
            channel.lastMessageId = newLastMessageId
            ydwk.emitEvent(
                MessageChannelLastMessageIdUpdateEvent(
                    ydwk, channel, oldLastMessageId, newLastMessageId))
        }

        val oldLastPinTimestamp = channel.lastPinTimestamp
        val newLastPinTimestamp = json["last_pin_timestamp"].asText() ?: null
        if (!Objects.deepEquals(oldLastPinTimestamp, newLastPinTimestamp)) {
            channel.lastPinTimestamp = newLastPinTimestamp
            ydwk.emitEvent(
                MessageChannelLastPinTimestampUpdateEvent(
                    ydwk, channel, oldLastPinTimestamp, newLastPinTimestamp))
        }

        val oldPermissionOverwrites = channel.permissionOverwrites
        val newPermissionOverwrites =
            json["permission_overwrites"].map {
                ydwk.entityInstanceBuilder.buildPermissionOverwrite(it)
            }
        if (!Objects.deepEquals(oldPermissionOverwrites, newPermissionOverwrites)) {
            channel.permissionOverwrites = newPermissionOverwrites
            ydwk.emitEvent(
                MessageChannelPermissionOverwritesUpdateEvent(
                    ydwk, channel, oldPermissionOverwrites, newPermissionOverwrites))
        }

        updateGuildChannel(channel)
    }

    private fun updateCategory() {
        val channel: GuildCategory? =
            ydwk.getGuildChannelById(json["id"].asText())?.guildChannelGetter?.asGuildCategory()

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[json["id"].asText(), ydwk.entityInstanceBuilder.buildGuildCategory(json)] =
                CacheIds.CHANNEL
            return
        }

        ydwk.logger.debug("Updating channel ${channel.id}")

        val oldName = channel.name
        val newName = json["name"].asText()
        if (!Objects.deepEquals(oldName, newName)) {
            channel.name = newName
            ydwk.emitEvent(CategoryNameUpdateEvent(ydwk, channel, oldName, newName))
        }

        updateGuildChannel(channel)
    }

    private fun updateGuildChannel(channel: GuildChannel) {
        val oldPosition = channel.position
        val newPosition = json["position"].asInt()
        if (oldPosition != newPosition) {
            channel.position = newPosition
            ydwk.emitEvent(
                GuildChannelPositionUpdateEvent(
                    ydwk, channel, channel.type, oldPosition, newPosition))
        }

        val oldParentId: GetterSnowFlake? = channel.parentId
        val newParentId = json["parent_id"]?.asText()?.let { GetterSnowFlake.of(it.toLong()) }
        if (!Objects.deepEquals(oldParentId, newParentId)) {
            channel.parentId = newParentId
            ydwk.emitEvent(
                GuildChannelParentUpdateEvent(
                    ydwk, channel, channel.type, oldParentId, newParentId))
        }
    }

    private fun updateStageChannel() {
        val channel: GuildStageChannel? =
            ydwk.getGuildChannelById(json["id"].asText())?.guildChannelGetter?.asGuildStageChannel()

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[
                    json["id"].asText(), ydwk.entityInstanceBuilder.buildGuildStageChannel(json)] =
                CacheIds.CHANNEL
            return
        }

        ydwk.logger.debug("Updating channel ${channel.id}")

        val oldTopic = channel.topic
        val newTopic = json["topic"].asText()
        if (!Objects.deepEquals(oldTopic, newTopic)) {
            channel.topic = newTopic
            ydwk.emitEvent(StateChannelTopicUpdateEvent(ydwk, channel, oldTopic, newTopic))
        }

        updateVoiceChannel(channel)
    }

    private fun updateVoiceChannel() {
        val channel: GuildVoiceChannel? =
            ydwk.getGuildChannelById(json["id"].asText())?.guildChannelGetter?.asGuildVoiceChannel()

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[
                    json["id"].asText(), ydwk.entityInstanceBuilder.buildGuildVoiceChannel(json)] =
                CacheIds.CHANNEL
            return
        }

        ydwk.logger.debug("Updating channel ${channel.id}")

        updateVoiceChannel(channel)
    }

    private fun updateVoiceChannel(channel: GuildVoiceChannel) {
        val oldBitrate = channel.bitrate
        val newBitrate = json["bitrate"].asInt()
        if (oldBitrate != newBitrate) {
            channel.bitrate = newBitrate
            ydwk.emitEvent(VoiceChannelBitrateUpdateEvent(ydwk, channel, oldBitrate, newBitrate))
        }

        val oldUserLimit = channel.userLimit
        val newUserLimit = json["user_limit"].asInt()
        if (oldUserLimit != newUserLimit) {
            channel.userLimit = newUserLimit
            ydwk.emitEvent(
                VoiceChannelUserLimitUpdateEvent(ydwk, channel, oldUserLimit, newUserLimit))
        }

        val oldRateLimitPerUser = channel.rateLimitPerUser
        val newRateLimitPerUser = json["rate_limit_per_user"].asInt()
        if (oldRateLimitPerUser != newRateLimitPerUser) {
            channel.rateLimitPerUser = newRateLimitPerUser
            ydwk.emitEvent(
                VoiceChannelRateLimitPerUserUpdateEvent(
                    ydwk, channel, oldRateLimitPerUser, newRateLimitPerUser))
        }

        updateGuildChannel(channel)
    }

    private fun updateForumChannel() {
        val channel: GuildForumChannel? =
            ydwk.getGuildChannelById(json["id"].asText())?.guildChannelGetter?.asGuildForumChannel()

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[
                    json["id"].asText(), ydwk.entityInstanceBuilder.buildGuildForumChannel(json)] =
                CacheIds.CHANNEL
            return
        }

        ydwk.logger.debug("Updating channel ${channel.id}")

        val oldName = channel.name
        val newName = json["name"].asText()
        if (!Objects.deepEquals(oldName, newName)) {
            channel.name = newName
            ydwk.emitEvent(ForumChannelNameUpdateEvent(ydwk, channel, oldName, newName))
        }

        val oldTopic = channel.topic
        val newTopic = if (json.hasNonNull("topic")) json["topic"].asText() else null
        if (!Objects.deepEquals(oldTopic, newTopic)) {
            channel.topic = newTopic
            ydwk.emitEvent(ForumChannelTopicUpdateEvent(ydwk, channel, oldTopic, newTopic))
        }

        val oldTemplate = channel.template
        val newTemplate = if (json.hasNonNull("template")) json["template"].asText() else null
        if (!Objects.deepEquals(oldTemplate, newTemplate)) {
            channel.template = newTemplate
            ydwk.emitEvent(ForumChannelTemplateUpdateEvent(ydwk, channel, oldTemplate, newTemplate))
        }

        val oldRateLimitPerUser = channel.rateLimitPerUser
        val newRateLimitPerUser = json["rate_limit_per_user"].asInt()
        if (oldRateLimitPerUser != newRateLimitPerUser) {
            channel.rateLimitPerUser = newRateLimitPerUser
            ydwk.emitEvent(
                ForumChannelRateLimitPerUserUpdateEvent(
                    ydwk, channel, oldRateLimitPerUser, newRateLimitPerUser))
        }

        val oldPermissionOverwrites = channel.permissionOverwrites
        val newPermissionOverwrites =
            json["permission_overwrites"].map {
                ydwk.entityInstanceBuilder.buildPermissionOverwrite(it)
            }
        if (!Objects.deepEquals(oldPermissionOverwrites, newPermissionOverwrites)) {
            channel.permissionOverwrites = newPermissionOverwrites
            ydwk.emitEvent(
                ForumChannelPermissionOverwritesUpdateEvent(
                    ydwk, channel, oldPermissionOverwrites, newPermissionOverwrites))
        }

        val wasNsfw = channel.nsfw
        val isNsfw = json["nsfw"].asBoolean()
        if (wasNsfw != isNsfw) {
            channel.nsfw = isNsfw
            ydwk.emitEvent(ForumChannelNsfwUpdateEvent(ydwk, channel, wasNsfw, isNsfw))
        }

        val oldLastMessageId = channel.lastMessageId
        val newLastMessageId =
            if (json.hasNonNull("last_message_id")) json["last_message_id"].asText() else null
        if (!Objects.deepEquals(oldLastMessageId, newLastMessageId)) {
            channel.lastMessageId = newLastMessageId?.let { GetterSnowFlake.of(it) }
            ydwk.emitEvent(
                ForumChannelLastMessageIdUpdateEvent(
                    ydwk, channel, oldLastMessageId!!.asString, newLastMessageId))
        }

        val oldChannelFlags = channel.channelFlags.getValue()
        val newChannelFlags = json["channel_flags"].asLong()
        if (oldChannelFlags != newChannelFlags) {
            channel.channelFlags = ChannelFlag.getValue(newChannelFlags)
            ydwk.emitEvent(
                ForumChannelChannelFlagsUpdateEvent(
                    ydwk,
                    channel,
                    ChannelFlag.getValue(oldChannelFlags),
                    ChannelFlag.getValue(newChannelFlags)))
        }

        val oldDefaultSortOrder = channel.defaultSortOrder
        val newDefaultSortOrder =
            if (json.hasNonNull("default_sort_order")) json["default_sort_order"].asInt() else null
        if (oldDefaultSortOrder != newDefaultSortOrder) {
            channel.defaultSortOrder = newDefaultSortOrder
            ydwk.emitEvent(
                ForumChannelDefaultSortOrderUpdateEvent(
                    ydwk, channel, oldDefaultSortOrder, newDefaultSortOrder))
        }

        val oldDefaultReactionEmoji = channel.defaultReactionEmoji
        val newDefaultReactionEmoji =
            if (json.hasNonNull("default_reaction_emoji"))
                ydwk.entityInstanceBuilder.buildDefaultReactionEmoji(json["default_reaction_emoji"])
            else null
        if (!Objects.deepEquals(oldDefaultReactionEmoji, newDefaultReactionEmoji)) {
            channel.defaultReactionEmoji = newDefaultReactionEmoji
            ydwk.emitEvent(
                ForumChannelDefaultReactionEmojiUpdateEvent(
                    ydwk, channel, oldDefaultReactionEmoji, newDefaultReactionEmoji))
        }

        val oldAvailableForumTags = channel.availableForumTags
        val newAvailableForumTags =
            json["available_tags"].map { ydwk.entityInstanceBuilder.buildForumTag(it) }
        if (!Objects.deepEquals(oldAvailableForumTags, newAvailableForumTags)) {
            channel.availableForumTags = newAvailableForumTags
            ydwk.emitEvent(
                ForumChannelAvailableForumTagsUpdateEvent(
                    ydwk, channel, oldAvailableForumTags, newAvailableForumTags))
        }

        val oldAvailableForumTagsIds = channel.availableForumTagsIds
        val newAvailableForumTagsIds =
            json["available_forum_tags"].map { GetterSnowFlake.of(it["id"].asLong()) }
        if (!Objects.deepEquals(oldAvailableForumTagsIds, newAvailableForumTagsIds)) {
            channel.availableForumTagsIds = newAvailableForumTagsIds
            ydwk.emitEvent(
                ForumChannelAvailableForumTagsIdsUpdateEvent(
                    ydwk, channel, oldAvailableForumTagsIds, newAvailableForumTagsIds))
        }

        val oldDefaultForumLayoutView = channel.defaultForumLayoutView
        val newDefaultForumLayoutView =
            ForumLayoutType.getValue(json["default_forum_layout"].asInt())
        if (oldDefaultForumLayoutView.getValue() != newDefaultForumLayoutView.getValue()) {
            channel.defaultForumLayoutView = newDefaultForumLayoutView
            ydwk.emitEvent(
                ForumChannelDefaultForumLayoutViewUpdateEvent(
                    ydwk, channel, oldDefaultForumLayoutView, newDefaultForumLayoutView))
        }

        updateGuildChannel(channel)
    }
}
