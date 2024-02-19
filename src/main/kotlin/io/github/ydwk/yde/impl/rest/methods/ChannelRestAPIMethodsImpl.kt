/*
 * Copyright 2023 YDWK inc.
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

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.impl.entities.channel.DmChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildChannelImpl
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.methods.ChannelRestAPIMethods
import kotlinx.coroutines.CompletableDeferred

class ChannelRestAPIMethodsImpl(val yde: YDE) : ChannelRestAPIMethods {
    override fun requestChannel(channelId: Long): CompletableDeferred<Channel> {
        return this.yde.restApiManager
            .get(EndPoint.ChannelEndpoint.GET_CHANNEL, channelId.toString())
            .execute() {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    val channelType = ChannelType.fromInt(jsonBody["type"].asInt())
                    if (ChannelType.isGuildChannel(channelType)) {
                        GuildChannelImpl(yde, jsonBody, jsonBody["id"].asLong())
                    } else {
                        DmChannelImpl(yde, jsonBody, jsonBody["id"].asLong())
                    }
                }
            }
    }

    override fun requestGuildChannel(
        guildId: Long,
        channelId: Long
    ): CompletableDeferred<GuildChannel> {
        // TODO : Have a look at this
        return this.yde.restApiManager
            .get(EndPoint.ChannelEndpoint.GET_CHANNEL, channelId.toString())
            .execute() {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    GuildChannelImpl(yde, jsonBody, jsonBody["id"].asLong())
                }
            }
    }

    override fun requestGuildChannels(guildId: Long): CompletableDeferred<List<GuildChannel>> {
        return this.yde.restApiManager
            .get(EndPoint.GuildEndpoint.GET_GUILD_CHANNELS, guildId.toString())
            .execute { it ->
                val jsonBody = it.jsonBody
                jsonBody?.map { GuildChannelImpl(yde, it, it["id"].asLong()) }
                    ?: throw IllegalStateException("json body is null")
            }
    }
}
