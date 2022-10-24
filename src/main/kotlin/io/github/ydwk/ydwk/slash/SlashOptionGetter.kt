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
package io.github.ydwk.ydwk.slash

import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildTextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.message.Attachment
import io.github.ydwk.ydwk.util.NameAbleEntity

interface SlashOptionGetter : NameAbleEntity {

    /**
     * Gets the type of this option.
     *
     * @return the type of this option.
     */
    val type: SlashOptionType

    /**
     * Gets the option as a string.
     *
     * @return The option as a string.
     */
    val asString: String?

    /**
     * Gets the option as a boolean.
     *
     * @return The option as a boolean.
     */
    val asBoolean: Boolean?

    /**
     * Gets the option as a long.
     *
     * @return The option as a long.
     */
    val asLong: Long?

    /**
     * Gets the option as a double.
     *
     * @return The option as a double.
     */
    val asDouble: Double?

    /**
     * Gets the option as a user.
     *
     * @return The option as a user.
     */
    val asUser: User?

    /**
     * Gets the option as a member.
     *
     * @return The option as a member.
     */
    val asMember: Member?

    /**
     * Gets the option as a channel.
     *
     * @return The option as a channel.
     */
    val asChannel: GenericGuildTextChannel?

    /**
     * Gets the option as a role.
     *
     * @return The option as a role.
     */
    val asRole: Role?

    /**
     * Gets the option as an attachment.
     *
     * @return The option as an attachment.
     */
    val asAttachment: Attachment?
}
