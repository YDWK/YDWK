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
package io.github.ydwk.ydwk.evm.handler.handlers.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.ydwk.entities.channel.guild.message.news.GuildNewsChannel
import io.github.ydwk.ydwk.entities.channel.guild.message.text.GuildTextChannel
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildStageChannel
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.ydwk.evm.event.events.channel.update.category.CategoryNameUpdateEvent
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
import io.github.ydwk.ydwk.impl.entities.channel.guild.*
import java.util.*

class ChannelUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        when (ChannelType.fromId(json["type"].asInt())) {
            ChannelType.TEXT -> updateTextChannel()
            ChannelType.DM -> ydwk.logger.warn("Dm is not supported")
            ChannelType.VOICE -> updateVoiceChannel()
            ChannelType.GROUP_DM -> ydwk.logger.warn("Group DMs are not supported.")
            ChannelType.CATEGORY -> updateCategory()
            ChannelType.NEWS -> updateNewsChannel()
            ChannelType.NEWS_THREAD -> ydwk.logger.warn("News threads are not supported yet.")
            ChannelType.PUBLIC_THREAD -> ydwk.logger.warn("Public threads are not supported yet.")
            ChannelType.PRIVATE_THREAD -> ydwk.logger.warn("Private threads are not supported yet.")
            ChannelType.STAGE_VOICE -> updateStageChannel()
            ChannelType.DIRECTORY -> ydwk.logger.warn("Directories are not supported.")
            ChannelType.FORUM -> ydwk.logger.warn("Forums are not supported yet.")
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
            ydwk.cache[json["id"].asText(), GuildTextChannelImpl(ydwk, json, json["id"].asLong())] =
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
            ydwk.cache[json["id"].asText(), GuildNewsChannelImpl(ydwk, json, json["id"].asLong())] =
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
        val newLastPinTimestamp = json["last_pin_timestamp"].asText()
        if (!Objects.deepEquals(oldLastPinTimestamp, newLastPinTimestamp)) {
            channel.lastPinTimestamp = newLastPinTimestamp
            ydwk.emitEvent(
                MessageChannelLastPinTimestampUpdateEvent(
                    ydwk, channel, oldLastPinTimestamp, newLastPinTimestamp))
        }

        val oldPermissionOverwrites = channel.permissionOverwrites
        val newPermissionOverwrites =
            json["permission_overwrites"].map {
                PermissionOverwriteImpl(ydwk, it, it["id"].asLong())
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
            ydwk.cache[json["id"].asText(), GuildCategoryImpl(ydwk, json, json["id"].asLong())] =
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

        val oldParent: GuildCategory? = channel.parent
        val newParent =
            json["parent_id"]?.asText()?.let {
                ydwk.getGuildChannelById(json["id"].asText())?.guildChannelGetter?.asGuildCategory()
            }
        if (!Objects.deepEquals(oldParent, newParent)) {
            channel.parent = newParent
            ydwk.emitEvent(
                GuildChannelParentUpdateEvent(ydwk, channel, channel.type, oldParent, newParent))
        }
    }

    private fun updateStageChannel() {
        val channel: GuildStageChannel? =
            ydwk.getGuildChannelById(json["id"].asText())?.guildChannelGetter?.asGuildStageChannel()

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[
                    json["id"].asText(), GuildStageChannelImpl(ydwk, json, json["id"].asLong())] =
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
                    json["id"].asText(), GuildVoiceChannelImpl(ydwk, json, json["id"].asLong())] =
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
}
