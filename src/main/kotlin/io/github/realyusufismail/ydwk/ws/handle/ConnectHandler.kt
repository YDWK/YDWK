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
package io.github.realyusufismail.ydwk.ws.handle

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.ws.WebSocketManager
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent
import io.github.realyusufismail.ydwk.ws.util.OpCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ConnectHandler(ydwk: YDWKImpl, token: String, intent: List<GateWayIntent>) :
    WebSocketManager(ydwk, token, intent) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger

    fun identify() {
        // event data
        val d: ObjectNode =
            ydwk.objectNode
                .put("token", token)
                .put("intents", GateWayIntent.calculateBitmask(intents.toList()))
                .set(
                    "properties",
                    ydwk.objectNode
                        .put("os", System.getProperty("os.name"))
                        .put("browser", "YDWK")
                        .put("device", "YDWK"))

        val json: JsonNode = ydwk.objectNode.put("op", OpCode.IDENTIFY.code).set("d", d)
        webSocket?.sendText(json.toString())
    }

    fun resume() {
        val json = ydwk.objectNode.put("token", token).put("session_id", sessionId).put("seq", seq)

        val identify: ObjectNode = ydwk.objectNode.put("op", OpCode.RESUME.code).set("d", json)

        webSocket?.sendText(identify.toString())
    }
}
