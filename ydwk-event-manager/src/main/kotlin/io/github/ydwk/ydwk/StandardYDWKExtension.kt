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
package io.github.ydwk.ydwk

import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.backend.managers.CoroutineEventManager
import io.github.ydwk.ydwk.evm.backend.managers.SampleEventManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val YDWK.simpleEventManager: SampleEventManager
    get() = SampleEventManager()

val YDWK.coroutineEventManager: CoroutineEventManager
    get() = CoroutineEventManager()

fun YDWK.addEventListeners(vararg eventListeners: Any) {
    for (eventListener in eventListeners) {
        when (eventListener) {
            is IEventListener -> {
                simpleEventManager.addEvent(eventListener)
            }
            is CoroutineEventListener -> {
                coroutineEventManager.addEvent(eventListener)
            }
            else -> {
                error(
                    "Event listener is not an instance of EventListener or CoroutineEventListener")
            }
        }
    }
}

fun YDWK.removeEventListeners(vararg eventListeners: Any) {
    for (eventListener in eventListeners) {
        when (eventListener) {
            is IEventListener -> {
                simpleEventManager.removeEvent(eventListener)
            }
            is CoroutineEventListener -> {
                coroutineEventManager.removeEvent(eventListener)
            }
            else -> {
                error(
                    "Event listener is not an instance of EventListener or CoroutineEventListener")
            }
        }
    }
}

fun YDWK.emitEvent(event: GenericEvent) {
    simpleEventManager.emitEvent(event)
    coroutineEventManager.emitEvent(event)
}

val YDWK.ydwkLogger: Logger
    get() = LoggerFactory.getLogger(YDWK::class.java)
