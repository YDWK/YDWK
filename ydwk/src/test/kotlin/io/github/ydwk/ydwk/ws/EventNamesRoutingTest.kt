/*
 * Copyright 2024-2026 YDWK inc.
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
package io.github.ydwk.ydwk.ws

import io.github.ydwk.ydwk.testkit.DiscordJsonFixtures
import io.github.ydwk.ydwk.ws.util.EventNames
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EventNamesRoutingTest {

    @Test
    @DisplayName("maps READY gateway payload to READY event enum")
    fun maps_ready_gateway_payload_to_ready_event_enum() {
        val payload = DiscordJsonFixtures.gatewayEvent("READY.json")

        assertEquals(EventNames.READY, EventNames.getValue(payload["t"].asText()))
    }

    @Test
    @DisplayName("maps MESSAGE_CREATE gateway payload to MESSAGE_CREATE event enum")
    fun maps_message_create_gateway_payload_to_message_create_event_enum() {
        val payload = DiscordJsonFixtures.gatewayEvent("MESSAGE_CREATE.json")

        assertEquals(EventNames.MESSAGE_CREATE, EventNames.getValue(payload["t"].asText()))
    }

    @Test
    @DisplayName("returns UNKNOWN for unsupported gateway event names")
    fun returns_unknown_for_unsupported_gateway_event_names() {
        assertEquals(EventNames.UNKNOWN, EventNames.getValue("UNSUPPORTED_EVENT_TYPE"))
    }
}

