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
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommand
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.GetterSnowFlake

open class ApplicationCommandImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val description: String,
    override val isNsfw: Boolean?,
    override val isDmPermissions: Boolean?,
    override val targetId: GetterSnowFlake?,
    override val name: String,
    override val type: ApplicationCommandType,
    val interaction: Interaction,
    override val applicationId: GetterSnowFlake = interaction.applicationId,
    override val guildId: GetterSnowFlake? = interaction.guildId,
    override val version: Int = interaction.version,
    override val user: User = interaction.user,
    override val member: Member? = interaction.member,
    override val interactionType: InteractionType = interaction.type,
    override val channelId: GetterSnowFlake? = interaction.channelId,
    override val token: String = interaction.token,
    override val message: Message? = interaction.message,
    override val permissions: Long? = interaction.permissions,
) :
    ApplicationCommand,
    ToStringEntityImpl<ApplicationCommand>(yde, ApplicationCommand::class.java) {
    constructor(
        applicationCommand: ApplicationCommand,
        interaction: Interaction,
    ) : this(
        applicationCommand.yde,
        applicationCommand.json,
        applicationCommand.idAsLong,
        applicationCommand.description,
        applicationCommand.isNsfw,
        applicationCommand.isDmPermissions,
        applicationCommand.targetId,
        applicationCommand.name,
        applicationCommand.type,
        interaction,
        applicationCommand.applicationId,
        applicationCommand.guildId,
        applicationCommand.version,
        applicationCommand.user,
        applicationCommand.member,
        applicationCommand.interactionType,
        applicationCommand.channelId,
        applicationCommand.token,
        applicationCommand.message,
        applicationCommand.permissions,
    )
}
