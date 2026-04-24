/*
 * Copyright 2024-2026 YDWK inc.
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
package io.github.ydwk.ydwk.evm.handler.handlers.presence

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.presence.PresenceUpdateEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class PresenceUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val guildId = GetterSnowFlake.of(json.get("guild_id").asLong())
        val userId = GetterSnowFlake.of(json.get("user").get("id").asLong())
        val status = json.get("status").asText()
        val user = ydwk.getUserById(userId.asLong())
        ydwk.emitEvent(PresenceUpdateEvent(ydwk, guildId, userId, status, user))
    }
}
