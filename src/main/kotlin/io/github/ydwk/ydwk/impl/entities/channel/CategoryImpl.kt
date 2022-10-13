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
package io.github.ydwk.ydwk.impl.entities.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.guild.Category

class CategoryImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : Category {

    override val guild: Guild
        get() =
            when {
                json.has("guild_id") -> {
                    if (ydwk.getGuild(json["guild_id"].asLong()) != null) {
                        ydwk.getGuild(json["guild_id"].asLong())!!
                    } else {
                        throw IllegalStateException("Guild is null")
                    }
                }
                else -> {
                    throw IllegalStateException("Guild is null")
                }
            }

    override val type: ChannelType
        get() = ChannelType.CATEGORY

    override var name: String
        get() = json["name"].asText()
        set(value) {}
}