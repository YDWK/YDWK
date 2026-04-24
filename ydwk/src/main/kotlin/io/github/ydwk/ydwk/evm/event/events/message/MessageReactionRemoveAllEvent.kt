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

import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when all reactions are removed from a message.
 *
 * @param ydwk The [YDWK] instance.
 * @param channelId The id of the channel the message is in.
 * @param messageId The id of the message.
 * @param guildId The id of the guild (null for DMs).
 * @param message The cached message (null if not cached).
 */
data class MessageReactionRemoveAllEvent(
  override val ydwk: YDWK,
  val channelId: GetterSnowFlake,
  val messageId: GetterSnowFlake,
  val guildId: GetterSnowFlake?,
  val message: Message?,
) : Event(ydwk)
