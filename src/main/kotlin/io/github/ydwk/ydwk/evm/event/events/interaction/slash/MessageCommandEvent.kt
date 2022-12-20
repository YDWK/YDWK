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
package io.github.ydwk.ydwk.evm.event.events.interaction.slash

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.event.Event
import io.github.ydwk.ydwk.interaction.application.type.MessageCommand

/**
 * This event is triggered when a message command is triggered.
 *
 * @param ydwk The [YDWK] instance.
 * @param slash The [MessageCommand] that was triggered.
 */
data class MessageCommandEvent(override val ydwk: YDWK, val slash: MessageCommand) : Event(ydwk)
