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
package io.github.ydwk.ydwk.evm.handler.handlers.role

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.impl.YDWKImpl
import io.github.ydwk.yde.impl.entities.guild.RoleImpl
import io.github.ydwk.ydwk.evm.handler.Handler

class GuildRoleUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val role =
            ydwk
                .getGuildById(json.get("guild_id").asLong())
                ?.getRoleById(json.get("role").get("id").asLong())
        if (role == null) {
            ydwk.logger.info("Role not found in cache, creating new role")
            val roleImpl = RoleImpl(ydwk, json.get("role"), json.get("role").get("id").asLong())
            ydwk.cache[roleImpl.id, roleImpl] = CacheIds.ROLE
        }
    }
}
