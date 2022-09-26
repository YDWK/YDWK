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

interface IEventManager {
    /**
     * Used to handel the event when it is called
     *
     * @param event The event that is called
     */
    fun emitEvent(event: Event)

    /**
     * Used to add the event to the event handler
     *
     * @param event The event that is called
     */
    fun addEvent(event: Any)

    /**
     * Used to remove the event from the event handler
     *
     * @param event The event that is called
     */
    fun removeEvent(event: Any)

    /** Used to remove all the events from the event handler */
    fun removeAllEvents()

    /** Used to get the registered event listeners */
    val events: MutableList<EventListener>
}
