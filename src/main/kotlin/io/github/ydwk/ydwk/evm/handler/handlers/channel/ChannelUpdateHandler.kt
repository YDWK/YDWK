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
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.ydwk.entities.channel.guild.message.news.GuildNewsChannel
import io.github.ydwk.ydwk.entities.channel.guild.message.text.GuildTextChannel
import io.github.ydwk.ydwk.evm.event.events.channel.update.text.TextChannelSlowModeUpdateEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.channel.guild.GuildNewsChannelImpl
import io.github.ydwk.ydwk.impl.entities.channel.guild.GuildTextChannelImpl

class ChannelUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val type = ChannelType.fromId(json["type"].asInt())
        when (type) {
            ChannelType.TEXT -> updateTextChannel()
            ChannelType.DM -> ydwk.logger.warn("Dm is not supported")
            ChannelType.VOICE -> TODO()
            ChannelType.GROUP_DM -> ydwk.logger.warn("Group DMs are not supported.")
            ChannelType.CATEGORY -> TODO()
            ChannelType.NEWS -> updateNewsChannel()
            ChannelType.NEWS_THREAD -> TODO()
            ChannelType.PUBLIC_THREAD -> TODO()
            ChannelType.PRIVATE_THREAD -> TODO()
            ChannelType.STAGE_VOICE -> TODO()
            ChannelType.DIRECTORY -> TODO()
            ChannelType.FORUM -> TODO()
            ChannelType.UNKNOWN -> TODO()
        }
    }

    private fun updateTextChannel() {
        val channel: GuildTextChannel? = ydwk.getGuildTextChannelById(json["id"].asText())

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[json["id"].asText(), GuildTextChannelImpl(ydwk, json, json["id"].asLong())] =
                CacheIds.TEXT_CHANNEL
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
        val channel: GuildNewsChannel? = ydwk.getGuildNewsChannelById(json["id"].asText())

        if (channel == null) {
            ydwk.logger.info("Channel ${json["id"].asText()} is not cached, creating new one.")
            ydwk.cache[json["id"].asText(), GuildNewsChannelImpl(ydwk, json, json["id"].asLong())] =
                CacheIds.TEXT_CHANNEL
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
    private fun updateMessageChannel(channel: GuildMessageChannel) {}
}
