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
import io.github.ydwk.yde.entities.builder.guild.channel.VoiceChannelBuilder
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.impl.entities.channel.guild.GuildVoiceChannelImpl
import io.github.ydwk.yde.rest.EndPoint
import kotlinx.coroutines.CompletableDeferred
import okhttp3.RequestBody.Companion.toRequestBody

class VoiceChannelBuilderImpl(val yde: YDE, val guildId: String?, val name: String) :
    VoiceChannelBuilder {
    private var isVoiceChannel: Boolean = true
    private var bitrate: Int? = null
    private var userLimit: Int? = null
    private var position: Int? = null
    private var permissionOverwrites: MutableList<PermissionOverwrite> = mutableListOf()
    private var parentId: String? = null

    override fun isStageChannel(): VoiceChannelBuilder {
        this.isVoiceChannel = false
        return this
    }

    override fun setBitrate(bitrate: Int): VoiceChannelBuilder {
        this.bitrate = bitrate
        return this
    }

    override fun setUserLimit(userLimit: Int): VoiceChannelBuilder {
        this.userLimit = userLimit
        return this
    }

    override fun setPosition(position: Int): VoiceChannelBuilder {
        this.position = position
        return this
    }

    override fun setPermissionOverwrites(
        permissionOverwrites: List<PermissionOverwrite>,
    ): VoiceChannelBuilder {
        this.permissionOverwrites.addAll(permissionOverwrites)
        return this
    }

    override fun setParentId(parentId: String): VoiceChannelBuilder {
        this.parentId = parentId
        return this
    }

    override val json: JsonNode
        get() {
            val json = yde.objectMapper.createObjectNode()

            json.put("name", name)
            json.put(
                "type",
                if (isVoiceChannel) ChannelType.VOICE.getId() else ChannelType.STAGE_VOICE.getId())
            if (bitrate != null) json.put("bitrate", bitrate)
            if (userLimit != null) json.put("user_limit", userLimit)
            if (position != null) json.put("position", position)
            // if (permissionOverwrites != null) json.put("permission_overwrites",
            // yde.objectMapper.valueToTree(permissionOverwrites))
            if (parentId != null) json.put("parent_id", parentId)

            return json
        }

    override fun create(): CompletableDeferred<GuildVoiceChannel> {
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
                    GuildVoiceChannelImpl(yde, jsonBody, jsonBody["id"].asLong())
                }
            }
    }
}
