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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.ws.voice

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.neovisionaries.ws.client.*
import io.github.ydwk.yde.impl.YDWKImpl
import io.github.ydwk.ydwk.YDWKInfo
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.ws.logging.WebsocketLogging
import io.github.ydwk.ydwk.ws.util.HeartBeat
import io.github.ydwk.ydwk.ws.voice.util.*
import io.github.ydwk.ydwk.ws.voice.util.VoiceEncryption.Companion.getPreferred
import io.github.ydwk.ydwk.ws.voice.util.handleUDP
import java.io.IOException
import java.net.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

/** Handles Voice connections. */
class VoiceWebSocket(internal val voiceConnection: VoiceConnectionImpl) :
    WebSocketAdapter(), WebSocketListener {
    internal val logger = LoggerFactory.getLogger(VoiceWebSocket::class.java)
    internal var webSocket: WebSocket? = null
    internal val ydwk = (voiceConnection.ydwk as YDWKImpl)
    private val webSocketManager =
        ydwk.webSocketManager ?: throw IllegalStateException("WebSocketManager is null!")
    private var timesTriedToConnect = 0
    @get:Synchronized @set:Synchronized var connected = false
    private var heartBeat: HeartBeat? = null
    var secretKey: ByteArray? = null
    var ssrc: Int? = null
    internal var encryptionMode: VoiceEncryption? = null
    private var wssUrl: String? = null
    private var reconnecting = false
    private var ready = false

    init {
        val url =
            (voiceConnection.voiceEndpoint?.replace(":80", "")
                ?: throw IllegalStateException("Voice endpoint is null!")) +
                YDWKInfo.VOICE_GATEWAY_VERSION.getUrl()

        wssUrl =
            if (url.startsWith("wss://")) {
                url
            } else {
                "wss://$url"
            }

        if (voiceConnection.sessionId.isNullOrBlank() ||
            voiceConnection.sessionId.isNullOrEmpty()) {
            throw IllegalStateException("SessionId is null!")
        }

        if (voiceConnection.token.isNullOrBlank() || voiceConnection.token.isNullOrEmpty()) {
            throw IllegalStateException("Token is null!")
        }

        connect()
    }

    @Synchronized
    private fun connect(): VoiceWebSocket {
        try {

            if (!reconnecting && webSocket != null) {
                logger.info("This voice websocket has already attempted to connect")
            }

            val webSocketFactory = WebSocketFactory()
            if (webSocketFactory.socketTimeout > 0)
                webSocketFactory.socketTimeout = 1000.coerceAtLeast(webSocketFactory.socketTimeout)
            else webSocketFactory.socketTimeout = 10000

            webSocket =
                webSocketFactory
                    .createSocket(wssUrl ?: throw IllegalStateException("wssUrl is null!"))
                    .addHeader("Accept-Encoding", "gzip")
                    .addListener(this)
                    .addListener(WebsocketLogging(logger))
                    .connectAsynchronously()
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
        if (reconnecting) {
            logger.info("Resuming voice connection")
            resume()
        } else {
            logger.info("Identifying voice connection")
            identify()
        }
        reconnecting = false
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

        val closeFrame: WebSocketFrame? = if (closedByServer) serverCloseFrame else clientCloseFrame

        val closeCodeReason: String =
            if (closeFrame != null) closeFrame.closeReason else "Unknown reason"

        val closeCodeAsString: String =
            if (closeFrame != null)
                VoiceCloseCode.fromCode(closeFrame.closeCode).name +
                    " (" +
                    closeFrame.closeCode +
                    ")"
            else "Unknown code"

        logger.info(
            "Disconnected from websocket with close code $closeCodeAsString and reason $closeCodeReason")

        heartBeat?.heartbeatThread?.cancel(false)

        val closeCode = VoiceCloseCode.fromCode(closeFrame?.closeCode ?: 1000)

        if (closeCode.isReconnect) {
            logger.info("Reconnecting to voice gateway")
            reconnecting = true
            connect()
        } else {
            logger.info("Not reconnecting to voice gateway")
        }
    }

    override fun onError(websocket: WebSocket, cause: WebSocketException) {
        when (cause.cause) {
            is SocketTimeoutException -> {
                logger.error(
                    "Voice Socket timeout due to {}",
                    (cause.cause as SocketTimeoutException).message)
            }
            is IOException -> {
                logger.error("Voice IO error {}", (cause.cause as IOException).message)
            }
            else -> {
                logger.error("Voice Unknown error", cause)
            }
        }
    }

    private fun handleMessage(message: String) {
        try {
            val payload = ydwk.objectMapper.readTree(message)
            logger.debug("Received voice payload: ${payload.toString()}")
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
                heartBeat = HeartBeat(ydwk, webSocket!!)
                heartBeat!!.stopVoiceHeartbeat()
                heartBeat?.startVoiceHeartbeat(heartbeatInterval, connected, voiceConnection)
            }
            VoiceOpcode.READY -> {
                logger.debug("Received $opCode - READY")
                ssrc = data.get("ssrc").asInt()
                val port = data.get("port").asInt()
                val ip = data.get("ip").asText()
                // "modes": ["xsalsa20_poly1305", "xsalsa20_poly1305_suffix",
                // "xsalsa20_poly1305_lite"],
                val modes = data.get("modes").map { it.asText() }
                encryptionMode = getPreferred(modes)

                var ourInetSocketAddress: InetSocketAddress?
                do {
                    ourInetSocketAddress = handleUDP(InetSocketAddress(ip, port), ssrc!!)
                } while (ourInetSocketAddress == null)

                onSelectProtocol(ourInetSocketAddress)
            }
            VoiceOpcode.SESSION_DESCRIPTION -> {
                logger.debug("Received $opCode - Session Description")
                sendSpeaking()

                val keyArray: ArrayNode
                if (data.get("secret_key").isArray) {
                    keyArray = data.get("secret_key") as ArrayNode
                } else {
                    keyArray = ydwk.objectMapper.createArrayNode()
                    keyArray.add(data.get("secret_key").asText())
                }
                val secretKey = ByteArray(32)
                this.secretKey = secretKey
                for (i in 0 until keyArray.size()) secretKey[i] = keyArray.get(i).asInt().toByte()
                ready = true

                voiceConnection.sendSendingAudio()
            }
            VoiceOpcode.RESUMED -> {
                logger.debug("Received $opCode - RESUMED")
                logger.info("Successfully resumed voice connection")
                ready = true
            }
            VoiceOpcode.HEARTBEAT_ACK -> {
                logger.debug("Received $opCode - HEARTBEAT_ACK")
                heartBeat?.receivedHeartbeatAck()
            }
            else -> {
                // do nothing
            }
        }
    }

    fun close() {
        logger.info("Closing voice connection")
        sendCloseCode(VoiceCloseCode.DISCONNECTED)
        voiceConnection.stopSendingAudio()
        // in one minute stop heartbeat
        stopSendingEncodedData()
        ScheduledThreadPoolExecutor(1)
            .schedule({ heartBeat?.heartbeatThread?.cancel(false) }, 1, TimeUnit.MINUTES)
    }
}

private operator fun Char.times(i: Int): Int {
    return this.code * i
}
