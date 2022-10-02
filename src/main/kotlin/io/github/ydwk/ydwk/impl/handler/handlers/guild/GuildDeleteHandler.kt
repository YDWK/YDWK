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
package io.github.ydwk.ydwk.impl.handler.handlers.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.cache.CacheType
import io.github.realyusufismail.ydwk.entities.Guild
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.impl.entities.GuildImpl
import io.github.realyusufismail.ydwk.impl.entities.UnavailableGuildImpl
import io.github.realyusufismail.ydwk.impl.handler.Handler

class GuildDeleteHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val unavailableGuild = UnavailableGuildImpl(ydwk, json, json.get("id").asLong())
        val guild: Guild = ydwk.cache[unavailableGuild.id, CacheType.GUILD] as GuildImpl
        guild.roles.forEach { role -> ydwk.cache.remove(role.id, CacheType.ROLE) }
        guild.emojis.forEach { emoji ->
            run {
                if (emoji.idLong != null) {
                    ydwk.cache.remove(emoji.id!!, CacheType.EMOJI)
                }
            }
        }
        guild.stickers.forEach { sticker -> ydwk.cache.remove(sticker.id, CacheType.STICKER) }
        ydwk.cache.remove(guild.id, CacheType.GUILD)
    }
}
