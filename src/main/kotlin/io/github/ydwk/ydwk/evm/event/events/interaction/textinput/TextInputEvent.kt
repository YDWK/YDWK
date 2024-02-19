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
package io.github.ydwk.ydwk.evm.event.events.interaction.textinput

import io.github.ydwk.yde.interaction.message.textinput.TextInput
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.InteractionEvent
import io.github.ydwk.ydwk.evm.event.Event

/**
 * The event that is triggered when a user submits a text input.
 *
 * @param ydwk the [YDWK] instance
 * @param textInput the [TextInput] that is submitted
 */
@InteractionEvent
data class TextInputEvent(override val ydwk: YDWK, val textInput: TextInput) : Event(ydwk)
