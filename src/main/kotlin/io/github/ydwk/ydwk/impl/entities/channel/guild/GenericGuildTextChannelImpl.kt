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
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildTextChannel
import io.github.ydwk.ydwk.entities.channel.guild.text.GuildTextChannel

open class GenericGuildTextChannelImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : GenericGuildTextChannel, GenericGuildChannelImpl(ydwk, json, idAsLong, true, false, false) {

    override fun asGuildTextChannel(): GuildTextChannel? {
        return if (isCastable(GuildTextChannel::class.java)) {
            GuildTextChannelImpl(ydwk, json, idAsLong)
        } else {
            null
        }
    }

    override fun asGenericGuildTextChannel(): GenericGuildTextChannel {
        return this
    }
}
