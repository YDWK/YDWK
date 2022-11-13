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
package io.github.ydwk.ydwk.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.VoiceState
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.impl.entities.guild.MemberImpl
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.formatZonedDateTime
import java.util.concurrent.CompletableFuture

class VoiceStateImpl(override val ydwk: YDWK, override val json: JsonNode) : VoiceState {

    override val guild: Guild?
        get() = ydwk.getGuildById(json["guild_id"].asLong())

    override val channel: GuildVoiceChannel?
        get() = ydwk.getGuildVoiceChannelById(json["channel_id"].asLong())

    override val user: User
        get() =
            ydwk.getUserById(json["user_id"].asLong())
                ?: throw IllegalStateException("User not found")

    override val member: Member?
        get() =
            if (json.hasNonNull("member")) MemberImpl(ydwk, json["member"], guild!!, user, this)
            else null

    override val sessionId: String
        get() = json["session_id"].asText()

    override val isDeafened: Boolean
        get() = json["deaf"].asBoolean()

    override val isMuted: Boolean
        get() = json["mute"].asBoolean()

    override val isSelfDeafened: Boolean
        get() = json["self_deaf"].asBoolean()

    override val isSelfMuted: Boolean
        get() = json["self_mute"].asBoolean()

    override val isStreaming: Boolean
        get() = json["self_stream"].asBoolean()

    override val isSuppressed: Boolean
        get() = json["suppress"].asBoolean()

    override val requestToSpeakTimestamp: String?
        get() = formatZonedDateTime(json["request_to_speak_timestamp"].asText())

    override suspend fun requestVoiceRegion(): CompletableFuture<VoiceState.VoiceRegion> {
        return ydwk.restApiManager.get(EndPoint.VoiceEndpoint.GET_VOICE_REGIONS).execute { it ->
            val jsonBody = it.jsonBody
            if (jsonBody == null) {
                throw IllegalStateException("json body is null")
            } else {
                VoiceRegionImpl(ydwk, jsonBody, jsonBody["id"].asLong())
            }
        }
    }

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).add("sessionId", sessionId).toString()
    }

    class VoiceRegionImpl(
        override val ydwk: YDWK,
        override val json: JsonNode,
        override val idAsLong: Long
    ) : VoiceState.VoiceRegion {
        override val isOptimal: Boolean
            get() = json["optimal"].asBoolean()

        override val isDeprecated: Boolean
            get() = json["deprecated"].asBoolean()

        override val isCustom: Boolean
            get() = json["custom"].asBoolean()

        override fun toString(): String {
            return EntityToStringBuilder(ydwk, this).name(name).toString()
        }

        override var name: String = json["name"].asText()
    }
}