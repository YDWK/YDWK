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
package io.github.ydwk.ydwk.ws.util

import com.fasterxml.jackson.databind.JsonNode
import com.neovisionaries.ws.client.WebSocket
import io.github.ydwk.ydwk.YDWK
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HeartBeat(
    private val ydwk: YDWK,
    private val webSocket: WebSocket,
    private var heartbeatThread: Future<*>,
    val scheduler: ScheduledExecutorService,
    var heartbeatsMissed: Int,
    var heartbeatStartTime: Long
) {
    private val logger: Logger = LoggerFactory.getLogger(HeartBeat::class.java)

    public fun startGateWayHeartbeat(heartbeatInterval: Long, connected: Boolean, seq: Int?) {
        try {
            val rawSocket: Socket = webSocket.socket
            rawSocket.soTimeout =
                (heartbeatInterval + 10000).toInt() // setup a timeout when we miss
            // heartbeats
        } catch (ex: SocketException) {
            logger.warn("Failed to setup timeout for socket", ex)
        }

        heartbeatThread =
            scheduler.scheduleAtFixedRate(
                {
                    if (connected) {
                        sendGateWayHeartbeat(seq)
                    } else {
                        logger.info("Not sending heartbeat because not connected")
                    }
                },
                0,
                heartbeatInterval,
                TimeUnit.MILLISECONDS)
    }

    private fun sendGateWayHeartbeat(seq: Int?) {
        val s: Int? = seq

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", OpCode.HEARTBEAT.code).put("d", s)

        if (heartbeatsMissed >= 2) {
            heartbeatsMissed = 0
            logger.warn("Heartbeat missed, will attempt to reconnect")
            webSocket.sendClose(
                CloseCode.MISSED_HEARTBEAT.code(), CloseCode.MISSED_HEARTBEAT.getReason())
        } else {
            heartbeatsMissed += 1
            webSocket.sendText(heartbeat.toString())
            heartbeatStartTime = System.currentTimeMillis()
        }
    }
}
