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
package io.github.realyusufismail.ydwk.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.ws.WebSocketManager
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent

class YDWKImpl : YDWK {

    /** Used to create a json object. */
    override val objectNode: ObjectNode
        get() = JsonNodeFactory.instance.objectNode()

    /** Used to parse json, i.e convert plain text json to Jackson classes such as JsonNode. */
    override val objectMapper: ObjectMapper
        get() = ObjectMapper()

    override var webSocketManager: WebSocketManager? = null
        private set

    /** Used to shut down the websocket manager */
    override fun shutdown() {
        webSocketManager?.shutdown()
    }

    /**
     * Used to start the websocket manager
     *
     * @param token The token of the bot which is used to authenticate the bot.
     * @param intents The gateway intent which will decide what events are sent by discord.
     */
    fun setWebSocketManager(token: String, intents: List<GateWayIntent>) {
        this.webSocketManager = WebSocketManager(this, token, intents).connect()
    }
}
