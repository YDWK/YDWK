/*
 * Copyright 2024 YDWK inc.
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
import com.fasterxml.jackson.databind.node.ObjectNode
import com.neovisionaries.ws.client.*
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.ydwk.YDWKInfo
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.voice.leaveVC
import io.github.ydwk.ydwk.ws.logging.WebsocketLogging
import io.github.ydwk.ydwk.ws.util.HeartBeat
import io.github.ydwk.ydwk.ws.voice.payload.VoiceReadyPayload
import io.github.ydwk.ydwk.ws.voice.payload.VoiceServerUpdatePayload
import io.github.ydwk.ydwk.ws.voice.util.UpdHandler
import io.github.ydwk.ydwk.ws.voice.util.VoiceCloseCode
import io.github.ydwk.ydwk.ws.voice.util.VoiceEncryption
import io.github.ydwk.ydwk.ws.voice.util.VoiceOpcode
import java.io.IOException
import java.net.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException
import org.slf4j.LoggerFactory

/**
 * VoiceWebSocket is a class which is related to how voice data (like in a voice chat) is sent over
 * the internet.
 *
 * When chatting over the internet, voice is converted into data that can be sent over the network.
 *
 * This data needs to be transmitted quickly to ensure the voice chat feels real-time. It won't
 * matter too much if a bit of the data is lost, as the human brain can fill in the gaps.
 *
 * UDP (User Datagram Protocol) is a protocol of sending this data that is faster but potentially
 * less reliable in terms of ensuring every bit of data gets to where it needs to go. However, in
 * the case of voice chat, speed is more important than getting 100% of the data through.
 *
 * So, this class is essentially related to the connection established using the UDP method to send
 * voice data over the network.
 *
 * This class handles the voice connection and the receiving of the op codes and handling of them.
 */
