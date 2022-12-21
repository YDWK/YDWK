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

interface IEventManager {
    /**
     * Handels the event when it is called
     *
     * @param event The event that is called
     */
    fun emitEvent(event: GenericEvent)

    /**
     * Adds the event to the event handler
     *
     * @param event The event that is called
     */
    fun addEvent(event: Any)

    /**
     * Removes the event from the event handler
     *
     * @param event The event that is called
     */
    fun removeEvent(event: Any)

    /** Removes all the events from the event handler */
    fun removeAllEvents()

    /** The registered event listeners */
    val events: MutableList<Any>
}
