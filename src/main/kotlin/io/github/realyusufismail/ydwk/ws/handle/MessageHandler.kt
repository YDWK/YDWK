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
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.ws.WebSocketManager
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MessageHandler(
    private var ydwk: YDWKImpl,
    private var token: String,
    private var intent: List<GateWayIntent>
) : WebSocketManager(ydwk, token, intent) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger
    private var payload: JsonNode? = null

    fun handleMessage(message: String) {
        try {
            payload = ydwk.objectMapper.readTree(message)
        } catch (e: Exception) {
            logger.error("Error while handling message", e)
        }
    }

    fun onEvent() {
        if (payload == null) {
            logger.error("Payload is null, try again")
            return
        }

        
    }
}
