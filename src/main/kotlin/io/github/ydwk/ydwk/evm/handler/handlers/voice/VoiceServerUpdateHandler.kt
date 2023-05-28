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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.evm.handler.handlers.voice

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.voice.impl.util.getVoiceConnection

class VoiceServerUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {

    override suspend fun start() {
        val guildId = json.get("guild_id").asLong()
        val guild = ydwk.getGuildById(guildId) ?: throw IllegalStateException("Guild not found")

        val token: String = json.get("token").asText()
        val endPoint: String = json.get("endpoint").asText()
        val sessionId: String =
            guild.botAsMember.voiceState?.sessionId
                ?: throw IllegalStateException(
                    "SessionId not found, looks like VoiceStateUpdateHandler didn't run")
        val userId: Long =
            guild.botAsMember.voiceState?.user?.idAsLong
                ?: throw IllegalStateException(
                    "UserId not found, looks like VoiceStateUpdateHandler didn't run")
        val voiceConnection: VoiceConnectionImpl =
            (guild as GuildImpl).getVoiceConnection()
                ?: throw IllegalStateException(
                    "VoiceConnection not found, looks like it was not saved")

        try {
            voiceConnection.safeConnect(endPoint, token, sessionId, userId)
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Failed to connect to voice server", e)
        }
    }
}
