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
package io.github.ydwk.ydwk.ws.voice

import com.fasterxml.jackson.databind.JsonNode
import com.neovisionaries.ws.client.*
import io.github.ydwk.ydwk.YDWKInfo
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.ws.logging.WebsocketLogging
import io.github.ydwk.ydwk.ws.util.HeartBeat
import io.github.ydwk.ydwk.ws.voice.util.VoiceOpcode
import io.github.ydwk.ydwk.ws.voice.util.findIp
import java.io.IOException
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

/** Handles Voice connections. */
class VoiceWebSocket(private val voiceConnection: VoiceConnectionImpl) :
    WebSocketAdapter(), WebSocketListener {
    private val logger = LoggerFactory.getLogger(VoiceWebSocket::class.java)
    private var webSocket: WebSocket? = null
    private val ydwk = voiceConnection.ydwk
    private val webSocketManager =
        ydwk.webSocketManager ?: throw IllegalStateException("WebSocketManager is null!")
    private var timesTriedToConnect = 0
    @get:Synchronized @set:Synchronized var connected = false
    private var heartbeatsMissed: Int = 0
    private var heartbeatStartTime: Long = 0
    private var heartBeat: HeartBeat =
        HeartBeat(ydwk, webSocket!!, heartbeatsMissed, heartbeatStartTime)
    private var ip: String? = null
    private var port: Int? = null
    private var ssrc: Int? = null
    private var modes: List<String>? = null
    private var secretKey: ByteArray? = null

    init {
        connect()
    }

    @Synchronized
    fun connect(): VoiceWebSocket {
        // u can get the endpoint from the Gateway op-0 "VOICE_SERVER_UPDATE" (not voice gateway)
        // in the payload json, u can find the "endpoint" field

        val url =
            "wss://" +
                (voiceConnection.voiceEndpoint?.replace(":80", "")
                    ?: throw IllegalStateException("Voice endpoint is null!")) +
                YDWKInfo.VOICE_GATEWAY_VERSION.url

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
                    .addListener(WebsocketLogging(logger))
                    .connect()
        } catch (e: IOException) {
            logger.error("Failed to connect to voice gateway, will try again in 10 seconds", e)
            if (timesTriedToConnect > 3) {
                timesTriedToConnect = 0
                logger.error("Failed to connect to gateway 3 times, please try again later")
            } else {
                webSocketManager.scheduler.schedule(
                    {
                        timesTriedToConnect++
                        connect()
                    },
                    10,
                    TimeUnit.SECONDS)
            }
        }
        return this
    }

    @Throws(Exception::class)
    override fun onConnected(websocket: WebSocket, headers: Map<String, List<String>>) {
        connected = true
        if (voiceConnection.sessionId != null) {
            resume()
        } else {
            identify()
        }
    }

    override fun onTextMessage(websocket: WebSocket, text: String) {
        handleMessage(text)
    }

    @Throws(Exception::class)
    override fun onDisconnected(
        websocket: WebSocket,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean,
    ) {
        connected = false
    }

    private fun handleMessage(message: String) {
        try {
            val payload = ydwk.objectMapper.readTree(message)
            // logger.info("Received payload: ${payload.toPrettyString()}")
            onOpCode(payload)
        } catch (e: Exception) {
            logger.error("Error while handling message", e)
        }
    }

    private fun onOpCode(payload: JsonNode) {
        val data = payload.get("d")
        val opCode = payload.get("op").asInt()
        when (VoiceOpcode.from(opCode)) {
            VoiceOpcode.HELLO -> {
                logger.debug("Received $opCode - HELLO")
                val heartbeatInterval = data.get("heartbeat_interval").asLong()
                heartBeat.startVoiceHeartbeat(heartbeatInterval, connected)
                heartbeatsMissed = heartBeat.heartbeatsMissed
            }
            VoiceOpcode.READY -> {
                logger.debug("Received $opCode - READY")
                this.ssrc = data.get("ssrc").asInt()
                this.port = data.get("port").asInt()
                this.ip = data.get("ip").asText()
                this.modes = data.get("modes").toList().map { it.asText() }
                onSelectProtocol()
                Thread.sleep(1000)
            }
            VoiceOpcode.SESSION_DESCRIPTION -> {
                logger.debug("Received $opCode - Session Description")
                this.secretKey = data.get("secret_key").binaryValue()
                sendSpeaking()
            }
            VoiceOpcode.RESUMED -> {
                logger.debug("Received $opCode - RESUMED")
                logger.info("Successfully resumed voice connection")
            }
            VoiceOpcode.CLIENT_DISCONNECT -> {
                logger.debug("Received $opCode - Client disconnected")
            }
            VoiceOpcode.HEARTBEAT_ACK -> {
                logger.debug("Received $opCode - HEARTBEAT_ACK")
                heartbeatsMissed = 0
            }
            VoiceOpcode.RESUME -> {}
            else -> {
                // do nothing
            }
        }
    }

    private fun resume() {
        val resumePayload = ydwk.objectNode
        resumePayload.put("op", VoiceOpcode.RESUME.code)
        val resumeData = ydwk.objectNode
        resumeData.put("server_id", voiceConnection.guildId)
        resumeData.put("session_id", voiceConnection.sessionId)
        resumeData.put("token", voiceConnection.token)
        resumePayload.set<JsonNode>("d", resumeData)
        webSocket?.sendText(resumePayload.toString())
    }

    private fun identify() {
        val identifyPayload = ydwk.objectNode
        identifyPayload.put("op", VoiceOpcode.IDENTIFY.code)
        val identifyData = ydwk.objectNode
        identifyData.put("server_id", voiceConnection.guildId)
        identifyData.put("user_id", voiceConnection.userId)
        identifyData.put("session_id", voiceConnection.sessionId)
        identifyData.put("token", voiceConnection.token)
        identifyPayload.set<JsonNode>("d", identifyData)
        webSocket?.sendText(identifyPayload.toString())
    }

    private fun onSelectProtocol() {
        val address = findIp(ip, port, ssrc)
        val selectProtocolPayload = ydwk.objectNode
        selectProtocolPayload.put("op", VoiceOpcode.SELECT_PROTOCOL.code)
        val selectProtocolData = ydwk.objectNode
        selectProtocolData.put("protocol", "udp")
        selectProtocolData.put("data", address.hostString)
        selectProtocolData.put("port", address.port)
        selectProtocolData.put("mode", "xsalsa20_poly1305")
        selectProtocolPayload.set<JsonNode>("d", selectProtocolData)
        webSocket?.sendText(selectProtocolPayload.toString())
    }

    private fun sendSpeaking() {
        val speakingPayload = ydwk.objectNode
        speakingPayload.put("op", VoiceOpcode.SPEAKING.code)
        val speakingData = ydwk.objectNode

        val speakFlags: Long = 0
        for (speakFlag in voiceConnection.speakingFlags) {
            speakFlags.or(speakFlag.getValue())
        }

        speakingData.put("speaking", speakFlags)
        speakingData.put("delay", 0)
        speakingData.put("ssrc", ssrc)
        speakingPayload.set<JsonNode>("d", speakingData)
        webSocket?.sendText(speakingPayload.toString())
    }
}
