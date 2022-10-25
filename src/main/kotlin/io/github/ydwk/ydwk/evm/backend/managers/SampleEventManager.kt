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
package io.github.ydwk.ydwk.evm.backend.managers

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.event.Event
import java.util.*

class SampleEventManager(private var eventListeners: MutableList<IEventListener> = ArrayList()) :
    IEventManager {
    // Null as there is no default value for this parameter
    var event: Event? = null

    override fun emitEvent(event: GenericEvent) {
        this.event = event as Event
        eventListeners.forEach { listener -> listener.onEvent(event) }
    }

    override fun addEvent(event: Any) {
        if (event is IEventListener) {
            eventListeners.add(event)
        } else {
            throw IllegalArgumentException("Event must be an instance of EventListener")
        }
    }

    override fun removeEvent(event: Any) {
        if (event is IEventListener) {
            eventListeners.remove(event)
        } else {
            throw IllegalArgumentException("Event must be an instance of EventListener")
        }
    }

    override fun removeAllEvents() {
        eventListeners.clear()
    }

    override val events: MutableList<Any>
        get() = Collections.unmodifiableList(ArrayList(eventListeners))
}
