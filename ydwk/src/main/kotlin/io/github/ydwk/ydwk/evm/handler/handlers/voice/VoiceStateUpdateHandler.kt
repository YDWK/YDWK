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
package io.github.ydwk.ydwk.evm.handler.handlers.voice

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.ydwk.evm.event.events.voice.VoiceStateEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.voice.getVoiceConnection

class VoiceStateUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val voiceState: VoiceState = ydwk.entityInstanceBuilder.buildVoiceState(json)

        val member = voiceState.member
        val channel = voiceState.channel

        if (member == null) {
            ydwk.logger.debug("Voice member is null")
        } else {
            val botAsMember = voiceState.guild?.getBotAsMember()

            if (botAsMember == null) {
                ydwk.logger.debug("Bot as member is null")
            } else if (member.idAsLong == botAsMember.idAsLong) {
                botAsMember.voiceState = if (channel != null) voiceState else null
            } else {
                val action = if (channel != null) "save" else "remove"
                ydwk.logger.debug("No need to $action i think")
            }
        }

        ydwk.logger.debug("Setting voice state")

        (voiceState.guild)?.getVoiceConnection()?.setVoiceState(voiceState)

        ydwk.emitEvent(VoiceStateEvent(ydwk, voiceState))
    }
}
