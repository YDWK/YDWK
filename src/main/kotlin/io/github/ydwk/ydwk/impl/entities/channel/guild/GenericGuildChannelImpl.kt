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
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildChannel
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildTextChannel
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildVoiceChannel
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory

open class GenericGuildChannelImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val isTextChannel: Boolean = false,
    override val isVoiceChannel: Boolean = false,
    override val isCategory: Boolean = false
) : GenericGuildChannel {

    override var position: Int = json["position"].asInt()

    override var parent: GuildCategory? =
        json["parent_id"]?.asText()?.let { ydwk.getCategoryById(it) }

    override fun asGuildCategory(): GuildCategory? {
        return if (isCategory) {
            GuildCategoryImpl(ydwk, json, idAsLong)
        } else {
            null
        }
    }

    override fun asGenericGuildTextChannel(): GenericGuildTextChannel {
        return if (isTextChannel) {
            GenericGuildTextChannelImpl(ydwk, json, idAsLong)
        } else {
            throw IllegalStateException("This channel is not a text channel")
        }
    }

    override fun asGenericGuildVoiceChannel(): GenericGuildVoiceChannel {
        return if (isVoiceChannel) {
            GenericGuildVoiceChannelImpl(ydwk, json, idAsLong)
        } else {
            throw IllegalStateException("This channel is not a voice channel")
        }
    }

    override val guild: Guild
        get() = TODO("Not yet implemented")

    override val type: ChannelType
        get() = ChannelType.fromId(json["type"].asInt())

    override var name: String
        get() = json["name"].asText()
        set(value) {}
}
