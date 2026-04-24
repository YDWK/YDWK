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
package io.github.ydwk.ydwk.evm.event.events.thread

import io.github.ydwk.yde.entities.channel.guild.thread.GuildThreadChannel
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.ChannelEvent
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when a thread is updated.
 *
 * @param ydwk The [YDWK] instance.
 * @param oldThread The old [GuildThreadChannel] state before the update (null if not cached).
 * @param newThread The new [GuildThreadChannel] state after the update.
 */
@ChannelEvent
data class ThreadUpdateEvent(
    override val ydwk: YDWK,
    val oldThread: GuildThreadChannel?,
    val newThread: GuildThreadChannel,
) : Event(ydwk)
