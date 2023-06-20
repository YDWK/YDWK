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

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.util.Incubating
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.event.EventListeners
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import java.util.concurrent.ScheduledExecutorService

/**
 * The main class of the YDWK library. This class is used to interact with the Discord API. Also
 * contains many things such as the embed builder for example.
 */
interface YDWK : YDE {
    /** This is where the websocket is created. */
    val webSocketManager: WebSocketManager?

    /**
     * Gets some application properties sent by discord's Ready event.
     *
     * @return the [PartialApplication] object
     */
    var partialApplication: PartialApplication?

    /**
     * The properties of the application.
     *
     * @return the [Application] object
     */
    val application: Application?

    /**
     * Gets information about when the bot logged in.
     *
     * @return the [LoggedIn] object
     */
    val loggedInStatus: LoggedIn?

    /**
     * A suspend function that waits for the READY gateway event to be received.
     *
     * @return The [YDWK] instance.
     */
    suspend fun awaitReady(): YDWK

    /**
     * Adds an event listener.
     *
     * @param eventListeners The event listeners to be added.
     */
    fun addEventListeners(vararg eventListeners: Any)

    /**
     * Removes an event listener.
     *
     * @param eventListeners The event listeners to be removed.
     */
    fun removeEventListeners(vararg eventListeners: Any)

    /**
     * Emits an event
     *
     * @param event The event to be emitted.
     */
    fun emitEvent(event: GenericEvent)

    /**
     * Emits an event
     */
    var GenericEvent.emitEvent: (GenericEvent) -> Unit
        get() = ::emitEvent
        set(value) = value(this)

    /** Shuts down the websocket manager */
    fun shutdownAPI()

    /**
     * The bot's uptime.
     *
     * @return The bot's uptime.
     */
    val uptime: Instant

    /**
     * The default ScheduledExecutorService.
     *
     * @return The [ScheduledExecutorService] object.
     */
    val defaultScheduledExecutorService: ScheduledExecutorService

    /**
     * A shortcut to listening to events.
     *
     * @return The [EventListeners] object.
     */
    val eventListener: EventListeners

    /**
     * Overrides the custom to string method.
     *
     * @return The string representation of the object.
     */
    override fun toString(): String
}
