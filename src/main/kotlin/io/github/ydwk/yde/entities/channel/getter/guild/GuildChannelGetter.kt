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
package io.github.ydwk.yde.entities.channel.getter.guild

import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.entities.channel.guild.forum.GuildForumChannel
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.yde.entities.channel.guild.vc.GuildStageChannel
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel

interface GuildChannelGetter {

    /**
     * The channel as a text channel.
     *
     * @return the channel as a text channel.
     */
    fun asGuildMessageChannel(): GuildMessageChannel?

    /**
     * The channel as a guild voice channel.
     *
     * @return the channel as a guild voice channel.
     */
    fun asGuildVoiceChannel(): GuildVoiceChannel?

    /**
     * The channel as a guild stage channel.
     *
     * @return the channel as a guild stage channel.
     */
    fun asGuildStageChannel(): GuildStageChannel?

    /**
     * The channel as a guild category.
     *
     * @return the channel as a guild category.
     */
    fun asGuildCategory(): GuildCategory?

    /**
     * The channel as a guild forum channel.
     *
     * @return the channel as a guild forum channel.
     */
    fun asGuildForumChannel(): GuildForumChannel?
}
