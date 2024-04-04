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

import io.github.ydwk.yde.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.ydwk.evm.event.events.interaction.button.ButtonClickEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.selectmenu.*
import io.github.ydwk.ydwk.evm.event.events.interaction.textinput.TextInputEvent
import io.github.ydwk.ydwk.impl.YDWKImpl

// TODO: Rewrite this class to mirror the new changes
class MessageComponentHandler(
    val type: ComponentType,
    private val customId: String,
    private val interactionComponent: ComponentInteractionImpl,
    val ydwk: YDWKImpl,
) {
    fun handle() {
        for (component in interactionComponent.components) {
            // filter through and find the component that matches the customId and type
            if (component.type == ComponentType.ACTION_ROW) {
                when (type) {
                    ComponentType.BUTTON -> {
                        ydwk.emitEvent(
                            ButtonClickEvent(
                                ydwk,
                                ydwk.entityInstanceBuilder.buildButtonInteraction(
                                    interactionComponent.json, interactionComponent.interactionId)))
                    }
                    ComponentType.STRING_SELECT_MENU -> {
                        ydwk.emitEvent(
                            StringSelectMenuEvent(
                                ydwk,
                                ydwk.entityInstanceBuilder.buildStringSelectMenuInteraction(
                                    interactionComponent.json, interactionComponent.interactionId)))
                    }
                    ComponentType.USER_SELECT_MENU -> {
                        ydwk.emitEvent(
                            UserSelectMenuEvent(
                                ydwk,
                                ydwk.entityInstanceBuilder.buildUserSelectMenuInteraction(
                                    interactionComponent.json, interactionComponent.interactionId)))
                    }
                    ComponentType.ROLE_SELECT_MENU -> {
                        ydwk.emitEvent(
                            RoleSelectMenuEvent(
                                ydwk,
                                ydwk.entityInstanceBuilder.buildRoleSelectMenuInteraction(
                                    interactionComponent.json, interactionComponent.interactionId)))
                    }
                    ComponentType.MENTIONABLE_SELECT_MENU ->
                        ydwk.emitEvent(
                            MemberSelectMenuEvent(
                                ydwk,
                                ydwk.entityInstanceBuilder.buildMemberSelectMenuInteraction(
                                    interactionComponent.json, interactionComponent.interactionId)))
                    ComponentType.CHANNEL_SELECT_MENU ->
                        ydwk.emitEvent(
                            ChannelSelectMenuEvent(
                                ydwk,
                                ydwk.entityInstanceBuilder.buildChannelSelectMenuInteraction(
                                    interactionComponent.json, interactionComponent.interactionId)))
                    ComponentType.TEXT_INPUT -> {
                        ydwk.emitEvent(
                            TextInputEvent(
                                ydwk,
                                ydwk.entityInstanceBuilder.buildTextInputInteraction(
                                    interactionComponent.json, interactionComponent.interactionId)))
                    }
                    // else -> {
                    // if action row, do nothing else warn
                    ///  if (children.type != ComponentType.ACTION_ROW) {
                    //   ydwk.logger.warn("Unknown component type: ${children.type}")
                    // }
                    // }
                    ComponentType.ACTION_ROW -> TODO()
                    ComponentType.UNKNOWN -> ydwk.logger.warn("New component type found: $type")
                }
            } else {
                // do nothing
            }
        }
    }
}
