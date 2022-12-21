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
package io.github.ydwk.ydwk.evm.handler.handlers.interactions

import io.github.ydwk.ydwk.evm.event.events.interaction.button.ButtonClickEvent
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.ydwk.impl.interaction.message.button.ButtonImpl
import io.github.ydwk.ydwk.interaction.message.ComponentType

class MessageComponentHandler(
    val type: ComponentType,
    private val customId: String,
    private val interactionComponent: ComponentInteractionImpl,
    val ydwk: YDWKImpl
) {
    fun handle() {
        for (component in interactionComponent.components) {
            // filter through and find the component that matches the customId and type
            if (component.type == ComponentType.ACTION_ROW) {
                for (children in component.children) {
                    when (type) {
                        ComponentType.BUTTON -> {
                            if (customId == children.customId) {
                                ydwk.emitEvent(
                                    ButtonClickEvent(
                                        ydwk, ButtonImpl(interactionComponent, children)))
                            }
                        }
                        ComponentType.SELECT_MENU -> TODO("Do something similar to buttons")
                        ComponentType.TEXT_INPUT -> TODO("Do something similar to buttons")
                        ComponentType.USER_SELECT_MENU -> TODO("Do something similar to buttons")
                        ComponentType.ROLE_SELECT_MENU -> TODO("Do something similar to buttons")
                        ComponentType.MENTIONABLE_SELECT_MENU ->
                            TODO("Do something similar to buttons")
                        ComponentType.CHANNEL_SELECT_MENU -> TODO("Do something similar to buttons")
                        ComponentType.UNKNOWN -> TODO("Do something similar to buttons")
                        else -> {
                            // if action row, do nothing else warn
                            if (children.type != ComponentType.ACTION_ROW) {
                                ydwk.logger.warn("Unknown component type: ${children.type}")
                            }
                        }
                    }
                }
            } else {
                // do nothing
            }
        }
    }
}
