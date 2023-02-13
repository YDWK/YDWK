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
package io.github.ydwk.ydwk.impl.entities.channel.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.entities.channel.getter.guild.GuildChannelGetter
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.entities.guild.invite.InviteCreator
import io.github.ydwk.ydwk.impl.entities.ChannelImpl
import io.github.ydwk.ydwk.impl.entities.channel.getter.guild.GuildChannelGetterImpl
import io.github.ydwk.ydwk.util.EntityToStringBuilder

open class GuildChannelImpl(
    ydwk: YDWK,
    json: JsonNode,
    idAsLong: Long,
) : ChannelImpl(ydwk, json, idAsLong, true, false), GuildChannel {

    override val guild: Guild
        get() = ydwk.getGuildById(json["guild_id"].asText())!!

    override var position: Int = json["position"].asInt()

    override var parent: GuildCategory? =
        if (json.hasNonNull("parent_id")) {
            ydwk
                .getGuildChannelById(json["parent_id"].asLong())
                ?.guildChannelGetter
                ?.asGuildCategory()
        } else {
            null
        }

    override val guildChannelGetter: GuildChannelGetter
        get() = GuildChannelGetterImpl(ydwk, json, idAsLong)

    override val inviteCreator: InviteCreator
        get() = InviteCreator(ydwk, this.id)

    override var name: String = json["name"].asText()

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).toString()
    }
}
