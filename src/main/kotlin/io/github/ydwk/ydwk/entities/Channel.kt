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
package io.github.ydwk.ydwk.entities

import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.util.AssignableEntity
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.SnowFlake

interface Channel : SnowFlake, GenericEntity, AssignableEntity<Channel> {
    /**
     * Get the channel type
     *
     * @return the channel type
     */
    val type: ChannelType

    /**
     * Gets the channel as a [GuildChannel] if it is one.
     *
     * @return the channel as a [GuildChannel] if it is one.
     */
    fun asGuildChannel(): GuildChannel? {
        return cast(GuildChannel::class.java)
    }

    /**
     * Gets the channel as a [TextChannel] if it is one.
     *
     * @return the channel as a [TextChannel] if it is one.
     */
    fun asTextChannel(): TextChannel? = cast(TextChannel::class.java)
}
