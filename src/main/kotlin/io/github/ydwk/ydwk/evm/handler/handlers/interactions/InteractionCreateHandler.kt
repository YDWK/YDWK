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
package io.github.ydwk.ydwk.evm.handler.handlers.interactions

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.interaction.AutoCompleteSlashCommandEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.ModelEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.PingEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.message.MessageCommandEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.user.UserCommandEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class InteractionCreateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override suspend fun start() {
        val interaction: Interaction = ydwk.entityInstanceBuilder.buildInteraction(json)
        when (interaction.type) {
            InteractionType.APPLICATION_COMMAND -> {
                val dataJson = json["data"]
                when (ApplicationCommandType.fromInt(dataJson["type"].asInt())) {
                    ApplicationCommandType.CHAT_INPUT -> {
                        val slashCommand =
                            ydwk.entityInstanceBuilder.buildSlashCommand(json, interaction)
                        val event = SlashCommandEvent(ydwk, slashCommand)
                        ydwk.emitEvent(event)
                    }
                    ApplicationCommandType.USER -> {
                        val userCommand =
                            ydwk.entityInstanceBuilder.buildUserCommand(json, interaction)
                        val event = UserCommandEvent(ydwk, userCommand)
                        ydwk.emitEvent(event)
                    }
                    ApplicationCommandType.MESSAGE -> {
                        val messageCommand =
                            ydwk.entityInstanceBuilder.buildMessageCommand(json, interaction)
                        val event = MessageCommandEvent(ydwk, messageCommand)
                        ydwk.emitEvent(event)
                    }
                    else -> throw IllegalArgumentException("Unknown command type")
                }
            }
            InteractionType.MESSAGE_COMPONENT -> {
                val interactionComponent =
                    ydwk.entityInstanceBuilder.buildComponentInteraction(
                        json, GetterSnowFlake.of(json["id"].asLong())) as ComponentInteractionImpl
                val data = interactionComponent.data
                val type = data.componentType
                val customId = data.customId
                MessageComponentHandler(type, customId, interactionComponent, ydwk).handle()
            }
            InteractionType.APPLICATION_COMMAND_AUTOCOMPLETE -> {
                ydwk.emitEvent(AutoCompleteSlashCommandEvent(ydwk, interaction))
            }
            InteractionType.MODAL_SUBMIT -> {
                ydwk.emitEvent(ModelEvent(ydwk, interaction))
            }
            InteractionType.PING -> {
                ydwk.emitEvent(PingEvent(ydwk, interaction))
            }
            InteractionType.UNKNOWN -> {
                ydwk.logger.warn("Unknown interaction type: ${json["type"].asInt()}")
            }
        }
    }
}
