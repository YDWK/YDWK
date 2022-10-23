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
package io.github.ydwk.ydwk.impl.handler.handlers.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.event.events.channel.ChannelDeleteEvent
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.channel.guild.GenericGuildChannelImpl
import io.github.ydwk.ydwk.impl.handler.Handler

class ChannelDeleteHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val channelType = ChannelType.fromId(json.get("type").asInt())
        when {
            channelType.isGuildText -> {
                val channel = GenericGuildChannelImpl(ydwk, json, json.get("id").asLong(), true)
                ydwk.cache.remove(json.get("id").asText(), CacheIds.TEXT_CHANNEL)
                ydwk.emitEvent(ChannelDeleteEvent(ydwk, channel))
            }
            channelType.isVoice -> {
                val channel =
                    GenericGuildChannelImpl(
                        ydwk, json, json.get("id").asLong(), isVoiceChannel = true)
                ydwk.cache.remove(json.get("id").asText(), CacheIds.VOICE_CHANNEL)
                ydwk.emitEvent(ChannelDeleteEvent(ydwk, channel))
            }
            channelType.isCategory -> {
                val channel =
                    GenericGuildChannelImpl(ydwk, json, json.get("id").asLong(), isCategory = true)
                ydwk.cache.remove(json.get("id").asText(), CacheIds.CATEGORY)
                ydwk.emitEvent(ChannelDeleteEvent(ydwk, channel))
            }
            channelType.isText -> {
                ydwk.logger.warn(
                    "Received a text channel create event, but it is not supported yet.")
            }
        }
    }
}
