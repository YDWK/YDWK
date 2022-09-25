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
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.time.Duration
import kotlinx.coroutines.*
import org.slf4j.Logger

fun getDefaultScope() = CoroutineScope(Dispatchers.Default)

open class CoroutineEventReceiver(
    private val scope: CoroutineScope = getDefaultScope(),
    private var timeout: Duration = Duration.INFINITE
) : IEventReceiver, CoroutineScope by scope {
    private val events = CopyOnWriteArrayList<CoroutineEventListener>()
    private val logger: Logger =
        org.slf4j.LoggerFactory.getLogger(CoroutineEventReceiver::class.java)

    protected fun timeout(event: Any) =
        when {
            (event is CoroutineEventListener && event.eventTimeout != EventTimeout.ZERO) ->
                event.eventTimeout.duration
            else -> timeout
        }

    override fun handleEvent(event: Event) {
        println("Handling event $event")
        launch {
            events.forEach { listener ->
                try {
                    val timeout = timeout(listener)
                    if (timeout.isPositive() && timeout.isFinite()) {
                        val result =
                            withTimeoutOrNull(timeout.inWholeMilliseconds) {
                                listener.onEvent(event)
                            }

                        if (result == null) {
                            logger.warn(
                                "Event ${event::class.simpleName} timed out after $timeout")
                        }
                    } else {
                        listener.onEvent(event)
                    }
                } catch (e: Exception) {
                    logger.error("Error while handling event ${event::class.simpleName}", e)
                }
            }
        }
    }

    override fun addEventReceiver(eventReceiver: Any) {
        if (eventReceiver is CoroutineEventListener) {
            events.add(eventReceiver)
        } else {
            throw IllegalArgumentException(
                "Event receiver ${eventReceiver::class.simpleName} is not a CoroutineEventListener")
        }
    }

    override fun removeEventReceiver(eventReceiver: Any) {
        if (eventReceiver is CoroutineEventListener) {
            events.remove(eventReceiver)
        } else {
            throw IllegalArgumentException(
                "Event receiver ${eventReceiver::class.simpleName} is not a CoroutineEventListener")
        }
    }

    inline fun <reified T : Event> onEvent(
        timeout: Duration? = null,
        crossinline consumer: suspend CoroutineEventListener.(T) -> Unit
    ): CoroutineEventListener {
        return object : CoroutineEventListener {

                override val eventTimeout: EventTimeout
                    get() = timeout.toTimeout()

                override fun cancelEvent() {
                    removeEventReceiver(this)
                }

                override suspend fun onEvent(event: Event) {
                    println("Event ${event::class.simpleName} received")
                    if (event is T) consumer(event)
                }
            }
            .also { addEventReceiver(it) }
    }
}
