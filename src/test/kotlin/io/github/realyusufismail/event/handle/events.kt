/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ws.io.github.realyusufismail.event.handle

import io.github.realyusufismail.ws.io.github.realyusufismail.event.EvenTester
import io.github.realyusufismail.ydwk.event.Event

inline fun <reified T : Event> EvenTester.on(
    crossinline consumer: suspend Event.(T) -> Unit
): EventListener {
    return object : EventListener {
            override suspend fun onEvent(event: Event) {
                if (event is T) {
                    event.consumer(event)
                }
            }
        }
        .also { this.addEvent(it) }
}
