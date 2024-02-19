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
package io.github.ydwk.yde.builders.slash

import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.message.Attachment
import io.github.ydwk.yde.interaction.application.type.sub.SubCommand
import io.github.ydwk.yde.util.NameAbleEntity

interface SlashOptionGetter : NameAbleEntity {

    /**
     * The type of this option.
     *
     * @return the type of this option.
     */
    val type: SlashOptionType

    /**
     * The option as a string.
     *
     * @return The option as a string.
     */
    val asString: String

    /**
     * The option as a boolean.
     *
     * @return The option as a boolean.
     * @throws IllegalArgumentException if the option is not a boolean.
     */
    val asBoolean: Boolean

    /**
     * The option as a long.
     *
     * @return The option as a long.
     * @throws IllegalArgumentException if the option is not a long.
     */
    val asLong: Long

    /**
     * The option as a double.
     *
     * @return The option as a double.
     * @throws IllegalArgumentException if the option is not a double.
     */
    val asDouble: Double

    /**
     * The option as a user.
     *
     * @return The option as a user.
     * @throws IllegalArgumentException if the option is not a user.
     */
    val asUser: User

    /**
     * The option as a member.
     *
     * @return The option as a member.
     * @throws IllegalArgumentException if the option is not a member.
     */
    val asMember: Member

    /**
     * The option as a channel.
     *
     * @return The option as a channel.
     * @throws IllegalArgumentException if the option is not a channel.
     */
    val asChannel: Channel

    /**
     * The option as a role.
     *
     * @return The option as a role.
     * @throws IllegalArgumentException if the option is not a role.
     */
    val asRole: Role

    /**
     * The option as an attachment.
     *
     * @return The option as an attachment.
     * @throws IllegalArgumentException if the option is not an attachment.
     */
    val asAttachment: Attachment

    /**
     * The option as a subcommand.
     *
     * @return The option as a subcommand.
     * @throws IllegalArgumentException if the option is not a subcommand.
     */
    val asSubCommand: SubCommand
}
