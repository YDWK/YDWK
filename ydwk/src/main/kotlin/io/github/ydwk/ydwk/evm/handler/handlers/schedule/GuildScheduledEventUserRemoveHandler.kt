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
package io.github.ydwk.ydwk.evm.handler.handlers.schedule

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.schedule.GuildScheduledEventUserRemoveEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class GuildScheduledEventUserRemoveHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val scheduledEventId = GetterSnowFlake.of(json.get("guild_scheduled_event_id").asLong())
        val guildId = GetterSnowFlake.of(json.get("guild_id").asLong())
        val userId = json.get("user_id").asLong()
        val user = ydwk.getUserById(userId)
        ydwk.emitEvent(GuildScheduledEventUserRemoveEvent(ydwk, scheduledEventId, guildId, user, null))
    }
}
