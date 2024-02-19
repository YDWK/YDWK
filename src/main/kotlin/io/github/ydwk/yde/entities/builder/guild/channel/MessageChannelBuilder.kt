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
package io.github.ydwk.yde.entities.builder.guild.channel

import io.github.ydwk.yde.entities.builder.GenericEntityBuilder
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite

interface MessageChannelBuilder : GenericEntityBuilder<GuildMessageChannel> {

    /**
     * Weather this channel is a text or news channel.
     *
     * @return true if this channel is a news channel, false if this channel is a text channel
     * @see MessageChannelBuilder
     */
    fun isNewsChannel(): MessageChannelBuilder

    /**
     * Sets the topic of the channel.
     *
     * @param topic the topic of the channel
     * @return this builder
     */
    fun setTopic(topic: String): MessageChannelBuilder

    /**
     * Sets the rate limit per user of the channel.
     *
     * @param rateLimitPerUser the rate limit per user of the channel
     * @return this builder
     */
    fun setRateLimitPerUser(rateLimitPerUser: Int): MessageChannelBuilder

    /**
     * Sets the position of the channel.
     *
     * @param position the position of the channel
     * @return this builder
     */
    fun setPosition(position: Int): MessageChannelBuilder

    /**
     * Sets the permission overwrites of the channel.
     *
     * @param permissionOverwrites the permission overwrites of the channel
     * @return this builder
     */
    fun setPermissionOverwrites(
        permissionOverwrites: List<PermissionOverwrite>,
    ): MessageChannelBuilder

    /**
     * Sets the parent id of the channel.
     *
     * @param parentId the parent id of the channel
     * @return this builder
     */
    fun setParentId(parentId: String): MessageChannelBuilder

    /**
     * Sets the nsfw flag of the channel.
     *
     * @param nsfw the nsfw flag of the channel
     * @return this builder
     */
    fun setNsfw(nsfw: Boolean): MessageChannelBuilder
}
