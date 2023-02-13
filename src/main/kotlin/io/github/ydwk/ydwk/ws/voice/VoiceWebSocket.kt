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
import io.github.ydwk.ydwk.YDWKInfo
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.ws.logging.WebsocketLogging
import io.github.ydwk.ydwk.ws.util.HeartBeat
import io.github.ydwk.ydwk.ws.voice.util.VoiceCloseCode
import io.github.ydwk.ydwk.ws.voice.util.VoiceEncryption
import io.github.ydwk.ydwk.ws.voice.util.VoiceEncryption.Companion.getPreferred
import io.github.ydwk.ydwk.ws.voice.util.VoiceOpcode
import java.io.IOException
import java.net.*
import java.nio.ByteBuffer
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

/** Handles Voice connections. */
class VoiceWebSocket(private val voiceConnection: VoiceConnectionImpl) :
    WebSocketAdapter(), WebSocketListener {
    private val logger = LoggerFactory.getLogger(VoiceWebSocket::class.java)
    private var webSocket: WebSocket? = null
    private val ydwk = (voiceConnection.ydwk as YDWKImpl)
    private val webSocketManager =
        ydwk.webSocketManager ?: throw IllegalStateException("WebSocketManager is null!")
    private var timesTriedToConnect = 0
    @get:Synchronized @set:Synchronized var connected = false
    private var heartBeat: HeartBeat? = null
    var secretKey: ByteArray? = null
    var ssrc: Int? = null
    private var encryptionMode: VoiceEncryption? = null
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

    private fun sendCloseCode(code: VoiceCloseCode) {
        checkNotNull(webSocket) { "WebSocket is null" }
        webSocket!!.sendClose(code.code, code.reason)
    }

    private fun resume() {
        val resumePayload = ydwk.objectNode
        resumePayload.put("op", VoiceOpcode.RESUME.code)
        val resumeData = ydwk.objectNode
        resumeData.put("server_id", voiceConnection.channel.guild.idAsLong)
        resumeData.put("session_id", voiceConnection.sessionId)
        resumeData.put("token", voiceConnection.token)
        resumePayload.set<JsonNode>("d", resumeData)
        webSocket?.sendText(resumePayload.toString())
    }

    private fun identify() {
        val identifyPayload = ydwk.objectNode
        identifyPayload.put("op", VoiceOpcode.IDENTIFY.code)
        val identifyData = ydwk.objectNode
        identifyData.put("server_id", voiceConnection.channel.guild.idAsLong)
        identifyData.put("user_id", voiceConnection.userId)
        identifyData.put("session_id", voiceConnection.sessionId)
        identifyData.put("token", voiceConnection.token)
        identifyPayload.set<JsonNode>("d", identifyData)
        webSocket?.sendText(identifyPayload.toString())
    }

    private fun onSelectProtocol(ourInetSocketAddress: InetSocketAddress) {
        val selectProtocolPayload = ydwk.objectNode
        selectProtocolPayload.put("op", VoiceOpcode.SELECT_PROTOCOL.code)

        val selectProtocolData = ydwk.objectNode
        selectProtocolData.put("protocol", "udp")

        val dataPayload = ydwk.objectNode
        dataPayload.put("address", ourInetSocketAddress.hostString)
        dataPayload.put("port", ourInetSocketAddress.port)
        dataPayload.put("mode", encryptionMode!!.getPreferredMode())

        selectProtocolPayload.set<JsonNode>("d", selectProtocolData)
        selectProtocolData.set<JsonNode>("data", dataPayload)

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

    fun close() {
        logger.info("Closing voice connection")
        sendCloseCode(VoiceCloseCode.DISCONNECTED)
        voiceConnection.stopSendingAudio()
        // in one minute stop heartbeat
        stopSendingEncodedData()
        ScheduledThreadPoolExecutor(1)
            .schedule({ heartBeat?.heartbeatThread?.cancel(false) }, 1, TimeUnit.MINUTES)
    }

    // UDP is a communication protocol that transmits independent packets over the network with no
    // guarantee of arrival and no guarantee of the order of delivery.
    private fun handleUDP(address: InetSocketAddress, ssrc: Int): InetSocketAddress? {
        try {
            // check if udp is already connected and if so close it
            if (voiceConnection.udpsocket != null) {
                voiceConnection.udpsocket!!.close()
            }

            // create a new udp socket
            voiceConnection.udpsocket = DatagramSocket()

            // create a byte array of length 70 containing the ssrc
            val ssrcBytes = ByteBuffer.allocate(70)
            ssrcBytes.putShort(1.toShort())
            ssrcBytes.putShort(70.toShort())
            ssrcBytes.putInt(ssrc)

            // create a new datagram packet with the ssrc bytes and the address
            val packet = DatagramPacket(ssrcBytes.array(), ssrcBytes.array().size, address)
            voiceConnection.udpsocket!!.send(packet)

            // Discord returns a packet with the port and ip of the udp socket
            val receivedPacket = DatagramPacket(ByteArray(70), 70)
            voiceConnection.udpsocket!!.soTimeout = 1000
            voiceConnection.udpsocket!!.receive(receivedPacket)

            // get the received bytes and convert them to a string
            val receivedBytes: ByteArray = receivedPacket.data

            // get the ip from the received bytes
            var ip = String(receivedBytes, 4, receivedBytes.size - 6)
            ip = ip.trim()

            // get the port from the received bytes and end it with 0xFFFF to get the correct port
            val port = getPort(receivedBytes, receivedBytes.size - 2).toInt() or 0xFFFF
            voiceConnection.address = address
            return InetSocketAddress(ip, port)
        } catch (e: Exception) {
            logger.error("Error while sending encoded data", e)
            return null
        }
    }

    private fun getPort(array: ByteArray, offset: Int): Short {
        return (array[offset].toInt() and 0xff shl 8 or array[offset + 1].toInt() and 0xff)
            .toShort()
    }

    private fun stopSendingEncodedData() {
        try {
            if (voiceConnection.udpsocket != null) {
                voiceConnection.udpsocket!!.close()
            }
        } catch (e: Exception) {
            logger.error("Error while stopping sending encoded data", e)
        }
    }
}

private operator fun Char.times(i: Int): Int {
    return this.code * i
}
