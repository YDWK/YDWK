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
package io.github.ydwk.ydwk.evm.handler.handlers.ws

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.evm.event.events.gateway.ReadyEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.BotImpl
import io.github.ydwk.ydwk.impl.entities.GuildImpl
import io.github.ydwk.ydwk.impl.entities.application.PartialApplicationImpl

class ReadyHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val bot = BotImpl(json.get("user"), json.get("user").get("id").asLong(), ydwk)
        ydwk.bot = bot
        ydwk.cache[json.get("user").get("id").asText(), bot] = CacheIds.USER

        val partialApplication =
            PartialApplicationImpl(
                json.get("application"), json.get("application").get("id").asLong(), ydwk)
        ydwk.applicationId = partialApplication.id
        ydwk.partialApplication = partialApplication
        ydwk.cache[json.get("application").get("id").asText(), partialApplication] =
            CacheIds.APPLICATION

        val guildArray: ArrayNode = json.get("guilds") as ArrayNode

        var availableGuildsAmount: Int = 0
        var unAvailableGuildsAmount: Int = 0

        for (guild in guildArray) {
            if (!guild.get("unavailable").asBoolean()) {
                availableGuildsAmount += 1
            } else {
                unAvailableGuildsAmount += 1
            }
        }

        val unavailableGuild: MutableList<Guild> = mutableListOf()
        val availableGuild: MutableList<Guild> = mutableListOf()
        for (guild in guildArray) {
            if (!guild.get("unavailable").asBoolean()) {
                availableGuild.add(GuildImpl(ydwk, guild, guild.get("id").asLong()))
            } else {
                unavailableGuild.add(requestGuild(guild.get("id").asLong()))
            }
        }

        availableGuild.forEach { ydwk.cache[it.id, it] = CacheIds.GUILD }

        unavailableGuild.forEach { ydwk.cache[it.id, it] = CacheIds.GUILD }

        ydwk.emitEvent(ReadyEvent(ydwk, availableGuildsAmount, unAvailableGuildsAmount))
    }

    private fun requestGuild(guildId: Long): Guild {
        val guild = ydwk.requestGuild(guildId).get()
        return GuildImpl(ydwk, guild.json, guildId)
    }
}
