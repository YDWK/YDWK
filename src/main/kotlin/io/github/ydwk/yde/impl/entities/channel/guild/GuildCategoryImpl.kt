/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.impl.entities.channel.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.util.EntityToStringBuilder

class GuildCategoryImpl(yde: YDE, json: JsonNode, idAsLong: Long) :
    GuildCategory, GuildChannelImpl(yde, json, idAsLong) {

    override val channels: List<GuildChannel>
        get() = yde.getGuildChannels().filter { it.parent == this }

    override val nsfw: Boolean
        get() = json.has("nsfw") && json["nsfw"].asBoolean()

    override val guild: Guild = yde.getGuildById(json["guild_id"].asText())!!

    override var parent: GuildCategory? = null

    override val type: ChannelType = ChannelType.CATEGORY

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
