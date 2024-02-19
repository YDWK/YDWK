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
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel

interface VoiceChannelBuilder : GenericEntityBuilder<GuildVoiceChannel> {
    /**
     * Weather this channel is a voice channel or stage channel.
     *
     * @return true if this channel is a stage channel, false if this channel is a voice channel
     */
    fun isStageChannel(): VoiceChannelBuilder

    /**
     * Sets the bitrate of the channel.
     *
     * @param bitrate the bitrate of the channel
     * @return this builder
     */
    fun setBitrate(bitrate: Int): VoiceChannelBuilder

    /**
     * Sets the user limit of the channel.
     *
     * @param userLimit the user limit of the channel
     * @return this builder
     */
    fun setUserLimit(userLimit: Int): VoiceChannelBuilder

    /**
     * Sets the position of the channel.
     *
     * @param position the position of the channel
     * @return this builder
     */
    fun setPosition(position: Int): VoiceChannelBuilder

    /**
     * Sets the permission overwrites of the channel.
     *
     * @param permissionOverwrites the permission overwrites of the channel
     * @return this builder
     */
    fun setPermissionOverwrites(
        permissionOverwrites: List<PermissionOverwrite>,
    ): VoiceChannelBuilder

    /**
     * Sets the parent id of the channel.
     *
     * @param parentId the parent id of the channel
     * @return this builder
     */
    fun setParentId(parentId: String): VoiceChannelBuilder
}
