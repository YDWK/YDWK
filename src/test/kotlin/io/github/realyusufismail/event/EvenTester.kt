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

import io.github.realyusufismail.ws.io.github.realyusufismail.TestEvent
import io.github.realyusufismail.ws.io.github.realyusufismail.event.handle.TestCoroutineEventReceiver
import io.github.realyusufismail.ws.io.github.realyusufismail.event.handle.TestIEventReceiver
import io.github.realyusufismail.ws.io.github.realyusufismail.event.handle.on
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test

class EvenTester {
    val iEventReceiver: TestIEventReceiver = TestCoroutineEventReceiver()

    @Test
    fun testEvent() {
        iEventReceiver.handleEvent(TestEvent(YDWKImpl(OkHttpClient())))

        var name: String? = null
        this.on<TestEvent> { name = "Yusuf" }

        assertNotNull(name, "Name is null")
        assertEquals("Yusuf", name, "Name is not Yusuf")
    }
}
