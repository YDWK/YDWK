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

import io.github.realyusufismail.event.handle.EventTimeout
import io.github.realyusufismail.event.handle.toTimeout
import io.github.realyusufismail.ydwk.event.Event
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.time.Duration
import kotlinx.coroutines.*
import org.slf4j.Logger

fun getDefaultScope() = CoroutineScope(Dispatchers.Default)

open class TestCoroutineEventReceiver(
    private val scope: CoroutineScope = getDefaultScope(),
    private var timeout: Duration = Duration.INFINITE
) : TestIEventReceiver, CoroutineScope by scope {
    private val events = CopyOnWriteArrayList<TestCoroutineEventListener>()
    private val logger: Logger =
        org.slf4j.LoggerFactory.getLogger(TestCoroutineEventReceiver::class.java)

    private fun timeout(event: Any) =
        when {
            (event is TestCoroutineEventListener && event.eventTimeout != EventTimeout.ZERO) ->
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
                            logger.warn("Event ${event::class.simpleName} timed out after $timeout")
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
        if (eventReceiver is TestCoroutineEventListener) {
            events.add(eventReceiver)
        } else {
            throw IllegalArgumentException(
                "Event receiver ${eventReceiver::class.simpleName} is not a CoroutineEventListener")
        }
    }

    override fun removeEventReceiver(eventReceiver: Any) {
        if (eventReceiver is TestCoroutineEventListener) {
            events.remove(eventReceiver)
        } else {
            throw IllegalArgumentException(
                "Event receiver ${eventReceiver::class.simpleName} is not a CoroutineEventListener")
        }
    }

    inline fun <reified T : Event> onEvent(
        timeout: Duration? = null,
        crossinline consumer: suspend TestCoroutineEventListener.(T) -> Unit
    ): TestCoroutineEventListener {
        return object : TestCoroutineEventListener {

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
