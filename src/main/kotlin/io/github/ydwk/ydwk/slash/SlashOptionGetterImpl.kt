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

import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.message.Attachment
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandOption

class SlashOptionGetterImpl(
    private val applicationCommandOption: ApplicationCommandOption,
    private val optionObjects: Map<Long, GenericEntity>
) : SlashOptionGetter {

    override val type: SlashOptionType
        get() = applicationCommandOption.type

    override val asString: String
        get() = applicationCommandOption.value.asText()

    override val asBoolean: Boolean
        get() =
            if (type == SlashOptionType.BOOLEAN) applicationCommandOption.value.asBoolean()
            else throw IllegalArgumentException("The option type ${type.name} is not a boolean")

    override val asLong: Long
        get() =
            if (type == SlashOptionType.INTEGER) applicationCommandOption.value.asLong()
            else throw IllegalArgumentException("The option type ${type.name} is not a long")

    override val asDouble: Double
        get() =
            if (type == SlashOptionType.NUMBER) applicationCommandOption.value.asDouble()
            else throw IllegalArgumentException("The option type ${type.name} is not a double")

    override val asUser: User
        get() =
            if (type == SlashOptionType.USER)
                if (optionObjects[applicationCommandOption.value.asLong()] is Member) {
                    (optionObjects[applicationCommandOption.value.asLong()] as Member).user
                } else {
                    optionObjects[applicationCommandOption.value.asLong()] as User
                }
            else throw IllegalArgumentException("The option type ${type.name} is not a user")

    override val asMember: Member
        get() =
            if (type == SlashOptionType.USER)
                optionObjects[applicationCommandOption.value.asLong()] as Member
            else throw IllegalArgumentException("The option type ${type.name} is not a member")

    override val asChannel: Channel
        get() =
            if (type == SlashOptionType.CHANNEL)
                optionObjects[applicationCommandOption.value.asLong()] as Channel
            else throw IllegalArgumentException("The option type ${type.name} is not a channel")

    override val asRole: Role
        get() =
            if (type == SlashOptionType.ROLE)
                optionObjects[applicationCommandOption.value.asLong()] as Role
            else throw IllegalArgumentException("The option type ${type.name} is not a role")

    override val asAttachment: Attachment
        get() =
            if (type == SlashOptionType.ATTACHMENT)
                optionObjects[applicationCommandOption.value.asLong()] as Attachment
            else throw IllegalArgumentException("The option type ${type.name} is not an attachment")

    override var name: String
        get() = applicationCommandOption.name
        set(value) {
            applicationCommandOption.name = value
        }
}
