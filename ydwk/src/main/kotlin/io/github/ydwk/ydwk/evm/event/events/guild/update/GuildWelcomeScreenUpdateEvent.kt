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
package io.github.ydwk.ydwk.evm.event.events.guild.update

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.guild.WelcomeScreen
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.GuildEvent
import io.github.ydwk.ydwk.evm.event.events.guild.GenericGuildUpdateEvent

/**
 * This event is triggered when a guild's welcome screen is updated.
 *
 * @param ydwk The [YDWK] instance.
 * @param entity The [Guild] that was updated.
 * @param oldWelcomeScreen The old welcome screen.
 * @param newWelcomeScreen The new welcome screen.
 */
@GuildEvent
data class GuildWelcomeScreenUpdateEvent(
    override val ydwk: YDWK,
    override val entity: Guild,
    val oldWelcomeScreen: WelcomeScreen?,
    val newWelcomeScreen: WelcomeScreen?,
) : GenericGuildUpdateEvent<WelcomeScreen?>(ydwk, entity, oldWelcomeScreen, newWelcomeScreen)
