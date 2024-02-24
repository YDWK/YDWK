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
package io.github.ydwk.ydwk.evm.handler.handlers.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.ydwk.evm.event.events.guild.GuildDeleteEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class GuildDeleteHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val unavailableGuild = ydwk.entityInstanceBuilder.buildUnavailableGuild(json)
        val guild: Guild = ydwk.cache[unavailableGuild.id, CacheIds.GUILD] as GuildImpl
        guild.roles.forEach { role -> ydwk.cache.remove(role.id, CacheIds.ROLE) }
        guild.emojis.forEach { emoji ->
            run {
                if (emoji.idLong != null) {
                    ydwk.cache.remove(emoji.id!!, CacheIds.EMOJI)
                }
            }
        }
        guild.stickers.forEach { sticker -> ydwk.cache.remove(sticker.id, CacheIds.STICKER) }
        ydwk.cache.remove(guild.id, CacheIds.GUILD)
        ydwk.emitEvent(GuildDeleteEvent(ydwk, guild))
    }
}
