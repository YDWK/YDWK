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
package io.github.realyusufismail.ws.io.github.realyusufismail.event

import io.github.realyusufismail.event.TestEvent
import io.github.realyusufismail.ws.io.github.realyusufismail.event.handle.EventManager
import io.github.realyusufismail.ws.io.github.realyusufismail.event.handle.IEventManager
import io.github.realyusufismail.ws.io.github.realyusufismail.event.handle.on
import io.github.realyusufismail.ydwk.event.Event
import io.github.realyusufismail.ydwk.event.backend.event.on
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test

class EvenTester {
    private val eventListener: IEventManager = EventManager()

    @Test
    fun testEvent() {
        eventListener.emitEvent(TestEvent(YDWKImpl(OkHttpClient())))
        var name = ""
        this.on<TestEvent> { name = "Yusuf" }
        // assertEquals("Yusuf", name, "Name is not Yusuf")
    }

    @Test
    fun testYDWKEvent() {
        val ydwk = YDWKImpl(OkHttpClient())
        ydwk.emitEvent(TestEvent(ydwk))
        var name: String? = null
        ydwk.on<TestEvent> { name = "Yusuf" }
        // assertEquals("Yusuf", name, "Name is not Yusuf")
    }

    fun addEvent(vararg eventListeners: Any) {
        eventListeners.forEach { eventListener.addEvent(it) }
    }

    fun removeEvent(vararg eventListeners: Any) {
        eventListeners.forEach { eventListener.removeEvent(it) }
    }

    inline fun <reified T : Event> onEvent(noinline listener: (T) -> Unit) {
        this.on<T> { listener(it) }
    }
}
