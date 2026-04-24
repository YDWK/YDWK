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
package io.github.ydwk.ydwk.evm.event.events.thread

import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.ChannelEvent
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when anyone is added to or removed from a thread.
 *
 * @param ydwk The [YDWK] instance.
 * @param threadId The id of the thread.
 * @param guildId The id of the guild.
 * @param memberCount The approximate member count of the thread (capped at 50).
 * @param addedMemberIds The ids of the users who were added.
 * @param removedMemberIds The ids of the users who were removed.
 */
@ChannelEvent
data class ThreadMembersUpdateEvent(
    override val ydwk: YDWK,
    val threadId: GetterSnowFlake,
    val guildId: GetterSnowFlake,
    val memberCount: Int,
    val addedMemberIds: List<GetterSnowFlake>,
    val removedMemberIds: List<GetterSnowFlake>,
) : Event(ydwk)
