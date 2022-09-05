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
package io.github.realyusufismail.ydwk.impl.event.handle.config

import io.github.realyusufismail.ydwk.impl.event.Event
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
open class EventListener(scope: CoroutineScope = getDefaultScope()) :
    IEventReceiver, CoroutineScope by scope {

    private val log: Logger = LoggerFactory.getLogger(EventListener::class.java)
    private val listeners = CopyOnWriteArrayList<Any>()

    override fun handleEvent(event: Event) {
        launch {
            for (listener in listeners) {
                if (listener is IEventListener) {
                    try {
                        runListener(listener, event)
                    } catch (e: Exception) {
                        log.error("Error while handling event", e)
                    }
                }
            }
        }
    }

    private suspend fun runListener(listener: Any, event: Event) {
        when (listener) {
            is IEventListener -> listener.onEvent(event)
            is IEvent -> listener.onEvent(event)
            else ->
                throw IllegalArgumentException("Listener must implement IEventListener or IEvent")
        }
    }

    override fun addEventReceiver(eventReceiver: Any) {
        listeners.add(
            when (eventReceiver) {
                is IEventListener -> eventReceiver
                is IEvent -> eventReceiver
                else ->
                    throw IllegalArgumentException(
                        "Event receiver must implement IEventListener or IEvent")
            })
    }

    override fun removeEventReceiver(eventReceiver: Any) {
        listeners.remove(
            when (eventReceiver) {
                is IEventListener -> eventReceiver
                is IEvent -> eventReceiver
                else ->
                    throw IllegalArgumentException(
                        "Event receiver must implement IEventListener or IEvent")
            })
    }

    inline fun <reified EventClass : Event> onEvent(
        crossinline block: suspend IEventListener.(EventClass) -> Unit
    ): IEventListener {
        return object : IEventListener {

                override fun cancelEvent() {
                    removeEventReceiver(this)
                }

                override suspend fun onEvent(event: Event) {
                    if (event is EventClass) {
                        block(event)
                    }
                }
            }
            .also { addEventReceiver(it) }
    }
}

fun getDefaultScope(): CoroutineScope {
    return CoroutineScope(
        Dispatchers.Default +
            SupervisorJob() +
            CoroutineExceptionHandler { _, throwable -> throw throwable } +
            EmptyCoroutineContext)
}
 */
