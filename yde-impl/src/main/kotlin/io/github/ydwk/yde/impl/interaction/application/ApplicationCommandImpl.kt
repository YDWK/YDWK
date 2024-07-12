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
package io.github.ydwk.yde.impl.interaction.application

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommand
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.GetterSnowFlake

abstract class ApplicationCommandImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val description: String,
    override val isNsfw: Boolean?,
    override val isDmPermissions: Boolean?,
    override val targetId: GetterSnowFlake?,
    val interaction: Interaction,
    override val applicationId: GetterSnowFlake,
    override val guild: Guild?,
    override val version: Int,
    override val user: User,
    override val member: Member?,
    override val interactionType: InteractionType,
    override val channel: Channel?,
    override val token: String,
    override val message: Message?,
    override val permissions: Long?,
) :
    ApplicationCommand,
    ToStringEntityImpl<ApplicationCommand>(yde, ApplicationCommand::class.java) {
    constructor(
        applicationCommand: ApplicationCommand,
        interaction: Interaction
    ) : this(
        applicationCommand.yde,
        applicationCommand.json,
        applicationCommand.idAsLong,
        interaction,
        applicationCommand.applicationId,
        applicationCommand.guild,
        applicationCommand.description,
        applicationCommand.isDmPermissions,
        applicationCommand.isNsfw,
        applicationCommand.version,
        applicationCommand.targetId,
        applicationCommand.user,
        applicationCommand.member,
        applicationCommand.interactionType,
        applicationCommand.channel,
        applicationCommand.token,
        applicationCommand.message,
        applicationCommand.permissions)
}
