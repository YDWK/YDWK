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
package io.github.ydwk.ydwk.evm.event.events.channel.update.category

import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.ChannelEvent
import io.github.ydwk.ydwk.evm.event.events.channel.GenericChannelUpdateEvent

/**
 * This event is triggered when a guild category's name is updated.
 *
 * @param ydwk The [YDWK] instance.
 * @param entity The [GuildCategory] that was updated.
 * @param oldName The old name.
 * @param newName The new name.
 */
@ChannelEvent
data class CategoryNameUpdateEvent(
    override val ydwk: YDWK,
    override val entity: GuildCategory,
    val oldName: String,
    val newName: String,
) : GenericChannelUpdateEvent<String>(ydwk, entity, oldName, newName)
