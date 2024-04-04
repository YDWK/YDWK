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
package io.github.ydwk.yde.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.guild.GuildScheduledEvent
import io.github.ydwk.yde.entities.guild.Invite
import io.github.ydwk.yde.entities.guild.invite.TargetType
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.util.GetterSnowFlake

internal class InviteImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val code: String,
    override val guildId: GetterSnowFlake,
    override val channel: GuildChannel,
    override val inviter: User?,
    override val targetType: TargetType,
    override val targetUser: User?,
    override val targetApplication: Application?,
    override val approximatePresenceCount: Int,
    override val approximateMemberCount: Int,
    override val expirationDate: String,
    override val guildScheduledEvent: GuildScheduledEvent
) : Invite, ToStringEntityImpl<Invite>(yde, Invite::class.java)
