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
package io.github.ydwk.yde.impl.entities.channel.getter.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.getter.guild.GuildMessageChannelGetter
import io.github.ydwk.yde.entities.channel.guild.message.news.GuildNewsChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.GuildTextChannel
import io.github.ydwk.yde.impl.entities.channel.guild.GuildMessageChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildNewsChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildTextChannelImpl

class GuildMessageChannelGetterImpl(yde: YDE, json: JsonNode, idAsLong: Long) :
    GuildMessageChannelImpl(yde, json, idAsLong), GuildMessageChannelGetter {
    override fun asGuildTextChannel(): GuildTextChannel? {
        return if (type == ChannelType.TEXT) {
            GuildTextChannelImpl(yde, json, idAsLong)
        } else {
            null
        }
    }

    override fun asGuildNewsChannel(): GuildNewsChannel? {
        return if (type == ChannelType.NEWS) {
            GuildNewsChannelImpl(yde, json, idAsLong)
        } else {
            null
        }
    }
}
