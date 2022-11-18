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
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.ws.voice.util.VoiceCloseCode
import io.github.ydwk.ydwk.ws.voice.util.VoiceOpcode
import java.net.DatagramPacket
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HeartBeat(
    private val ydwk: YDWK,
    private val webSocket: WebSocket,
    var heartbeatsMissed: Int,
    private var heartbeatStartTime: Long,
    var heartbeatThread: Future<*>? = null,
) {
    private val scheduler: ScheduledExecutorService = ydwk.defaultScheduledExecutorService
    private val logger: Logger = LoggerFactory.getLogger(HeartBeat::class.java)

    fun startGateWayHeartbeat(heartbeatInterval: Long, connected: Boolean, seq: Int?) {
        tryWebSocket(heartbeatInterval)

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", OpCode.HEARTBEAT.code).put("d", seq)

        heartbeatThread =
            heartbeatThread(
                heartbeatInterval,
                connected,
                heartbeat,
                CloseCode.MISSED_HEARTBEAT.code,
                CloseCode.MISSED_HEARTBEAT.reason,
                null,
                null,
                null)
    }

    fun startVoiceHeartbeat(
        heartbeatInterval: Long,
        connected: Boolean,
        voiceConnection: VoiceConnectionImpl,
        udpHeartbeat: ByteArray,
        address: InetSocketAddress
    ) {
        tryWebSocket(heartbeatInterval)

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", VoiceOpcode.HEARTBEAT.code)

        heartbeatThread =
            heartbeatThread(
                heartbeatInterval,
                connected,
                heartbeat,
                VoiceCloseCode.MISSED_HEARTBEAT.code,
                VoiceCloseCode.MISSED_HEARTBEAT.reason,
                voiceConnection,
                udpHeartbeat,
                address)
    }

    private fun tryWebSocket(heartbeatInterval: Long) {
        try {
            val rawSocket: Socket = webSocket.socket
            rawSocket.soTimeout =
                (heartbeatInterval + 10000).toInt() // setup a timeout when we miss
            // heartbeats
        } catch (ex: SocketException) {
            logger.warn("Failed to setup timeout for socket", ex)
        }
    }

    private fun heartbeatThread(
        heartbeatInterval: Long,
        connected: Boolean,
        heartbeat: JsonNode,
        closeCode: Int,
        closeReason: String,
        voiceConnection: VoiceConnectionImpl?,
        udpHeartbeat: ByteArray?,
        address: InetSocketAddress?
    ): ScheduledFuture<*> {
        return scheduler.scheduleAtFixedRate(
            {
                if (connected) {
                    sendHeartBeat(heartbeat, closeCode, closeReason)
                    if (voiceConnection != null && udpHeartbeat != null && address != null) {
                        sendUdpHeartBeat(voiceConnection, udpHeartbeat, address)
                    }
                } else {
                    logger.info("Not sending heartbeat because not connected")
                }
            },
            0,
            heartbeatInterval,
            TimeUnit.MILLISECONDS)
    }

    private fun sendUdpHeartBeat(
        voiceConnection: VoiceConnectionImpl,
        udpHeartbeat: ByteArray,
        address: InetSocketAddress
    ) {
        try {
            val heartbeatPacket = DatagramPacket(udpHeartbeat, udpHeartbeat.size, address)
            voiceConnection.udpsocket?.send(heartbeatPacket)
        } catch (ex: Exception) {
            logger.warn("Failed to send UDP heartbeat", ex)
        }
    }

    private fun sendHeartBeat(json: JsonNode, closeCode: Int, closeReason: String) {

        if (heartbeatsMissed >= 2) {
            heartbeatsMissed = 0
            logger.warn("Heartbeat missed, will attempt to reconnect")
            webSocket.sendClose(closeCode, closeReason)
        } else {
            heartbeatsMissed += 1
            webSocket.sendText(json.toString())
            heartbeatStartTime = System.currentTimeMillis()
        }
    }

    fun stopVoiceHeartbeat() {
        heartbeatThread?.cancel(true)
    }
}
