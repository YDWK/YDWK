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
package io.github.ydwk.yde.impl.entities.builder.guild.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.builder.guild.channel.MessageChannelBuilder
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.impl.entities.channel.guild.GuildMessageChannelImpl
import io.github.ydwk.yde.rest.EndPoint
import kotlinx.coroutines.CompletableDeferred
import okhttp3.RequestBody.Companion.toRequestBody

class MessageChannelBuilderImpl(val yde: YDE, val guildId: String?, val name: String) :
    MessageChannelBuilder {
    private var isTextChannel = true
    private var topic: String? = null
    private var nsfw: Boolean? = null
    private var rateLimitPerUser: Int? = null
    private var position: Int? = null
    private var permissionOverwrites: MutableList<PermissionOverwrite> = mutableListOf()
    private var parentId: String? = null

    override fun isNewsChannel(): MessageChannelBuilder {
        this.isTextChannel = false
        return this
    }

    override fun setTopic(topic: String): MessageChannelBuilder {
        this.topic = topic
        return this
    }

    override fun setRateLimitPerUser(rateLimitPerUser: Int): MessageChannelBuilder {
        this.rateLimitPerUser = rateLimitPerUser
        return this
    }

    override fun setPosition(position: Int): MessageChannelBuilder {
        this.position = position
        return this
    }

    override fun setPermissionOverwrites(
        permissionOverwrites: List<PermissionOverwrite>,
    ): MessageChannelBuilder {
        this.permissionOverwrites.addAll(permissionOverwrites)
        return this
    }

    override fun setParentId(parentId: String): MessageChannelBuilder {
        this.parentId = parentId
        return this
    }

    override fun setNsfw(nsfw: Boolean): MessageChannelBuilder {
        this.nsfw = nsfw
        return this
    }

    override val json: JsonNode
        get() {
            val json = yde.objectMapper.createObjectNode()
            json.put("name", name)
            json.put(
                "type", if (isTextChannel) ChannelType.TEXT.getId() else ChannelType.NEWS.getId())
            if (topic != null) json.put("topic", topic)
            if (nsfw != null) json.put("nsfw", nsfw)
            if (rateLimitPerUser != null) json.put("rate_limit_per_user", rateLimitPerUser)
            if (position != null) json.put("position", position)
            if (permissionOverwrites.isNotEmpty()) {
                val array = yde.objectMapper.createArrayNode()
                permissionOverwrites.forEach { array.add(it.json) }
                json.set<JsonNode>("permission_overwrites", array)
            }
            if (parentId != null) json.put("parent_id", parentId)
            return json
        }

    override fun create(): CompletableDeferred<GuildMessageChannel> {
        if (guildId == null) {
            throw IllegalStateException("Guild id is not set")
        }

        return yde.restApiManager
            .post(json.toString().toRequestBody(), EndPoint.GuildEndpoint.CREATE_CHANNEL, guildId)
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    GuildMessageChannelImpl(yde, jsonBody, jsonBody["id"].asLong())
                }
            }
    }
}
