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
package io.github.ydwk.ydwk.evm.handler.handlers.voice

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.Bot
import io.github.ydwk.ydwk.evm.event.events.voice.VoiceStateEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.ChannelImpl
import io.github.ydwk.ydwk.impl.entities.VoiceStateImpl
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl

class VoiceStateUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val userId = json.get("user_id").asText()
        val voiceState = VoiceStateImpl(ydwk, json)
        val newMember = ydwk.memberCache.getOrPut(voiceState.member!!)
        ydwk.memberCache.updateVoiceState(newMember, voiceState, true)

        val bot: Bot = ydwk.bot ?: return
        if (bot.id == userId) {
            handleBotVoiceStateUpdate(voiceState)
        }

        ydwk.emitEvent(VoiceStateEvent(ydwk, voiceState, newMember))
    }

    private fun handleBotVoiceStateUpdate(voiceState: VoiceStateImpl) {
        val sessionId = json.get("session_id").asText()
        val channelId = json.get("channel_id").asText()
        val guildChannel =
            (ydwk.cache[channelId, CacheIds.CHANNEL] as ChannelImpl).channelGetter.asGuildChannel()
        val vc = guildChannel?.guildChannelGetter?.asGuildVoiceChannel()
        if (vc != null) {
            val voiceConnection: VoiceConnectionImpl? =
                (ydwk.getPendingVoiceConnectionById(vc.guild.idAsLong) as VoiceConnectionImpl?)
            if (voiceConnection != null) {
                voiceConnection.sessionId = sessionId
                voiceConnection.attemptConnect()
            }

            if (vc.guild.voiceConnection != null) {
                (vc.guild.voiceConnection!! as VoiceConnectionImpl).sessionId = sessionId
                (vc.guild.voiceConnection!! as VoiceConnectionImpl).attemptConnect()
            }
        } else {
            ydwk.logger.warn("VoiceStateUpdateHandler: Bot is not in a voice channel")
        }
    }
}
