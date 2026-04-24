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
package io.github.ydwk.ydwk.evm.event.events.message

import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when a reaction is added to a message.
 *
 * @param ydwk The [YDWK] instance.
 * @param userId The id of the user who added the reaction.
 * @param channelId The id of the channel the message is in.
 * @param messageId The id of the message.
 * @param guildId The id of the guild (null for DMs).
 * @param member The member who added the reaction (null for DMs).
 * @param emoji The emoji that was added.
 * @param message The cached message (null if not cached).
 * @param user The user who added the reaction (null if not cached).
 */
data class MessageReactionAddEvent(
    override val ydwk: YDWK,
    val userId: GetterSnowFlake,
    val channelId: GetterSnowFlake,
    val messageId: GetterSnowFlake,
    val guildId: GetterSnowFlake?,
    val member: Member?,
    val emoji: Emoji,
    val message: Message?,
    val user: User?,
) : Event(ydwk)
