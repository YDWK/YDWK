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
import io.github.realyusufismail.ydwk.ws.util.OpCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MessageHandler(
    private var ydwk: YDWKImpl,
    private var token: String,
    private var intent: List<GateWayIntent>
) : WebSocketManager(ydwk, token, intent) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger

    fun handleMessage(message: String) {
        try {
            val payload = ydwk.objectMapper.readTree(message)
            onEvent(payload)
        } catch (e: Exception) {
            logger.error("Error while handling message", e)
        }
    }

    private fun onEvent(payload: JsonNode) {
        val s: Int? = if (payload.hasNonNull("s")) payload.get("s").asInt() else null
        if (s != null) seq = s

        val d: JsonNode = payload.get("d")
        val op: Int? = if (payload.hasNonNull("op")) payload.get("op").asInt() else null

        if (op != null) onOpCode(op, d)

        val t: String? = if (payload.hasNonNull("t")) payload.get("t").asText() else null
        if (t != null) onEventType(t, d)
    }

    private fun onOpCode(opCode: Int, d: JsonNode) {
        val op: OpCode = OpCode.fromCode(opCode)
        when (op) {
            OpCode.HELLO -> {
                logger.debug("Received " + op.name)
                val heartbeatInterval: Int = d.get("heartbeat_interval").asInt()
                sendHeartbeat(heartbeatInterval)
            }
            else -> {
                logger.error("Unknown opcode: $opCode")
            }
        }
    }

    private fun sendHeartbeat(heartbeatInterval: Int) {}

    private fun onEventType(eventType: String, d: JsonNode) {}
}
