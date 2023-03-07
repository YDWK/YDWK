/*
 * Copyright 2022 YDWK inc.
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
package io.github.ydwk.ydwk.evm.event.events.channel.update.text

import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.event.events.channel.GenericChannelUpdateEvent

/**
 * This event is triggered when a guild text channel's slow mode is updated.
 *
 * @param ydwk The [YDWK] instance.
 * @param entity The [GuildMessageChannel] that was updated.
 * @param oldSlowMode The old slow mode.
 * @param newSlowMode The new slow mode.
 */
data class TextChannelSlowModeUpdateEvent(
    override val ydwk: YDWK,
    override val entity: GuildMessageChannel,
    val channelType: ChannelType,
    val oldSlowMode: Int,
    val newSlowMode: Int
) : GenericChannelUpdateEvent<Int>(ydwk, entity, oldSlowMode, newSlowMode)
