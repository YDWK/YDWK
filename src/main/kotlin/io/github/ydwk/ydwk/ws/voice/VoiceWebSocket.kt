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

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketListener
import io.github.ydwk.ydwk.YDWKInfo
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.ws.logging.WebsocketLogging
import java.io.IOException
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

/** Handles Voice connections. */
class VoiceWebSocket(private val ydwk: YDWKImpl) : WebSocketAdapter(), WebSocketListener {
    private val logger = LoggerFactory.getLogger(VoiceWebSocket::class.java)
    private var webSocket: WebSocket? = null
    private val webSocketManager =
        ydwk.webSocketManager ?: throw IllegalStateException("WebSocketManager is null!")
    private var timesTriedToConnect = 0

    init {
        connect()
    }

    @Synchronized
    fun connect(): VoiceWebSocket {
        // u can get the endpoint from the Gateway op-0 "VOICE_SERVER_UPDATE" (not voice gateway)
        // in the payload json, u can find the "endpoint" field

        val url =
            "wss://" +
                (ydwk.voiceEndpoint?.replace(":80", "")
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
}
