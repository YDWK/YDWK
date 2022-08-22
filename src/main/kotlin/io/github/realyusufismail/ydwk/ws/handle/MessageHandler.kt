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
import io.github.realyusufismail.ydwk.ws.util.CloseCode
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent
import io.github.realyusufismail.ydwk.ws.util.OpCode
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.concurrent.TimeUnit
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MessageHandler(ydwk: YDWKImpl, token: String, intent: List<GateWayIntent>) :
    WebSocketManager(ydwk, token, intent) {
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
        logger.info("Received message: {}", payload)
    }

    private fun onOpCode(opCode: Int, d: JsonNode) {
        when (val op: OpCode = OpCode.fromCode(opCode)) {
            OpCode.HELLO -> {
                logger.debug("Received " + op.name)
                val heartbeatInterval: Int = d.get("heartbeat_interval").asInt()
                sendHeartbeat(heartbeatInterval)
            }
            OpCode.HEARTBEAT -> {
                logger.debug("Received " + op.name)
                sendHeartbeat()
            }
            OpCode.HEARTBEAT_ACK -> logger.debug("Heartbeat acknowledged")
            else -> {
                logger.error("Unknown opcode: $opCode")
            }
        }
    }

    private fun sendHeartbeat(heartbeatInterval: Int) {
        try {
            val rawSocket: Socket? = webSocket?.socket
            if (rawSocket != null) rawSocket.soTimeout = heartbeatInterval + 10000
        } catch (ex: SocketException) {
            logger.warn("Failed to setup timeout for socket", ex)
        }

        Timer()
            .scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        sendHeartbeat()
                    }
                },
                0,
                TimeUnit.SECONDS.toMillis(heartbeatInterval.toLong()))
    }

    private fun sendHeartbeat() {
        if (webSocket == null) {
            logger.debug("WebSocket is null, cancelling timer")
            return
        }

        val s: Int? = if (seq != null) seq else null

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", OpCode.HEARTBEAT.getCode()).put("d", s)

        if (heartbeatsMissed >= 2) {
            heartbeatsMissed = 0
            logger.warn("Heartbeat missed, will attempt to reconnect")
            webSocket?.disconnect(CloseCode.RECONNECT.code, "ZOMBIE CONNECTION")
        } else {
            heartbeatsMissed += 1
            webSocket?.sendText(heartbeat.toString())
            heartbeatStartTime = System.currentTimeMillis()
            logger.debug("Sent heartbeat")
        }
    }

    private fun onEventType(eventType: String, d: JsonNode) {}
}
