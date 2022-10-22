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
package io.github.ydwk.ydwk.entities.channel

import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildTextChannel
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildVoiceChannel
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.util.NameAbleEntity

interface GuildChannel : Channel, NameAbleEntity {
    /**
     * Gets the guild of this channel.
     *
     * @return the guild of this channel.
     */
    val guild: Guild

    /**
     * Gets the channel as Guild Category.
     *
     * @return the channel as Guild Category.
     */
    fun asGuildCategory(): GuildCategory?

    /**
     * Gets the channel as a generic guild text channel.
     *
     * @return the channel as a generic guild text channel.
     */
    fun asGenericGuildTextChannel(): GenericGuildTextChannel

    /**
     * Gets the channel as a generic guild voice channel.
     *
     * @return the channel as a generic guild voice channel.
     */
    fun asGenericGuildVoiceChannel(): GenericGuildVoiceChannel
}
