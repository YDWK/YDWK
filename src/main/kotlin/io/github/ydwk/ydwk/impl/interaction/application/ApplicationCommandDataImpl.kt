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
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.impl.interaction.sub.InteractionResolvedDataImpl
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandData
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandOption
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandType
import io.github.ydwk.ydwk.interaction.sub.InteractionResolvedData
import io.github.ydwk.ydwk.util.GetterSnowFlake

class ApplicationCommandDataImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val user: User?,
    override val member: Member?
) : ApplicationCommandData {
    override val name: String = json["name"].asText()

    override val type: ApplicationCommandType = ApplicationCommandType.fromInt(json["type"].asInt())

    override val resolved: InteractionResolvedData? =
        if (json.has("resolved")) InteractionResolvedDataImpl(ydwk, json["resolved"]) else null

    override val options: List<ApplicationCommandOption>? =
        if (json.has("options")) json["options"].map { ApplicationCommandOptionImpl(ydwk, it) }
        else null

    override val guildId: GetterSnowFlake? =
        if (json.has("guild_id")) GetterSnowFlake.of(json["guild_id"].asLong()) else null

    override val targetId: GetterSnowFlake? =
        if (json.has("target_id")) GetterSnowFlake.of(json["target_id"].asLong()) else null
    override val channel: Channel
        get() = TODO("Not yet implemented")
}
