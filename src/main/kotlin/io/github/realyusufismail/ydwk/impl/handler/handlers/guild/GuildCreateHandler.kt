/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.impl.handler.handlers.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.entities.guild.Guild
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.impl.entities.guild.GuildImpl
import io.github.realyusufismail.ydwk.impl.entities.guild.MemberImpl
import io.github.realyusufismail.ydwk.impl.handler.Handler

class GuildCreateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val guild: Guild = GuildImpl(ydwk, json, json["id"].asLong())

        if (ydwk.cache.contains(guild.idAsLong)) {
            ydwk.logger.warn(
                "Guild with id ${guild.idAsLong} already exists in cache, will replace it")
            ydwk.cache.remove(guild.idAsLong)
        }

        ydwk.cache[guild.idAsLong] = guild

        val members: ArrayList<Member> = ArrayList()
        json["members"].forEach { member -> members.add(MemberImpl(ydwk, member, guild)) }

        members.forEach { member ->
            member.user?.let { ydwk.memberCache[it.idAsLong, member.guild.idAsLong] = member }
        }
    }
}
