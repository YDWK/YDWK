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
package io.github.ydwk.ydwk.impl.entities.channel.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.entities.channel.guild.text.GuildTextChannel
import io.github.ydwk.ydwk.entities.channel.guild.text.PermissionOverwrite

class GuildTextChannelImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : GuildTextChannel, GenericGuildTextChannelImpl(ydwk, json, idAsLong) {

    override val topic: String
        get() = json["topic"].asText()

    override val nsfw: Boolean
        get() = json["nsfw"].asBoolean()

    override val defaultAutoArchiveDuration: Int
        get() = json["default_auto_archive_duration"].asInt()

    override val rateLimitPerUser: Int
        get() = json["rate_limit_per_user"].asInt()

    override val lastMessageId: String
        get() = json["last_message_id"].asText()

    override val lastPinTimestamp: String
        get() = json["last_pin_timestamp"].asText()

    override val permissionOverwrites: List<PermissionOverwrite>
        get() =
            json["permission_overwrites"].map {
                PermissionOverwriteImpl(ydwk, it, it["id"].asLong())
            }

    override val position: Int
        get() = json["position"].asInt()

    override val parent: GuildCategory?
        get() = ydwk.getCategory(json["parent_id"].asText())

    override val guild: Guild
        get() =
            if (ydwk.getGuild(json["guild_id"].asText()) != null)
                ydwk.getGuild(json["guild_id"].asText())!!
            else throw IllegalStateException("Guild is null")

    override var name: String
        get() = json["name"].asText()
        set(value) {}
}
