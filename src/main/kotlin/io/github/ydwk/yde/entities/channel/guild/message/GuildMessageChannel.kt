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
package io.github.ydwk.yde.entities.channel.guild.message

import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.TextChannel
import io.github.ydwk.yde.entities.channel.getter.guild.GuildMessageChannelGetter
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite

interface GuildMessageChannel : TextChannel, GuildChannel {
    /**
     * The topic of this channel.
     *
     * @return the topic of this channel.
     */
    var topic: String

    /**
     * The nsfw flag of this channel.
     *
     * @return the nsfw flag of this channel.
     */
    var nsfw: Boolean

    /**
     * Get the default auto archive duration of this channel.
     *
     * @return the default auto archive duration of this channel.
     */
    var defaultAutoArchiveDuration: Int

    /**
     * The last message id of this channel.
     *
     * @return the last message id of this channel.
     */
    var lastMessageId: String

    /**
     * The last pinned message id of this channel.
     *
     * @return the last pinned message id of this channel.
     */
    var lastPinTimestamp: String

    /**
     * The permission overwrites of this channel.
     *
     * @return the permission overwrites of this channel.
     */
    var permissionOverwrites: List<PermissionOverwrite>

    /**
     * The guild message channel getter.
     *
     * @return the guild message channel getter.
     */
    val guildMessageChannelGetter: GuildMessageChannelGetter
}
