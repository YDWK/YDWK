/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.interaction.application

import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.interaction.sub.GenericCommandData
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.SnowFlake

interface ApplicationCommand : SnowFlake, GenericCommandData {
    /**
     * The type of the command.
     *
     * @return The type of the command.
     */
    val type: ApplicationCommandType

    /**
     * The Id of the application that this interaction is for.
     *
     * @return The Id of the application that this interaction is for.
     */
    val applicationId: GetterSnowFlake

    /**
     * The guild where the interaction occurred.
     *
     * @return The guild where the interaction occurred.
     */
    val guild: Guild?

    /**
     * The name of the command.
     *
     * @return The name of the command.
     */
    val name: String

    /**
     * The description for CHAT_INPUT commands, 1-100 characters. Empty string for USER and MESSAGE
     * commands.
     *
     * @return The description for CHAT_INPUT commands, 1-100 characters. Empty string for USER and
     *   MESSAGE commands.
     */
    val description: String

    /**
     * Whether the command is available in DMs with the app, only for globally-scoped commands. By
     * default, commands are visible.
     *
     * @return Indicates whether the command is available in DMs with the app, only for
     *   globally-scoped commands. By default, commands are visible.
     */
    val isDmPermissions: Boolean?

    /**
     * Whether the command is 'Not Safe For Work' (NSFW), only for globally-scoped commands. By
     * default, commands are not NSFW.
     *
     * @return Indicates whether the command is 'Not Safe For Work' (NSFW), only for globally-scoped
     *   commands. By default, commands are not NSFW.
     */
    val isNsfw: Boolean?

    /**
     * The version of this interaction.
     *
     * @return The version of this interaction.
     */
    val version: Int

    // Interaction related

    /**
     * The id of the user or message targeted by a user or message command
     *
     * @return The id of the user or message targeted by a user or message command
     */
    val targetId: GetterSnowFlake?

    /**
     * The user who invoked the command.
     *
     * @return The user who invoked the command.
     */
    val user: User

    /**
     * The member who invoked the command.
     *
     * @return The member who invoked the command.
     */
    val member: Member?

    /**
     * The type of this interaction.
     *
     * @return The type of this interaction.
     */
    val interactionType: InteractionType

    /**
     * The channel that this interaction is for.
     *
     * @return The channel that this interaction is for.
     */
    val channel: Channel?

    /**
     * The token of this interaction.
     *
     * @return The token of this interaction.
     */
    val token: String

    /**
     * The message of this interaction.
     *
     * @return The message of this interaction.
     */
    val message: Message?

    /**
     * Gets bitwise set of permissions the app or bot has within the channel the interaction was
     * sent from
     *
     * @return bitwise set of permissions the app or bot has within the channel the interaction was
     *   sent from
     */
    val permissions: Long?
}
