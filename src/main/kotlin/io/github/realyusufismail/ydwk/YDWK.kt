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
package io.github.realyusufismail.ydwk

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.realyusufismail.ydwk.entities.Application
import io.github.realyusufismail.ydwk.entities.Bot
import io.github.realyusufismail.ydwk.impl.event.handle.EventSubscriber
import io.github.realyusufismail.ydwk.ws.WebSocketManager
import io.github.realyusufismail.ydwk.ws.util.LoggedIn

interface YDWK {

    /** Used to create a json object. */
    val objectNode: ObjectNode

    /** Used to parse json, i.e convert plain text json to Jackson classes such as JsonNode. */
    val objectMapper: ObjectMapper

    /** This is where the websocket is created. */
    val webSocketManager: WebSocketManager?

    /** Used to get the properties of the bot. */
    val bot: Bot?

    /** Used to get the properties of the application. */
    val application: Application?

    /** Used to get information about when the bot logged in. */
    val loggedInStatus: LoggedIn?

    /** Used to indicated that bot has connected to the websocket. */
    val waitForConnection: YDWK

    /** Used to send and receive an event. */
    val eventSubscriber: EventSubscriber

    /** Used to shut down the websocket manager */
    fun shutdown()
}
