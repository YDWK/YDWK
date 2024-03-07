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
import io.github.ydwk.yde.entities.channel.getter.guild.GuildMessageChannelGetter
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite

internal open class GuildMessageChannelImpl(
    yde: YDE,
    json: JsonNode,
    idAsLong: Long,
    override var topic: String,
    override var nsfw: Boolean,
    override var defaultAutoArchiveDuration: Int,
    override var lastMessageId: String,
    override var lastPinTimestamp: String,
    override var permissionOverwrites: List<PermissionOverwrite>,
    override val guildMessageChannelGetter: GuildMessageChannelGetter
) : GuildMessageChannel, GuildChannelImpl(yde.entityInstanceBuilder.buildGuildChannel(json)) {
    constructor(
        guildMessageChannel: GuildMessageChannel
    ) : this(
        guildMessageChannel.yde,
        guildMessageChannel.json,
        guildMessageChannel.idAsLong,
        guildMessageChannel.topic,
        guildMessageChannel.nsfw,
        guildMessageChannel.defaultAutoArchiveDuration,
        guildMessageChannel.lastMessageId,
        guildMessageChannel.lastPinTimestamp,
        guildMessageChannel.permissionOverwrites,
        guildMessageChannel.guildMessageChannelGetter)
}
