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
package io.github.realyusufismail.ydwk.ws

import com.fasterxml.jackson.databind.JsonNode
import com.neovisionaries.ws.client.*
import io.github.realyusufismail.ydwk.YDWKInfo
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.ws.handle.ConnectHandler
import io.github.realyusufismail.ydwk.ws.util.CloseCode
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent
import io.github.realyusufismail.ydwk.ws.util.OpCode
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebSocketManager(
    protected var ydwk: YDWKImpl,
    protected var token: String,
    protected var intents: List<GateWayIntent>
) : WebSocketAdapter(), WebSocketListener {
    private val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger
    // Tha main websocket
    protected var webSocket: WebSocket? = null
    private var resumeUrl: String? = null
    protected var sessionId: String? = null
    protected var seq: Int? = null
    private var heartbeatsMissed: Int = 0
    private var heartbeatStartTime: Long = 0
    @Volatile protected var heartbeatThread: Future<*>? = null
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private var connected = false

    @Synchronized
    fun connect(): WebSocketManager {
        val url: String =
            (resumeUrl
                ?: YDWKInfo.DISCORD_GATEWAY_URL.toString()) +
                YDWKInfo.DISCORD_GATEWAY_VERSION.toString() +
                YDWKInfo.JSON_ENCODING.toString()

        try {
            val webSocketFactory = WebSocketFactory()
            if (webSocketFactory.socketTimeout > 0)
                webSocketFactory.socketTimeout = 1000.coerceAtLeast(webSocketFactory.socketTimeout)
            else webSocketFactory.socketTimeout = 10000

            webSocket =
                webSocketFactory
                    .createSocket(url)
                    .addHeader("Accept-Encoding", "gzip")
                    .addListener(this)
                    .connect()
        } catch (e: IOException) {
            resumeUrl = null
            throw RuntimeException(e)
        }
        return this
    }

    @Throws(Exception::class)
    override fun onConnected(websocket: WebSocket, headers: Map<String, List<String>>) {
        if (sessionId == null) {
            logger.info("Connected to gateway")
        } else {
            logger.info("Resuming session$sessionId")
        }

        connected = true
        if (sessionId == null) {
            ConnectHandler(ydwk, token, intents).identify()
        } else {
            ConnectHandler(ydwk, token, intents).resume()
        }
    }

    @Throws(Exception::class)
    override fun onConnectError(websocket: WebSocket, cause: WebSocketException) {
        logger.error("Error connecting to websocket", cause)
    }

    override fun onTextMessage(websocket: WebSocket, text: String) {
        handleMessage(text)
    }

    private fun handleMessage(message: String) {
        try {
            val payload = ydwk.objectMapper.readTree(message)
            onEvent(payload)
        } catch (e: Exception) {
            logger.error("Error while handling message", e)
        }
    }

    private fun onEvent(payload: JsonNode) {
        val d: JsonNode? = if (payload.hasNonNull("d")) payload.get("d") else null
        val opCode: Int = payload.get("op").asInt()
        if (d != null) {
            onOpCode(opCode, d)
        }
    }

    @Throws(Exception::class)
    override fun onDisconnected(
        websocket: WebSocket,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) {
        connected = false
    }

    override fun onError(websocket: WebSocket, cause: WebSocketException) {
        when (cause.cause) {
            is SocketTimeoutException -> {
                heartbeatThread?.cancel(true)
                logger.error(
                    "Socket timeout due to {}", (cause.cause as SocketTimeoutException).message)
            }
            is IOException -> {
                logger.error("IO error {}", (cause.cause as IOException).message)
            }
            else -> {
                logger.error("Unknown error", cause)
            }
        }
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
            OpCode.HEARTBEAT_ACK -> {
                heartbeatsMissed = 0
                logger.debug("Heartbeat acknowledged")
            }
            else -> {
                logger.error("Unknown opcode: $opCode")
            }
        }
    }

    private fun sendHeartbeat(heartbeatInterval: Int) {
        try {
            if (webSocket != null) {
                val rawSocket: Socket = webSocket!!.socket
                if (rawSocket != null)
                    rawSocket.soTimeout = heartbeatInterval + 10000 // setup a timeout when we miss
            } else {
                logger.error("WebSocket is null")
            }
            // heartbeats
        } catch (ex: SocketException) {
            logger.warn("Failed to setup timeout for socket", ex)
        }

        heartbeatThread =
            scheduler.scheduleAtFixedRate(
                {
                    if (connected) {
                        sendHeartbeat()
                    } else {
                        logger.info("Not sending heartbeat because not connected")
                    }
                },
                0,
                heartbeatInterval.toLong(),
                TimeUnit.MILLISECONDS)
    }

    private fun sendHeartbeat() {
        if (webSocket == null) {
            throw IllegalStateException("WebSocket is not connected")
        }

        val s: Int? = if (seq != null) seq else null

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", OpCode.HEARTBEAT.code).put("d", s)

        if (heartbeatsMissed >= 2) {
            heartbeatsMissed = 0
            logger.warn("Heartbeat missed, will attempt to reconnect")
            webSocket?.disconnect(CloseCode.RECONNECT.code, "ZOMBIE CONNECTION")
        } else {
            heartbeatsMissed += 1
            webSocket?.sendText(heartbeat.toString())
            heartbeatStartTime = System.currentTimeMillis()
        }
    }

    private fun onEventType(eventType: String, d: JsonNode) {}
}
