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
package io.github.ydwk.ydwk.evm.listeners

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.backend.update.IEventUpdate
import io.github.ydwk.ydwk.evm.event.events.gateway.DisconnectEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ReadyEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ResumeEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ShutDownEvent

interface CoreListeners : IEventListener {

    /**
     * Listens to all events
     *
     * @param event The event
     */
    fun onGenericEvent(event: GenericEvent) {}

    /**
     * Listens to all update events
     *
     * @param eventUpdate The update event
     */
    fun onGenericUpdate(eventUpdate: IEventUpdate<*, *>) {}

    /**
     * Listens to ready event
     *
     * @param event The ready event
     */
    fun onReady(event: ReadyEvent) {}

    /**
     * Listens to Disconnect event
     *
     * @param event The disconnect event
     */
    fun onDisconnect(event: DisconnectEvent) {}

    /**
     * Listens to Resume event
     *
     * @param event The resume event
     */
    fun onResume(event: ResumeEvent) {}

    /**
     * Listens to ShutDown event
     *
     * @param event The shutdown event
     */
    fun onShutDown(event: ShutDownEvent) {}

    override fun onEvent(event: GenericEvent) {
        onGenericEvent(event)

        if (event is IEventUpdate<*, *>) {
            onGenericUpdate(event)
        }

        when (event) {
            is ReadyEvent -> onReady(event)
            is DisconnectEvent -> onDisconnect(event)
            is ResumeEvent -> onResume(event)
            is ShutDownEvent -> onShutDown(event)
        }
    }
}
