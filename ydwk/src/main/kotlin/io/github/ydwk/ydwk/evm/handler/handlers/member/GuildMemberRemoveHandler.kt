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
package io.github.ydwk.ydwk.evm.handler.handlers.member

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.member.GuildMemberRemoveEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class GuildMemberRemoveHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val guild = ydwk.getGuildById(json.get("guild_id").asLong())
        if (guild != null) {
            val member =
                ydwk.entityInstanceBuilder.buildMember(json, GetterSnowFlake.of(guild.idAsLong))
            ydwk.memberCache.remove(guild.id, json.get("user").get("id").asText())
            ydwk.emitEvent(GuildMemberRemoveEvent(ydwk, member))
        } else {
            ydwk.logger.warn("Guild is null")
        }
    }
}
