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
package io.github.realyusufismail.ws

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.realyusufismail.ydwkimpl.YDWKImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ConnectSystem(
    private var ydwk: YDWKImpl,
    private var token: String,
    private var intent: Int
) : WebSocketManager(ydwk, token, intent) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger

    fun identify() {
        // event data
        val d: ObjectNode =
            ydwk.objectNode
                .put("token", token)
                .put("intents", intent)
                .set(
                    "properties",
                    ydwk.objectNode
                        .put("os", System.getProperty("os.name"))
                        .put("browser", "YDWK")
                        .put("device", "YDWK"))

        val json: JsonNode = ydwk.objectNode.put("op", 2).set("d", d)
        webSocket?.sendText(json.toString())
        logger.info("Connected to Discord")
    }

    fun resume() {}
}
