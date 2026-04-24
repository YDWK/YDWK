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
package io.github.ydwk.yde.impl.interaction.message.selectmenu.type

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.impl.interaction.message.selectmenu.SelectMenuInteractionImpl
import io.github.ydwk.yde.interaction.message.selectmenu.interaction.type.ChannelSelectMenuInteraction
import io.github.ydwk.yde.util.GetterSnowFlake

class ChannelSelectMenuInteractionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val interactionId: GetterSnowFlake,
    override val customId: String,
    override val selectedChannels: List<GuildChannel>,
) :
    ChannelSelectMenuInteraction,
    SelectMenuInteractionImpl(yde, json, interactionId, customId, "", 0, 1)
