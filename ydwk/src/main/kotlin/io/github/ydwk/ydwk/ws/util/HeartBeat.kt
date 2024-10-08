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
package io.github.ydwk.ydwk.ws.util

import com.fasterxml.jackson.databind.JsonNode
import com.neovisionaries.ws.client.WebSocket
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.ws.voice.util.VoiceCloseCode
import io.github.ydwk.ydwk.ws.voice.util.VoiceOpcode
import java.net.Socket
import java.net.SocketException
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HeartBeat(
    private val ydwk: YDWK,
    private val webSocket: WebSocket,
    var heartbeatJob: Job? = null,
) {
    private var heartbeatsMissed: Int = 0
    private var heartbeatStartTime: Long = 0
    private val wsScheduler: ExecutorCoroutineDispatcher = ydwk.defaultExecutorCoroutineDispatcher
    private val voiceWsScheduler: ExecutorCoroutineDispatcher =
        ydwk.defaultExecutorCoroutineDispatcher
    private val logger: Logger = LoggerFactory.getLogger(HeartBeat::class.java)

    fun startGateWayHeartbeat(heartbeatInterval: Long, connected: Boolean, seq: Int?) {
        tryWebSocket(heartbeatInterval)

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", OpCode.HEARTBEAT.code).put("d", seq)

        heartbeatJob =
            heartbeatThread(
                heartbeatInterval,
                connected,
                heartbeat,
                CloseCode.MISSED_HEARTBEAT.getCode(),
                CloseCode.MISSED_HEARTBEAT.getReason())
    }

    fun startVoiceHeartbeat(
        heartbeatInterval: Long,
        connected: Boolean,
        voiceConnection: VoiceConnectionImpl,
    ) {
        tryWebSocket(heartbeatInterval)

        heartbeatJob =
            voiceHeartbeatThread(
                heartbeatInterval,
                connected,
                VoiceCloseCode.MISSED_HEARTBEAT.code,
                VoiceCloseCode.MISSED_HEARTBEAT.reason,
                voiceConnection)
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
    ): Job {
        return CoroutineScope(wsScheduler).launch {
            while (isActive) {
                delay(heartbeatInterval)
                if (connected) {
                    sendHeartBeat(heartbeat, closeCode, closeReason)
                } else {
                    logger.info("Not sending heartbeat because not connected")
                }
            }
        }
    }

    private fun voiceHeartbeatThread(
        heartbeatInterval: Long,
        connected: Boolean,
        closeCode: Int,
        closeReason: String,
        voiceConnection: VoiceConnectionImpl,
    ): Job {
        return CoroutineScope(voiceWsScheduler).launch {
            while (isActive) {
                delay(heartbeatInterval)
                if (connected) {
                    handleVoiceConnection(closeCode, closeReason, voiceConnection)
                } else {
                    logger.info("Not sending heartbeat because not connected")
                }
            }
        }
    }

    private fun handleVoiceConnection(
        closeCode: Int,
        closeReason: String,
        voiceConnection: VoiceConnectionImpl,
    ) {

        if (webSocket.isOpen) {
            val keepAlive =
                ydwk.objectMapper
                    .createObjectNode()
                    .put("op", VoiceOpcode.HEARTBEAT.code)
                    .put("d", System.currentTimeMillis())
            sendHeartBeat(keepAlive, closeCode, closeReason)
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
        heartbeatJob?.cancel(CancellationException("Voice heartbeat stopped"))
        heartbeatJob = null
    }

    fun receivedHeartbeatAck() {
        heartbeatsMissed = 0
        val heartbeatTime = System.currentTimeMillis() - heartbeatStartTime
        logger.debug("Heartbeat acknowledged, took {} ms", heartbeatTime)
    }
}
