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
package io.github.ydwk.yde.impl.interaction.application.type

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.builders.slash.SlashOptionGetter
import io.github.ydwk.yde.builders.slash.SlashOptionGetterImpl
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.message.Embed
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.interaction.application.ApplicationCommandImpl
import io.github.ydwk.yde.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.application.sub.Reply
import io.github.ydwk.yde.interaction.application.type.SlashCommand
import io.github.ydwk.yde.util.EntityToStringBuilder

/** TODO: have a look at the [SlashCommandImpl] to see if it is possible to refactor the code */
class SlashCommandImpl(yde: YDE, json: JsonNode, interaction: Interaction) :
    ApplicationCommandImpl(
        yde.entityInstanceBuilder.buildApplicationCommand(
            json, ApplicationCommandType.CHAT_INPUT, interaction),
        interaction),
    SlashCommand {

    override val locale: String? = interaction.locale

    override fun reply(content: String): Reply {
        return ReplyImpl(yde, content, null, interaction.id, token)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(yde, null, embed, interaction.id, token)
    }

    override val options: List<SlashOptionGetter>
        get() {
            val map: MutableMap<Long, GenericEntity> = mutableMapOf()
            val options: JsonNode? = json["options"]
            val resolved: JsonNode? = json["resolved"]

            return if (resolved == null) {
                handleOptions(options, map)
            } else {
                handleResolved(resolved, map, options)
            }
        }

    private fun handleResolved(
        resolved: JsonNode,
        map: MutableMap<Long, GenericEntity>,
        options: JsonNode?
    ): List<SlashOptionGetter> {
        resolved["users"]?.let {
            it.fields().forEach { (id, node) ->
                map[id.toLong()] = yde.entityInstanceBuilder.buildUser(node)
            }
        }
        resolved["attachments"]?.let {
            it.fields().forEach { (id, node) ->
                map[id.toLong()] = yde.entityInstanceBuilder.buildAttachment(node)
            }
        }

        if (guildId != null) {
            resolved["members"]?.let {
                it.fields().forEach { (id, node) ->
                    resolved["users"]?.let { users ->
                        val user = users[id]
                        val member = yde.entityInstanceBuilder.buildMember(node, guildId!!, user)
                        val newMember = (yde as YDEImpl).memberCache.getOrPut(member)
                        map[id.toLong()] = newMember
                    }
                }
            }

            resolved["roles"]?.let {
                it.fields().forEach { (id, node) ->
                    map[id.toLong()] = yde.entityInstanceBuilder.buildRole(node)
                }
            }

            resolved["channels"]?.let {
                it.fields().forEach { (id, node) ->
                    val channelType = ChannelType.getValue(node["type"].asInt())
                    if (ChannelType.isGuildChannel(channelType)) {
                        map[id.toLong()] = yde.entityInstanceBuilder.buildGuildChannel(node)
                    } else {
                        map[id.toLong()] = yde.entityInstanceBuilder.buildDMChannel(node)
                    }
                }
            }
        }

        return handleOptions(options, map)
    }

    private fun handleOptions(
        options: JsonNode?,
        map: MutableMap<Long, GenericEntity>
    ): List<SlashOptionGetter> {
        val list: MutableList<SlashOptionGetter> = mutableListOf()

        options?.forEach { node ->
            val option = yde.entityInstanceBuilder.buildApplicationCommandOption(node)
            list.add(SlashOptionGetterImpl(option, map))
        }
            ?: return emptyList()

        return list
    }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
