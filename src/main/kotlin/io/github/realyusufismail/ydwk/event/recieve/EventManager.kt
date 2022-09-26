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
package io.github.realyusufismail.ydwk.event.recieve

import io.github.realyusufismail.ydwk.event.Event
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventManager : IEventManager, CoroutineScope by CoroutineScope(Dispatchers.Default) {
    private val listeners = CopyOnWriteArrayList<EventListener>()

    override fun emitEvent(event: Event) {
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
        if (event is EventListener) {
            listeners.add(event)
        } else {
            throw IllegalArgumentException("Event must be an instance of EventListener")
        }
    }

    override fun removeEvent(event: Any) {
        if (event is EventListener) {
            listeners.remove(event)
        } else {
            throw IllegalArgumentException("Event must be an instance of EventListener")
        }
    }

    override fun removeAllEvents() {
        listeners.clear()
    }

    override val events: MutableList<EventListener>
        get() = Collections.unmodifiableList(ArrayList(listeners))
}