class VoiceWebSocket(
    private val voiceConnection: VoiceConnectionImpl,
    private val etfInsteadOfJson: Boolean,
) : WebSocketAdapter(), WebSocketListener {

    internal val logger = LoggerFactory.getLogger(VoiceWebSocket::class.java)

    private var timesTriedToConnect = 0
    private var wssUrl: String? = null
    private var webSocket: WebSocket? = null
    private var reconnecting = false
    private var ready = false
    private var heartBeat: HeartBeat? = null
    @get:Synchronized @set:Synchronized var connected = false
    private var updHandler: UpdHandler? = null

    internal val ydwk = (voiceConnection.ydwk as YDWKImpl)
    private val webSocketManager =
        ydwk.webSocketManager ?: throw IllegalStateException("WebSocketManager is null!")

    private var voiceServerUpdatePayload: VoiceServerUpdatePayload =
        voiceConnection.getVoiceServerUpdatePayload()
            ?: throw IllegalStateException("Voice server payload is null!")
    private var voiceState: VoiceState =
        voiceConnection.getVoiceState() ?: throw IllegalStateException("Voice state is null!")

    // payloads
    private var voiceReadyPayload: VoiceReadyPayload? = null

    // First, we need to send opcode 4 to websocket to establish a voice connection.
    // If the request is successful, the gateway will respond with two events—a Voice State Update
    // event and a Voice Server Update event.
    // Need to wait for both events.
    // The first will contain a new key, session_id, and the second will provide voice server
    // information we can use to establish a new voice connection.
    // See ExampleVoiceServerUpdatePayload.json example in test resources jsons folder

    /**
     * Establishes a connection to the voice gateway.
     *
     * @return the reference to the VoiceWebSocket object.
     * @throws IllegalStateException if the voice server payload is null, the voice state is null,
     *   the voice endpoint is null, the session ID is blank or null, or the token is blank or null.
     * @throws IOException if an error occurs while connecting to the voice gateway.
     */
    @Synchronized
    fun connect(): VoiceWebSocket {
        voiceServerUpdatePayload =
            voiceConnection.getVoiceServerUpdatePayload()
                ?: throw IllegalStateException("Voice server payload is null!")
        voiceState =
            voiceConnection.getVoiceState() ?: throw IllegalStateException("Voice state is null!")

        val url: String =
            (voiceServerUpdatePayload.endpoint?.replace(":80", "")
                ?: throw IllegalStateException("Voice endpoint is null!")) +
                YDWKInfo.VOICE_GATEWAY_VERSION.getUrl()

        wssUrl =
            if (url.startsWith("wss://")) {
                url
            } else {
                "wss://$url"
            }

        if (voiceState.sessionId.isBlank() || voiceState.sessionId.isEmpty()) {
            throw IllegalStateException("SessionId is null!")
        }

        if (voiceServerUpdatePayload.token.isBlank() || voiceServerUpdatePayload.token.isEmpty()) {
            throw IllegalStateException("Token is null!")
        }

        try {
            val webSocketFactory = WebSocketFactory()
            if (webSocketFactory.socketTimeout > 0)
                webSocketFactory.socketTimeout = 1000.coerceAtLeast(webSocketFactory.socketTimeout)
            else webSocketFactory.socketTimeout = 10000

            webSocket =
                webSocketFactory
                    .createSocket(wssUrl)
                    .addHeader("Accept-Encoding", "gzip")
                    .setDirectTextMessage(etfInsteadOfJson)
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
        if (reconnecting) {
            logger.info("Resuming voice connection")
            resume()
        } else {
            logger.info("Identifying voice connection")
            identify()
        }
        reconnecting = false
    }

    /**
     * This function is used to send an identify payload to the WebSocket connection. The identify
     * payload includes information about the server, user, session, and token.
     */
    private fun identify() {
        val identifyPayload = ydwk.objectNode

        identifyPayload.put("op", VoiceOpcode.IDENTIFY.code)

        val identifyData = ydwk.objectNode
        identifyData.put("server_id", voiceConnection.voiceChannel.guild.idAsLong)
        identifyData.put("user_id", voiceState.user!!.idAsLong)
        identifyData.put("session_id", voiceState.sessionId)
        identifyData.put("token", voiceServerUpdatePayload.token)

        identifyPayload.set<JsonNode>("d", identifyData)
        webSocket?.sendText(identifyPayload.toString())
    }

    /**
     * Resumes the voice connection by sending a resume payload through the WebSocket. The resume
     * payload includes the necessary information to restore the voice session.
     */
    private fun resume() {
        val resumePayload = ydwk.objectNode
        resumePayload.put("op", VoiceOpcode.RESUME.code)

        val resumeData = ydwk.objectNode
        resumeData.put("server_id", voiceConnection.voiceChannel.guild.idAsLong)
        resumeData.put("session_id", voiceState.sessionId)
        resumeData.put("token", voiceServerUpdatePayload.token)

        resumePayload.set<JsonNode>("d", resumeData)
        webSocket?.sendText(resumePayload.toString())
    }

    override fun onTextMessage(websocket: WebSocket, text: String) {
        handleMessage(text)
    }
    override fun onTextMessage(websocket: WebSocket, data: ByteArray) {
        val dataAsString: String
        try {
            dataAsString = String(data, Charsets.UTF_8)
        } catch (e: Exception) {
            logger.error("Failed to convert data to string", e)
            return
        }

        handleMessage(dataAsString)
    }

    private fun handleMessage(message: String) {
        try {
            val payload = ydwk.objectMapper.readTree(message)
            onOpCode(VoiceOpcode.getOpCode(payload.get("op").asInt()), payload)
        } catch (e: Exception) {
            logger.error("Error while handling message", e)
        }
    }

    private fun onOpCode(opCode: VoiceOpcode, payload: JsonNode) {
        val dataObject = payload.get("d")

        when (opCode) {
            VoiceOpcode.READY -> {
                logger.debug("Received READY")
                val ssrc = dataObject.get("ssrc").asInt()
                val ip = dataObject.get("ip").asText()
                val port = dataObject.get("port").asInt()
                val modes: MutableList<String> = mutableListOf()
                for (jsonMode in dataObject.get("modes")) {
                    modes.add(jsonMode.asText())
                }

                voiceReadyPayload = VoiceReadyPayload(ssrc, ip, port, modes)
                voiceConnection.setVoiceReadyPayload(voiceReadyPayload!!)

                // Open a UDP connection
                updHandler =
                    UpdHandler(voiceConnection, voiceReadyPayload!!, InetSocketAddress(ip, port))
                handleProtocol(modes)
            }
            VoiceOpcode.SESSION_DESCRIPTION -> {
                logger.debug("Received SESSION DESCRIPTION")
                handleSpeaking()

                val secretKeysList: MutableList<Int> = mutableListOf()
                for (jsonKeys in dataObject.get("secret_key")) {
                    secretKeysList.add(jsonKeys.asInt())
                }

                val secretKey =
                    ByteArray(secretKeysList.size) { pos ->
                        if (secretKeysList[pos] > 127) {
                            // If the integer value is greater than 127, modulo by 128 is taken
                            (secretKeysList[pos] % (Byte.MAX_VALUE + 1)).toByte()
                        } else {
                            secretKeysList[pos].toByte()
                        }
                    }

                updHandler!!.secretKey = secretKey
                // updHandler!!.sendVoiceData()
                ready = true

                voiceConnection.connectFuture.complete(voiceConnection)
            }
            VoiceOpcode.HELLO -> {
                logger.debug("Received {} - HELLO", opCode)
                val heartbeatInterval = dataObject.get("heartbeat_interval").asLong()
                heartBeat = HeartBeat(ydwk, webSocket!!)
                heartBeat!!.stopVoiceHeartbeat()
                heartBeat?.startVoiceHeartbeat(heartbeatInterval, connected, voiceConnection)
            }
            VoiceOpcode.HEARTBEAT_ACK -> {
                logger.debug("Received {} - HEARTBEAT_ACK", opCode)
                heartBeat?.receivedHeartbeatAck()
            }
            else -> {
                logger.debug("received unknown opcode" + opCode.code)
            }
        }
    }

    /**
     * To establish a UDP connection for voice data, we start off with the received properties of a
     * UDP voice server. The steps involved are as follows:
     * 1. Open a UDP connection using the IP and port provided in the Ready payload.
     * 2. If necessary, perform an IP Discovery using the established connection.
     * 3. Post discovery of our external IP and UDP port, the information is conveyed to the voice
     *    WebSocket to initiate the process of receiving and sending data. This is done using Opcode
     *    1 Select Protocol.
     *
     * The request payload for Opcode 1 Select Protocol looks like this:
     * ```json
     * {
     *    "op": 1,
     *    "d": {
     *        "protocol": "udp",
     *        "data": {
     *            "address": "127.0.0.1",
     *            "port": 1337,
     *            "mode": "xsalsa20_poly1305_lite"
     *        }
     *    }
     * }
     * ```
     *
     * Here, `"address"` is the discovered external IP and `"port"` is the corresponding UDP port.
     */
    private fun handleProtocol(modes: MutableList<String>) {
        try {
            if (updHandler == null) {
                throw IllegalStateException("Upd handler is null")
            }

            val address = updHandler!!.findIp()
            val selectProtocolJson = ydwk.objectNode
            selectProtocolJson.put("op", VoiceOpcode.SELECT_PROTOCOL.code)

            // Event data object
            val selectProtocolEventDataJson = ydwk.objectNode
            selectProtocolEventDataJson.put("protocol", "udp")

            // Data Object
            val selectProtocolDataJson = ydwk.objectNode
            selectProtocolDataJson.put("address", address.fixedHostString)
            selectProtocolDataJson.put("port", address.port)
            selectProtocolDataJson.put(
                "mode", VoiceEncryption.getPreferred(modes).getPreferredMode())

            selectProtocolEventDataJson.set<ObjectNode>("data", selectProtocolDataJson)
            selectProtocolJson.set<ObjectNode>("d", selectProtocolEventDataJson)
            webSocket!!.sendText(selectProtocolJson.toString())
        } catch (e: RuntimeException) {
            throw RuntimeException(e)
        }
    }

    private val InetSocketAddress.fixedHostString: String
        get() {
            val originalHostString = this.hostString
            return originalHostString.replace("\u0000", "")
        }

    private fun handleSpeaking() {
        val speakingPayload = ydwk.objectNode
        speakingPayload.put("op", VoiceOpcode.SPEAKING.code)
        val speakingData = ydwk.objectNode
        val speakFlags: Long = 0
        for (speakFlag in voiceConnection.getSpeakingFlags()) {
            speakFlags.or(speakFlag.getValue())
        }

        speakingData.put("speaking", speakFlags)
        speakingData.put("delay", 0)
        speakingData.put("ssrc", voiceReadyPayload!!.ssrc)
        speakingPayload.set<JsonNode>("d", speakingData)
        webSocket?.sendText(speakingPayload.toString())
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
            else "Unknown voice close code"

        logger.info(
            "Disconnected from voice websocket with close code $closeCodeAsString and reason $closeCodeReason")

        heartBeat?.heartbeatJob?.cancel(CancellationException("Voice heartbeat stopped"))

        val closeCode = VoiceCloseCode.fromCode(closeFrame?.closeCode ?: 1000)

        if (closeCode.isReconnect) {
            logger.info("Reconnecting to websocket")
            connect()
        } else {
            logger.info("Not able to reconnect to voice websocket, ending voice connection")
            invalidate()
            websocket.disconnect()
        }
    }

    fun triggerDisconnect() {
        logger.info("Closing voice connection")
        webSocket!!.sendClose(VoiceCloseCode.DISCONNECTED.code)
    }

    private fun invalidate() {
        if (voiceConnection.guild.botAsMember.voiceState != null) {
            voiceConnection.guild.botAsMember.leaveVC()
        }

        heartBeat?.heartbeatJob?.cancel(CancellationException("Voice heartbeat stopped"))
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
}
