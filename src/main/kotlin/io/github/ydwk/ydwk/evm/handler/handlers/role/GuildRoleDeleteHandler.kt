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
package io.github.ydwk.ydwk.evm.handler.handlers.role

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.guild.RoleImpl

class GuildRoleDeleteHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val guild = ydwk.getGuildById(json.get("guild_id").asLong())
        if (guild != null) {
            // TODO: broken
            val role = RoleImpl(ydwk, json.get("role"), json.get("role").get("id").asLong())
            ydwk.cache.remove(json.get("role").get("id").asText(), CacheIds.ROLE)
            ydwk.emitEvent(
                io.github.ydwk.ydwk.evm.event.events.role.GuildRoleDeleteEvent(ydwk, role))
        } else {
            ydwk.logger.warn("Guild is null")
        }
    }
}