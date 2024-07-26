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
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.sub.GenericCommandData
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

class InteractionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val applicationId: GetterSnowFlake,
    override val type: InteractionType,
    override val guild: Guild?,
    override val channel: Channel?,
    override val member: Member?,
    override val user: User,
    override val token: String,
    override val version: Int,
    override val message: Message?,
    override val permissions: Long?,
    override val locale: String?,
    override val guildLocale: String?,
) : Interaction {

    override val data: GenericCommandData? =
        when (type) {
            InteractionType.APPLICATION_COMMAND -> {
                // get type
                when (ApplicationCommandType.getValue(json["data"]["type"].asInt())) {
                    ApplicationCommandType.CHAT_INPUT ->
                        yde.entityInstanceBuilder.buildSlashCommand(json, this)
                    ApplicationCommandType.USER ->
                        yde.entityInstanceBuilder.buildUserCommand(json, this)
                    ApplicationCommandType.MESSAGE ->
                        yde.entityInstanceBuilder.buildMessageCommand(json, this)
                    else ->
                        throw IllegalStateException(
                            "Unknown ApplicationCommandType ${json["data"]["type"].asInt()}")
                }
            }
            else -> null
        }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
