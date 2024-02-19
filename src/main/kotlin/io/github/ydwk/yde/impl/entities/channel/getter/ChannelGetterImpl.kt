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
package io.github.ydwk.yde.impl.entities.channel.getter

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.getter.ChannelGetter
import io.github.ydwk.yde.impl.entities.ChannelImpl
import io.github.ydwk.yde.impl.entities.channel.DmChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildChannelImpl

class ChannelGetterImpl(
    yde: YDE,
    json: JsonNode,
    idAsLong: Long,
    isGuildChannel: Boolean,
    isDmChannel: Boolean,
) : ChannelImpl(yde, json, idAsLong, isGuildChannel, isDmChannel), ChannelGetter {
    override fun asGuildChannel(): GuildChannel? {
        return if (isGuildChannel) {
            GuildChannelImpl(yde, json, idAsLong)
        } else {
            null
        }
    }

    override fun asDmChannel(): DmChannel? {
        return if (isDmChannel) {
            DmChannelImpl(yde, json, idAsLong)
        } else {
            null
        }
    }
}
