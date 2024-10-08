/*
 * Copyright 2024 YDWK inc.
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
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelCreateEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class ChannelCreateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val channelType = ChannelType.getValue(json.get("type").asInt())
        when {
            channelType.isGuildChannel -> {
                val channel = ydwk.entityInstanceBuilder.buildGuildChannel(json)
                ydwk.cache[json.get("id").asText(), channel] = CacheIds.CHANNEL
                ydwk.emitEvent(ChannelCreateEvent(ydwk, channel))
            }
            channelType.isNonGuildChannel -> {
                val channel = ydwk.entityInstanceBuilder.buildChannel(json, false, true)
                ydwk.cache[json.get("id").asText(), channel] = CacheIds.CHANNEL
                ydwk.emitEvent(ChannelCreateEvent(ydwk, channel))
            }
        }
    }
}
