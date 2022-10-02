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
package io.github.ydwk.ydwk.event.backend.managers

import io.github.realyusufismail.ydwk.event.backend.event.CoroutineEventListener
import io.github.realyusufismail.ydwk.event.backend.event.GenericEvent
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoroutineEventManager : IEventManager, CoroutineScope by CoroutineScope(Dispatchers.Default) {
    private val listeners: MutableList<CoroutineEventListener> = ArrayList()

    override fun emitEvent(event: GenericEvent) {
        launch {
            for (listener in listeners) {
                try {
                    listener.onEvent(event)
                } catch (e: Throwable) {
                    YDWKImpl.logger.error(
                        "Error while emitting event ${event.javaClass.simpleName} to ${listener.javaClass.simpleName}",
                        e)
                    if (e is Error) {
                        throw e
                    }
                }
            }
        }
    }

    override fun addEvent(event: Any) {
        if (event is CoroutineEventListener) {
            listeners.add(event)
        } else {
            throw IllegalArgumentException(
                "Event ${event.javaClass.simpleName} is not a valid event listener")
        }
    }

    override fun removeEvent(event: Any) {
        if (event is CoroutineEventListener) {
            listeners.remove(event)
        } else {
            throw IllegalArgumentException("Event must be an instance of EventListener")
        }
    }

    override fun removeAllEvents() {
        listeners.clear()
    }

    override val events: MutableList<Any>
        get() = Collections.unmodifiableList(ArrayList(listeners))
}
