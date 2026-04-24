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
package io.github.ydwk.ydwk.evm.handler.handlers.role

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.ydwk.evm.event.events.role.GuildRoleDeleteEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

// Discord GUILD_ROLE_DELETE sends {"guild_id":"...","role_id":"..."} — no role object.
class GuildRoleDeleteHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val guildId = json.get("guild_id").asLong()
        val roleId = json.get("role_id").asText()
        val guild = ydwk.getGuildById(guildId)
        if (guild == null) {
            ydwk.logger.warn("GuildRoleDelete: guild $guildId not in cache")
            return
        }
        val role = guild.getRoleById(roleId)
        ydwk.cache.remove(roleId, CacheIds.ROLE)
        if (role != null) {
            ydwk.emitEvent(GuildRoleDeleteEvent(ydwk, role))
        } else {
            ydwk.logger.debug("GuildRoleDelete: role $roleId was not cached, event skipped")
        }
    }
}
