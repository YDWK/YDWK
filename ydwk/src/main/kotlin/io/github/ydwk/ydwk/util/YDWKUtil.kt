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
package io.github.ydwk.ydwk.util

import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent

/** @return the [YDWK] instance which is the main interface of the YDWK Wrapper */
val GenericEntity.ydwk
    get() = (yde as YDWK)

/** Adds an event listener. */
val Array<out Any>.addEventListeners: (YDWK, Array<out Any>) -> Unit
    get() = { ydwk, listeners -> ydwk.addEventListeners(*listeners) }

/** Adds an event listener. */
val Sequence<Any>.addEventListeners: (YDWK, Sequence<Any>) -> Unit
    get() = { ydwk, listeners -> ydwk.addEventListeners(*listeners.toList().toTypedArray()) }

/** Adds an event listener. */
val Iterable<Any>.addEventListeners: (YDWK, Iterable<Any>) -> Unit
    get() = { ydwk, listeners -> ydwk.addEventListeners(*listeners.toList().toTypedArray()) }

/** Removes an event listener. */
val Array<out Any>.removeEventListeners: (YDWK, Array<out Any>) -> Unit
    get() = { ydwk, listeners -> ydwk.removeEventListeners(*listeners) }

/** Removes an event listener. */
val Sequence<Any>.removeEventListeners: (YDWK, Sequence<Any>) -> Unit
    get() = { ydwk, listeners -> ydwk.removeEventListeners(*listeners.toList().toTypedArray()) }

/** Removes an event listener. */
val Iterable<Any>.removeEventListeners: (YDWK, Iterable<Any>) -> Unit
    get() = { ydwk, listeners -> ydwk.removeEventListeners(*listeners.toList().toTypedArray()) }

/** Emits an event. */
fun GenericEvent.emitEvent() {
    ydwk.emitEvent(this)
}
