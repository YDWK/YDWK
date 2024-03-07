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
package io.github.ydwk.yde.rest.methods

import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.rest.RestResult

interface ChannelRestAPIMethods {
    /**
     * Request to get a channel by its id.
     *
     * @param channelId the id of the channel
     * @return a [RestResult] that will contain the channel
     */
    suspend fun requestChannel(channelId: Long): RestResult<Channel>

    /**
     * Request to get a guild channel by its id.
     *
     * @param guildId the id of the guild
     * @param channelId the id of the channel
     * @return a [RestResult] that will contain the channel
     */
    suspend fun requestGuildChannel(guildId: Long, channelId: Long): RestResult<GuildChannel>

    /**
     * Request to get a list of guild channels.
     *
     * @param guildId the id of the guild
     * @return a [RestResult] that will contain the list of channels
     */
    suspend fun requestGuildChannels(guildId: Long): RestResult<List<GuildChannel>>
}
