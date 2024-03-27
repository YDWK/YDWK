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
package io.github.ydwk.ydwk.evm.event.events.gateway

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.GatewayEvent
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when the websocket triggers a reconnect event.
 *
 * @param ydwk The [YDWK] instance.
 */
@GatewayEvent data class ReconnectEvent(override val ydwk: YDWK) : Event(ydwk)
