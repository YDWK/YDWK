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
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.message.MessageFlag
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.impl.entities.channel.guild.GenericGuildTextChannelImpl
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.impl.entities.guild.RoleImpl
import io.github.ydwk.ydwk.impl.entities.message.AttachmentImpl
import io.github.ydwk.ydwk.interaction.Interaction
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandOption
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandType
import io.github.ydwk.ydwk.interaction.application.SlashCommand
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.rest.json.replyJsonBody
import io.github.ydwk.ydwk.slash.SlashOptionGetter
import io.github.ydwk.ydwk.slash.SlashOptionGetterImpl
import io.github.ydwk.ydwk.util.GetterSnowFlake
import java.util.concurrent.CompletableFuture
import okhttp3.RequestBody.Companion.toRequestBody

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
    override fun reply(content: String, tts: Boolean, ephemeral: Boolean): CompletableFuture<Void> {
        return ydwk.restApiManager
            .post(
                replyJsonBody(ydwk, content, tts, if (ephemeral) MessageFlag.EPHEMERAL else null)
                    .toString()
                    .toRequestBody(),
                EndPoint.ApplicationCommandsEndpoint.REPLY_TO_SLASH_COMMAND,
                interaction.id,
                token)
            .executeWithNoResult()
    }

    override fun reply(embed: Embed, tts: Boolean, ephemeral: Boolean): CompletableFuture<Void> {
        return ydwk.restApiManager
            .post(
                replyJsonBody(ydwk, embed, tts, if (ephemeral) MessageFlag.EPHEMERAL else null)
                    .toString()
                    .toRequestBody(),
                EndPoint.ApplicationCommandsEndpoint.REPLY_TO_SLASH_COMMAND,
                interaction.id,
                token)
            .executeWithNoResult()
    }

    override val options: List<SlashOptionGetter>
        get() {
            val map: MutableMap<Long, GenericEntity> = mutableMapOf()
            val resolved = json["resolved"]
            resolved["users"]?.let {
                it.fields().forEach { (id, node) ->
                    map[id.toLong()] = UserImpl(node, node["id"].asLong(), ydwk)
                    (ydwk as YDWKImpl)
                        .cache
                        .update(
                            node["id"].asText(),
                            CacheIds.USER,
                            UserImpl(node, node["id"].asLong(), ydwk))
                }
            }
            resolved["attachments"]?.let {
                it.fields().forEach { (id, node) ->
                    map[id.toLong()] = AttachmentImpl(ydwk, node, node["id"].asLong())
                    (ydwk as YDWKImpl)
                        .cache
                        .update(
                            node["id"].asText(),
                            CacheIds.ATTACHMENT,
                            AttachmentImpl(ydwk, node, node["id"].asLong()))
                }
            }

            if (guild != null) {
                resolved["members"]?.let {
                    it.fields().forEach { (id, node) ->
                        resolved["users"]?.let { users ->
                            val user = users[id]
                            map[id.toLong()] =
                                MemberImpl(
                                    ydwk, node, guild, UserImpl(user, user["id"].asLong(), ydwk))
                        }
                        (ydwk as YDWKImpl)
                            .memberCache
                            .update(
                                node["id"].asText(),
                                CacheIds.MEMBER,
                                MemberImpl(
                                    ydwk,
                                    node,
                                    guild,
                                    UserImpl(
                                        resolved["users"][id],
                                        resolved["users"][id]["id"].asLong(),
                                        ydwk)))
                    }
                }

                resolved["roles"]?.let {
                    it.fields().forEach { (id, node) ->
                        map[id.toLong()] = RoleImpl(ydwk, node, node["id"].asLong())
                        (ydwk as YDWKImpl)
                            .cache
                            .update(
                                node["id"].asText(),
                                CacheIds.ROLE,
                                RoleImpl(ydwk, node, node["id"].asLong()))
                    }
                }

                resolved["channels"]?.let {
                    it.fields().forEach { (id, node) ->
                        map[id.toLong()] =
                            GenericGuildTextChannelImpl(ydwk, node, node["id"].asLong())
                        (ydwk as YDWKImpl)
                            .cache
                            .update(
                                node["id"].asText(),
                                CacheIds.TEXT_CHANNEL,
                                GenericGuildTextChannelImpl(ydwk, node, node["id"].asLong()))
                    }
                }
            }

            return applicationOptions?.map { SlashOptionGetterImpl(it, map) } ?: emptyList()
        }
}
