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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.evm.handler.handlers.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelDeleteEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.ChannelImpl
import io.github.ydwk.ydwk.impl.entities.channel.guild.GuildChannelImpl

class ChannelDeleteHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val channelType = ChannelType.fromInt(json.get("type").asInt())
        when {
            channelType.isGuildChannel -> {
                val channel = GuildChannelImpl(ydwk, json, json.get("id").asLong())
                ydwk.cache.remove(json.get("id").asText(), CacheIds.CHANNEL)
                ydwk.emitEvent(ChannelDeleteEvent(ydwk, channel))
            }
            channelType.isNonGuildChannel -> {
                val channel = ChannelImpl(ydwk, json, json.get("id").asLong(), false, true)
                ydwk.cache.remove(json.get("id").asText(), CacheIds.CHANNEL)
                ydwk.emitEvent(ChannelDeleteEvent(ydwk, channel))
            }
        }
    }
}
