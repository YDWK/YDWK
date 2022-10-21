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
package io.github.ydwk.ydwk.event.backend.event

import io.github.ydwk.ydwk.YDWKWebSocket
import io.github.ydwk.ydwk.event.Event

inline fun <reified T : Event> YDWKWebSocket.on(
    crossinline consumer: suspend Event.(T) -> Unit
): CoroutineEventListener {
    return object : CoroutineEventListener {
            override suspend fun onEvent(event: GenericEvent) {
                if (event is T) {
                    event.consumer(event)
                }
            }
        }
        .also { this.addEvent(it) }
}
