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
package io.github.realyusufismail.ydwk.impl.event.handle.normal

import io.github.realyusufismail.ydwk.impl.event.Event
import io.github.realyusufismail.ydwk.impl.event.handle.IEventReceiver

class EventReceiver : IEventReceiver {
    var event: Event? = null
    var iEvents: MutableList<IEvent> = mutableListOf()

    override fun addEventReceiver(eventReceiver: Any) {
        if (eventReceiver is IEvent) {
            iEvents.add(eventReceiver)
        } else {
            throw IllegalArgumentException("EventReceiver must be IEvent")
        }
    }

    override fun removeEventReceiver(eventReceiver: Any) {
        if (eventReceiver is IEvent) {
            iEvents.remove(eventReceiver)
        } else {
            throw IllegalArgumentException("EventReceiver must be IEvent")
        }
    }

    override fun handleEvent(event: Event) {
        this.event = event
        iEvents.forEach { it.onEvent(event) }
    }
}
