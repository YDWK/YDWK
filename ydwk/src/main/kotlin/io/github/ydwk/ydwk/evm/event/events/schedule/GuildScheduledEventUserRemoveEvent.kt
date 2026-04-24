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
package io.github.ydwk.ydwk.evm.event.events.schedule

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.GuildScheduledEvent
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.GuildEvent
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when a user unsubscribes from a scheduled event.
 *
 * @param ydwk The [YDWK] instance.
 * @param scheduledEventId The id of the scheduled event.
 * @param guildId The id of the guild.
 * @param user The [User] that unsubscribed (null if not cached).
 * @param scheduledEvent The [GuildScheduledEvent] (null if not cached).
 */
@GuildEvent
data class GuildScheduledEventUserRemoveEvent(
    override val ydwk: YDWK,
    val scheduledEventId: GetterSnowFlake,
    val guildId: GetterSnowFlake,
    val user: User?,
    val scheduledEvent: GuildScheduledEvent?,
) : Event(ydwk)
