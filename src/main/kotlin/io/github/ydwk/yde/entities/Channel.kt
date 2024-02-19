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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.getter.ChannelGetter
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.SnowFlake

interface Channel : SnowFlake, GenericEntity {
    /**
     * Get the channel type.
     *
     * @return the channel type.
     */
    val type: ChannelType

    /**
     * Weather the channel is a guild channel.
     *
     * @return true if the channel is a guild channel.
     */
    val isGuildChannel: Boolean

    /**
     * Weather the channel is a dm channel.
     *
     * @return true if the channel is a dm channel.
     */
    val isDmChannel: Boolean

    /**
     * The channel getter which gives access to the channels.
     *
     * @return the channel getter which gives access to the channels.
     */
    val channelGetter: ChannelGetter
}
