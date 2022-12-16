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
package io.github.ydwk.ydwk.entities.builder.guild

import io.github.ydwk.ydwk.entities.builder.guild.channel.MessageChannelBuilder
import io.github.ydwk.ydwk.entities.builder.guild.channel.VoiceChannelBuilder

interface ChannelBuilder {

    /**
     * Used to create a new message channel (text channel or announcement channel).
     *
     * @param name the name of the channel
     * @return a new channel builder
     */
    fun createMessageChannel(name: String): MessageChannelBuilder

    /**
     * Used to create a new voice channel.
     *
     * @param name the name of the channel
     * @return a new channel builder
     */
    fun createVoiceChannel(name: String): VoiceChannelBuilder
}