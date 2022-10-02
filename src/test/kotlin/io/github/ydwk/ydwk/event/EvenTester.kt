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
package io.github.ydwk.ydwk.event

import io.github.ydwk.ydwk.event.backend.event.on
import io.github.ydwk.ydwk.event.events.ReadyEvent
import io.github.ydwk.ydwk.impl.YDWKImpl
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test

object EvenTester {
    var name = "YDWK"

    @Test
    fun testEvent() {
        val ydwk = YDWKImpl(OkHttpClient())
        ydwk.emitEvent(TestEvent(ydwk))

        ydwk.on<ReadyEvent> { println("Ready!") }
    }
}
