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
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.impl.entities.channel.guild.GenericGuildTextChannelImpl
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.impl.entities.guild.RoleImpl
import io.github.ydwk.ydwk.impl.entities.message.AttachmentImpl
import io.github.ydwk.ydwk.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.ydwk.interaction.Interaction
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandOption
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandType
import io.github.ydwk.ydwk.interaction.application.SlashCommand
import io.github.ydwk.ydwk.interaction.application.sub.Reply
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.slash.SlashOptionGetter
import io.github.ydwk.ydwk.slash.SlashOptionGetterImpl
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.GetterSnowFlake

class SlashCommandImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long,
    val interaction: Interaction
) : SlashCommand {
    override val name: String = json["name"].asText()

    override val type: ApplicationCommandType = ApplicationCommandType.fromInt(json["type"].asInt())

    // ignore
    private val applicationOptions: List<ApplicationCommandOption>? =
        if (json.has("options")) json["options"].map { ApplicationCommandOptionImpl(ydwk, it) }
        else null

    override val guild: Guild? = interaction.guild

    override val targetId: GetterSnowFlake? =
        if (json.has("target_id")) GetterSnowFlake.of(json["target_id"].asLong()) else null

    override val user: User? = interaction.user

    override val member: Member? = interaction.member

    override val applicationId: GetterSnowFlake = interaction.applicationId

    override val interactionType: InteractionType = interaction.type

    override val channel: TextChannel? = interaction.channel

    override val token: String = interaction.token

    override val version: Int = interaction.version

    override val message: Message? = interaction.message

    override val permissions: Long? = interaction.permissions

    override val locale: String? = interaction.locale

    override fun reply(content: String): Reply {
        return ReplyImpl(ydwk, content, null, interaction.id, token)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(ydwk, null, embed, interaction.id, token)
    }

    override val options: List<SlashOptionGetter>
        get() {
            // TODO : replace with getOrPut
            val map: MutableMap<Long, GenericEntity> = mutableMapOf()
            val resolved = json["resolved"]
            resolved["users"]?.let {
                it.fields().forEach { (id, node) ->
                    map[id.toLong()] = UserImpl(node, node["id"].asLong(), ydwk)
                }
            }
            resolved["attachments"]?.let {
                it.fields().forEach { (id, node) ->
                    map[id.toLong()] = AttachmentImpl(ydwk, node, node["id"].asLong())
                }
            }

            if (guild != null) {
                resolved["members"]?.let {
                    it.fields().forEach { (id, node) ->
                        resolved["users"]?.let { users ->
                            val user = users[id]
                            val member =
                                MemberImpl(
                                    ydwk as YDWKImpl,
                                    node,
                                    guild,
                                    UserImpl(user, user["id"].asLong(), ydwk))

                            val newMember =
                                ydwk.memberCache.getOrPut(member.guild.id, member.user.id, member)
                            map[id.toLong()] = newMember
                        }
                    }
                }

                resolved["roles"]?.let {
                    it.fields().forEach { (id, node) ->
                        map[id.toLong()] = RoleImpl(ydwk, node, node["id"].asLong())
                    }
                }

                resolved["channels"]?.let {
                    it.fields().forEach { (id, node) ->
                        map[id.toLong()] =
                            GenericGuildTextChannelImpl(ydwk, node, node["id"].asLong())
                    }
                }
            }

            return applicationOptions?.map { SlashOptionGetterImpl(it, map) } ?: emptyList()
        }

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).name(this.name).toString()
    }
}
