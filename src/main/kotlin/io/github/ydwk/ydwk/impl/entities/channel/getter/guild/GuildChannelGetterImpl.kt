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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.entities.channel.getter.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.getter.guild.GuildChannelGetter
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.entities.channel.guild.forum.GuildForumChannel
import io.github.ydwk.ydwk.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildStageChannel
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.ydwk.impl.entities.channel.guild.*

class GuildChannelGetterImpl(ydwk: YDWK, json: JsonNode, idAsLong: Long) :
    GuildChannelImpl(ydwk, json, idAsLong), GuildChannelGetter {
    override fun asGuildMessageChannel(): GuildMessageChannel? {
        return if (type == ChannelType.TEXT || type == ChannelType.NEWS) {
            GuildMessageChannelImpl(ydwk, json, idAsLong)
        } else {
            null
        }
    }

    override fun asGuildVoiceChannel(): GuildVoiceChannel? {
        return if (type == ChannelType.VOICE) {
            GuildVoiceChannelImpl(ydwk, json, idAsLong)
        } else {
            null
        }
    }

    override fun asGuildStageChannel(): GuildStageChannel? {
        return if (type == ChannelType.STAGE_VOICE) {
            GuildStageChannelImpl(ydwk, json, idAsLong)
        } else {
            null
        }
    }

    override fun asGuildCategory(): GuildCategory? {
        return if (type == ChannelType.CATEGORY) {
            GuildCategoryImpl(ydwk, json, idAsLong)
        } else {
            null
        }
    }

    override fun asGuildForumChannel(): GuildForumChannel? {
        return if (type == ChannelType.FORUM) {
            GuildForumChannelImpl(ydwk, json, idAsLong)
        } else {
            null
        }
    }
}
