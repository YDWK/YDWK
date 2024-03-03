/*
 * Copyright 2024 YDWK inc.
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
import io.github.ydwk.yde.entities.channel.getter.guild.GuildChannelGetter
import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.entities.guild.invite.InviteCreator
import io.github.ydwk.yde.impl.entities.ChannelImpl

open class GuildChannelImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val guild: Guild,
    override var position: Int,
    override var parent: GuildCategory?,
    override val guildChannelGetter: GuildChannelGetter,
    override val inviteCreator: InviteCreator,
    override var name: String,
) :
    ChannelImpl(
        yde.entityInstanceBuilder.buildChannel(json, isGuildChannel = true, isDmChannel = false)),
    GuildChannel