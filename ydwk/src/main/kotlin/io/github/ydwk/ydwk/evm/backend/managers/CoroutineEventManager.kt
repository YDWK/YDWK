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
package io.github.ydwk.ydwk.evm.backend.managers

import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class CoroutineEventManager : IEventManager, CoroutineScope by CoroutineScope(Dispatchers.Default) {
    private val listeners: MutableList<CoroutineEventListener> = ArrayList()
    private val logger = LoggerFactory.getLogger(CoroutineEventManager::class.java)

    override suspend fun emitEvent(event: GenericEvent) {
        coroutineScope {
            launch {
                for (listener in listeners) {
                    try {
                        listener.onEvent(event)
                    } catch (e: Throwable) {
                        logger.error(
                            "Error while emitting event ${event.javaClass.simpleName} to ${listener.javaClass.simpleName}",
                            e)
                        if (e is Error) {
                            throw e
                        }
                    }
                }
            }
        }
    }

    override fun addEvent(event: Any) {
        if (event is CoroutineEventListener) {
            listeners.add(event)
        } else {
            require(event !is CoroutineEventListener) {
                "Event must be an instance of EventListener"
            }
        }
    }

    override fun removeEvent(event: Any) {
        if (event is CoroutineEventListener) {
            listeners.remove(event)
        } else {
            require(event !is CoroutineEventListener) {
                "Event must be an instance of EventListener"
            }
        }
    }

    override fun removeAllEvents() {
        listeners.clear()
    }

    override val events: MutableList<Any>
        get() = Collections.unmodifiableList(ArrayList(listeners))
}
