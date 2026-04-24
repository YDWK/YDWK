/*
 * Copyright 2024-2026 YDWK inc.
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
import io.github.ydwk.yde.entities.channel.guild.thread.GuildThreadChannel
import io.github.ydwk.yde.entities.guild.invite.InviteCreator
import io.github.ydwk.yde.util.GetterSnowFlake

internal class GuildThreadChannelImpl(
    yde: YDE,
    json: JsonNode,
    idAsLong: Long,
    guildId: GetterSnowFlake,
    position: Int,
    parentId: GetterSnowFlake?,
    inviteCreator: InviteCreator,
    name: String,
) :
    GuildChannelImpl(yde, json, idAsLong, guildId, position, parentId, inviteCreator, name),
    GuildThreadChannel
