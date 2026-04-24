/*
 * Copyright 2024-2025 YDWK inc.
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
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.event.Event

/**
 * Fired when all reactions for a specific emoji are removed from a message.
 *
 * @param ydwk The [YDWK] instance.
 * @param channelId The channel containing the message.
 * @param messageId The message that had reactions removed.
 * @param guildId The guild the message was in (null for DMs).
 * @param emoji The emoji whose reactions were all removed.
 * @param message The cached message (null if not cached).
 */
data class MessageReactionRemoveEmojiEvent(
    override val ydwk: YDWK,
    val channelId: GetterSnowFlake,
    val messageId: GetterSnowFlake,
    val guildId: GetterSnowFlake?,
    val emoji: Emoji,
    val message: Message?,
) : Event(ydwk)
