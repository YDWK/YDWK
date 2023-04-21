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
package io.github.ydwk.ydwk.event

import io.github.ydwk.ydwk.emitEvent
import io.github.ydwk.ydwk.evm.backend.event.on
import io.github.ydwk.ydwk.exception.TestException
import io.github.ydwk.ydwk.impl.YDWKImpl
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

object EvenTester {
    private var embedJson: String? = null
    private var retriedAmount = 0

    @Test
    @Order(1)
    fun runEvent() {
        val ydwk = YDWKImpl(OkHttpClient())
        emitTestEvent(ydwk)

        ydwk.on<TestEvent> { onTest() }

        if (embedJson == null) {
            retriedAmount++
            // wait for event and try again
            Thread.sleep(5000)
            runEvent()
        } else {
            assert(embedJson)
        }
    }

    private fun assert(embedJson: String?) {
        if (retriedAmount > 3) {
            throw TestException("Failed to get event after 5 retries")
        } else {
            assertNotNull(embedJson)
            assertEquals(
                """
            {
              "title" : "Event Test"
            }
        """
                    .trimIndent(),
                embedJson)
        }
    }

    private fun emitTestEvent(ydwk: YDWKImpl) {
        ydwk.emitEvent(TestEvent(ydwk))
    }

    private fun onTest() {
        embedJson =
            """
            {
              "title" : "Event Test"
            }
        """
                .trimIndent()
    }
}
