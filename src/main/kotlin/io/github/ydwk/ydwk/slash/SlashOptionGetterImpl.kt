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
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandOption

class SlashOptionGetterImpl(private val applicationCommandOption: ApplicationCommandOption) :
    SlashOptionGetter {

    init {
        println(applicationCommandOption.json.toPrettyString())
    }

    override val type: SlashOptionType
        get() = applicationCommandOption.type

    override val asString: String?
        get() =
            if (type == SlashOptionType.STRING) applicationCommandOption.value.asText() else null

    override val asBoolean: Boolean?
        get() =
            if (type == SlashOptionType.BOOLEAN) applicationCommandOption.value.asBoolean()
            else null

    override val asLong: Long?
        get() =
            if (type == SlashOptionType.INTEGER) applicationCommandOption.value.asLong() else null

    override val asDouble: Double?
        get() =
            if (type == SlashOptionType.NUMBER) applicationCommandOption.value.asDouble() else null

    override val asUser: User?
        get() =
            if (type == SlashOptionType.USER)
                applicationCommandOption.ydwk.getUser(applicationCommandOption.value.asLong())
            else null

    override val asMember: Member?
        get() = if (type == SlashOptionType.USER) applicationCommandOption.value as Member else null

    override val asChannel: GenericGuildTextChannel?
        get() =
            if (type == SlashOptionType.CHANNEL)
                applicationCommandOption.ydwk.getGuildTextChannel(
                    applicationCommandOption.value.asLong())
            else null

    override val asRole: Role?
        get() =
            if (type == SlashOptionType.ROLE)
                applicationCommandOption.ydwk.getRole(applicationCommandOption.value.asLong())
            else null

    override val asAttachment: Attachment?
        get() =
            if (type == SlashOptionType.MENTIONABLE) applicationCommandOption.value as Attachment
            else null

    override var name: String
        get() = applicationCommandOption.name
        set(value) {
            applicationCommandOption.name = value
        }
}
