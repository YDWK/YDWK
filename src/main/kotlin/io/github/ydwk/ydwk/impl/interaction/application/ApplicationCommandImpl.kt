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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.interaction.application

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.interaction.Interaction
import io.github.ydwk.ydwk.interaction.application.ApplicationCommand
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.GetterSnowFlake

abstract class ApplicationCommandImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long,
    val interaction: Interaction
) : ApplicationCommand {

    override val name: String
        get() = json["name"].asText()

    override val description: String
        get() = json["description"].asText()

    override val isDmPermissions: Boolean?
        get() = if (json.has("dm_permission")) json["dm_permission"].asBoolean() else null

    override val isNsfw: Boolean?
        get() = if (json.has("nsfw")) json["nsfw"].asBoolean() else null

    override val guild: Guild? = interaction.guild

    override val targetId: GetterSnowFlake?
        get() = if (json.has("target_id")) GetterSnowFlake.of(json["target_id"].asLong()) else null

    override val user: User? = interaction.user

    override val member: Member? = interaction.member

    override val applicationId: GetterSnowFlake = interaction.applicationId

    override val interactionType: InteractionType = interaction.type

    override val channel: Channel? = interaction.channel

    override val token: String = interaction.token

    override val version: Int = interaction.version

    override val message: Message? = interaction.message

    override val permissions: Long? = interaction.permissions

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).name(this.name).toString()
    }
}
