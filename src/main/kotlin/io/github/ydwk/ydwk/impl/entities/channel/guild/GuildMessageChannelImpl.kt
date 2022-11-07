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
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.ydwk.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.ydwk.util.EntityToStringBuilder

open class GuildMessageChannelImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : GuildMessageChannel {

    override var topic: String = json["topic"].asText()

    override var nsfw: Boolean = json["nsfw"].asBoolean()

    override var defaultAutoArchiveDuration: Int = json["default_auto_archive_duration"].asInt()

    override var lastMessageId: String = json["last_message_id"].asText()

    override var lastPinTimestamp: String = json["last_pin_timestamp"].asText()

    override var permissionOverwrites: List<PermissionOverwrite> =
        json["permission_overwrites"].map { PermissionOverwriteImpl(ydwk, it, it["id"].asLong()) }
    override val type: ChannelType
        get() = if (json["type"].asInt() == 0) ChannelType.TEXT else ChannelType.NEWS

    override var position: Int = json["position"].asInt()

    override var parent: GuildCategory? = ydwk.getCategoryById(json["parent_id"].asText())

    override val guild: Guild
        get() =
            if (ydwk.getGuildById(json["guild_id"].asText()) != null)
                ydwk.getGuildById(json["guild_id"].asText())!!
            else throw IllegalStateException("Guild is null")

    override var name: String = json["name"].asText()

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).name(this.name).toString()
    }
}
