/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.interaction.application

import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.interaction.sub.GenericCommandData
import io.github.realyusufismail.ydwk.interaction.sub.InteractionResolvedData
import io.github.realyusufismail.ydwk.util.GetterSnowFlake
import io.github.realyusufismail.ydwk.util.SnowFlake

interface ApplicationCommandData : SnowFlake, GenericCommandData {
    /**
     * Gets the name of the command.
     *
     * @return The name of the command.
     */
    val name: String

    /**
     * Gets the type of the command.
     *
     * @return The type of the command.
     */
    val type: ApplicationCommandType

    /**
     * Gets the resolved data of the command.
     *
     * @return The resolved data of the command.
     */
    val resolved: InteractionResolvedData?

    /**
     * Gets the options of this interaction.
     *
     * @return The options of this interaction.
     */
    val options: List<ApplicationCommandOption>?

    /**
     * Gets the guild Id of this interaction.
     *
     * @return The guild Id of this interaction.
     */
    val guildId: GetterSnowFlake?

    /**
     * Gets the id of the user or message targeted by a user or message command
     *
     * @return The id of the user or message targeted by a user or message command
     */
    val targetId: GetterSnowFlake?

    /**
     * Gets the user who invoked the command.
     *
     * @return The user who invoked the command.
     */
    val user: User?

    /**
     * Gets the member who invoked the command.
     *
     * @return The member who invoked the command.
     */
    val member: Member?
}
