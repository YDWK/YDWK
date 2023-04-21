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
package io.github.ydwk.ydwk.evm.backend.event

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.addEventListeners

inline fun <reified T : GenericEvent> YDWK.on(
    crossinline consumer: suspend GenericEvent.(T) -> Unit,
): CoroutineEventListener {
    return object : CoroutineEventListener {
            override suspend fun onEvent(event: GenericEvent) {
                if (event is T) {
                    event.consumer(event)
                }
            }
        }
        .also { this.addEventListeners(it) }
}
