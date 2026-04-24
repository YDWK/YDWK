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
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when a message is updated/edited.
 *
 * @param ydwk The [YDWK] instance.
 * @param oldMessage The old message before the edit (null if not cached).
 * @param newMessage The new message after the edit.
 */
data class MessageUpdateEvent(
  override val ydwk: YDWK,
  val oldMessage: Message?,
  val newMessage: Message,
) : Event(ydwk)
