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
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.yde.impl.entities.VoiceStateImpl
import io.github.ydwk.ydwk.evm.event.events.voice.VoiceStateEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class VoiceStateUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val userId = json.get("user_id").asText()
        val guildId = json.get("guild_id").asText()
        val guild = ydwk.getGuildById(guildId) ?: throw IllegalStateException("Guild not found")
        val bot = ydwk.bot
        val voiceState = VoiceStateImpl(ydwk, json)

        val newMember: Member =
            if (userId == (bot?.id ?: throw IllegalStateException("Bot not found"))) {
                val botAsMember = (guild as GuildImpl).botAsMember
                botAsMember.voiceState = voiceState
                botAsMember
            } else {
                val m = ydwk.memberCache.getOrPut(voiceState.member!!)
                m.voiceState = voiceState
                m
            }

        // TODO: returning null
        ydwk.emitEvent(VoiceStateEvent(ydwk, voiceState, newMember))
    }
}
