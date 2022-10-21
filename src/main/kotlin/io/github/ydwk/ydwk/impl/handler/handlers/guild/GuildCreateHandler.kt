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
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.Emoji
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Sticker
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.channel.VoiceChannel
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.guild.Category
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.impl.YDWKWebSocketImpl
import io.github.ydwk.ydwk.impl.entities.EmojiImpl
import io.github.ydwk.ydwk.impl.entities.GuildImpl
import io.github.ydwk.ydwk.impl.entities.StickerImpl
import io.github.ydwk.ydwk.impl.entities.channel.CategoryImpl
import io.github.ydwk.ydwk.impl.entities.channel.TextChannelImpl
import io.github.ydwk.ydwk.impl.entities.channel.VoiceChannelImpl
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.impl.entities.guild.RoleImpl
import io.github.ydwk.ydwk.impl.handler.Handler
import java.util.*

class GuildCreateHandler(ydwk: YDWKWebSocketImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val guild: Guild = GuildImpl(ydwk, json, json["id"].asLong())

        if (ydwk.cache.contains(guild.id, CacheIds.GUILD)) {
            ydwk.logger.warn(
                "Guild with id ${guild.idAsLong} already exists in cache, will replace it")
            ydwk.cache.remove(guild.id, CacheIds.GUILD)
        }

        ydwk.cache[guild.id, guild] = CacheIds.GUILD

        val members: ArrayList<Member> = ArrayList()
        json["members"].forEach { member -> members.add(MemberImpl(ydwk, member, guild)) }

        members.forEach { member ->
            member.user?.let { ydwk.memberCache[it.id, member.guild.id] = member }
        }

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
                .map { ChannelType.fromId(it["type"].asInt()) }
                .toCollection(EnumSet.noneOf(ChannelType::class.java))

        val textChannel = ArrayList<TextChannel>()
        val voiceChannel = ArrayList<VoiceChannel>()
        val category = ArrayList<Category>()
        channelType.forEach {
            when {
                it.isText -> {
                    channelJson.forEach { channel ->
                        if (channel["type"].asInt() == it.getId()) {
                            textChannel.add(TextChannelImpl(ydwk, channel, channel["id"].asLong()))
                        }
                    }
                }
                it.isVoice -> {
                    channelJson.forEach { channel ->
                        if (channel["type"].asInt() == it.getId()) {
                            voiceChannel.add(
                                VoiceChannelImpl(ydwk, channel, channel["id"].asLong()))
                        }
                    }
                }
                it.isCategory -> {
                    channelJson.forEach { channel ->
                        if (channel["type"].asInt() == it.getId()) {
                            category.add(CategoryImpl(ydwk, channel, channel["id"].asLong()))
                        }
                    }
                }
            }
        }

        if (textChannel.isNotEmpty()) {
            textChannel.forEach { channel ->
                ydwk.cache[channel.id, channel] = CacheIds.TEXT_CHANNEL
            }
        }

        if (voiceChannel.isNotEmpty()) {
            voiceChannel.forEach { channel ->
                ydwk.cache[channel.id, channel] = CacheIds.VOICE_CHANNEL
            }
        }

        if (category.isNotEmpty()) {
            category.forEach { channel -> ydwk.cache[channel.id, channel] = CacheIds.CATEGORY }
        }
    }
}
