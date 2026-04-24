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
package io.github.ydwk.ydwk.evm.event.events.presence

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.GuildEvent
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when a user's presence or info (e.g. username, avatar) is updated.
 *
 * @param ydwk The [YDWK] instance.
 * @param guildId The id of the guild.
 * @param userId The id of the user.
 * @param status The new status ("idle", "dnd", "online", "offline").
 * @param user The cached [User] (null if not cached).
 */
@GuildEvent
data class PresenceUpdateEvent(
  override val ydwk: YDWK,
  val guildId: GetterSnowFlake,
  val userId: GetterSnowFlake,
  val status: String,
  val user: User?,
) : Event(ydwk)
