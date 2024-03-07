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
package io.github.ydwk.yde.impl.rest.methods

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.json
import io.github.ydwk.yde.rest.methods.ChannelRestAPIMethods

class ChannelRestAPIMethodsImpl(val yde: YDE) : ChannelRestAPIMethods {
    override suspend fun requestChannel(channelId: Long): RestResult<Channel> {
        return yde.restApiManager
            .get(EndPoint.ChannelEndpoint.GET_CHANNEL, channelId.toString())
            .execute { response ->
                val jsonBody = response.json(yde)
                buildChannelFromJson(jsonBody)
            }
    }

    override suspend fun requestGuildChannel(
        guildId: Long,
        channelId: Long
    ): RestResult<GuildChannel> {
        return yde.restApiManager
            .get(EndPoint.ChannelEndpoint.GET_CHANNEL, channelId.toString())
            .execute { response ->
                val jsonBody = response.json(yde)
                yde.entityInstanceBuilder.buildGuildChannel(jsonBody)
            }
    }

    override suspend fun requestGuildChannels(guildId: Long): RestResult<List<GuildChannel>> {
        return yde.restApiManager
            .get(EndPoint.GuildEndpoint.GET_GUILD_CHANNELS, guildId.toString())
            .execute { response ->
                val jsonBody = response.json(yde)
                buildGuildChannelsFromJson(jsonBody)
            }
    }

    private fun buildChannelFromJson(jsonBody: JsonNode): Channel {
        val channelType = ChannelType.getValue(jsonBody["type"].asInt())
        return if (ChannelType.isGuildChannel(channelType)) {
            yde.entityInstanceBuilder.buildGuildChannel(jsonBody)
        } else {
            yde.entityInstanceBuilder.buildDMChannel(jsonBody)
        }
    }

    private fun buildGuildChannelsFromJson(jsonBody: JsonNode): List<GuildChannel> {
        val channels = mutableListOf<GuildChannel>()
        for (i in 0 until jsonBody.size()) {
            channels.add(yde.entityInstanceBuilder.buildGuildChannel(jsonBody[i]))
        }
        return channels
    }
}
