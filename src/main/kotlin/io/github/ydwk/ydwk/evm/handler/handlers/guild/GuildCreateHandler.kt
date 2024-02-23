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
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Sticker
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.impl.entities.ChannelImpl
import io.github.ydwk.yde.impl.entities.EmojiImpl
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.yde.impl.entities.StickerImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildChannelImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl
import io.github.ydwk.yde.impl.entities.guild.RoleImpl
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import java.util.*

class GuildCreateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val guild: Guild = ydwk.entityInstanceBuilder.buildGuild(json)

        if (ydwk.cache.contains(guild.id, CacheIds.GUILD)) {
            ydwk.logger.warn(
                "Guild with id ${guild.idAsLong} already exists in cache, will replace it")
            ydwk.cache.remove(guild.id, CacheIds.GUILD)
        }

        ydwk.cache[guild.id, guild] = CacheIds.GUILD

        val members: ArrayList<Member> = ArrayList()
        json["members"].forEach { member -> members.add(MemberImpl(ydwk, member, guild)) }

        members.forEach { member -> member.user.let { ydwk.memberCache[guild.id, it.id] = member } }

        val roles = ArrayList<Role>()
        json["roles"].forEach { role -> roles.add(RoleImpl(ydwk, role, role.get("id").asLong())) }

        roles.forEach { role -> ydwk.cache[role.id, role] = CacheIds.ROLE }

        val emojis = ArrayList<Emoji>()
        json["emojis"].forEach { emoji -> emojis.add(EmojiImpl(ydwk, emoji)) }

        emojis.forEach { emoji ->
            if (emoji.idLong != null) {
                ydwk.cache[emoji.id!!, emoji] = CacheIds.EMOJI
            }
        }

        val stickers = ArrayList<Sticker>()
        json["stickers"].forEach { sticker ->
            stickers.add(StickerImpl(ydwk, sticker, sticker["id"].asLong()))
        }

        stickers.forEach { sticker -> ydwk.cache[sticker.id, sticker] = CacheIds.STICKER }

        val channelJson = json["channels"]
        val channelType: EnumSet<ChannelType> =
            channelJson
                .map { ChannelType.fromInt(it["type"].asInt()) }
                .toCollection(EnumSet.noneOf(ChannelType::class.java))

        val guildChannels = ArrayList<GuildChannel>()
        val nonGuildChannels = ArrayList<Channel>()
        channelType.forEach {
            when {
                it.isGuildChannel -> {
                    channelJson.forEach { channel ->
                        if (channel["type"].asInt() == it.getId()) {
                            guildChannels.add(
                                GuildChannelImpl(ydwk, channel, channel["id"].asLong()))
                        }
                    }
                }
                it.isNonGuildChannel -> {
                    channelJson.forEach { channel ->
                        if (channel["type"].asInt() == it.getId()) {
                            nonGuildChannels.add(
                                ChannelImpl(ydwk, channel, channel["id"].asLong(), false, true))
                        }
                    }
                }
            }
        }

        guildChannels.forEach { channel -> ydwk.cache[channel.id, channel] = CacheIds.CHANNEL }
        nonGuildChannels.forEach { channel -> ydwk.cache[channel.id, channel] = CacheIds.CHANNEL }
    }
}
