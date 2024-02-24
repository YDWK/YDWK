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
package io.github.ydwk.yde.impl.interaction

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.TextChannel
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.interaction.message.ComponentInteractionData
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

open class ComponentInteractionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val interactionId: GetterSnowFlake,
    override val type: ComponentType,
    override val interactionToken: String,
    override val message: Message,
    override val member: Member?,
    override val user: User?,
    override val guild: Guild?,
    override val channel: TextChannel?,
    override val applicationId: GetterSnowFlake?,
    override val components: List<Component>,
    override val data: ComponentInteractionData,
) : ComponentInteraction {

    constructor(
        componentInteractionImpl: ComponentInteractionImpl
    ) : this(
        componentInteractionImpl.yde,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId,
        componentInteractionImpl.type,
        componentInteractionImpl.interactionToken,
        componentInteractionImpl.message,
        componentInteractionImpl.member,
        componentInteractionImpl.user,
        componentInteractionImpl.guild,
        componentInteractionImpl.channel,
        componentInteractionImpl.applicationId,
        componentInteractionImpl.components,
        componentInteractionImpl.data)

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
