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
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.neovisionaries.ws.client.*
import io.github.realyusufismail.ydwk.YDWKInfo
import io.github.realyusufismail.ydwk.cache.CacheType
import io.github.realyusufismail.ydwk.event.events.ReadyEvent
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.impl.entities.BotImpl
import io.github.realyusufismail.ydwk.impl.entities.application.PartialApplicationImpl
import io.github.realyusufismail.ydwk.impl.handler.handlers.UserUpdateHandler
import io.github.realyusufismail.ydwk.impl.handler.handlers.guild.GuildCreateHandler
import io.github.realyusufismail.ydwk.impl.handler.handlers.guild.GuildDeleteHandler
import io.github.realyusufismail.ydwk.impl.handler.handlers.guild.GuildUpdateHandler
import io.github.realyusufismail.ydwk.ws.util.CloseCode
import io.github.realyusufismail.ydwk.ws.util.EventNames
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent
import io.github.realyusufismail.ydwk.ws.util.OpCode
import io.github.realyusufismail.ydwk.ws.util.OpCode.*
import io.github.realyusufismail.ydwk.ws.util.impl.LoggedInImpl
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebSocketManager(
    protected var ydwk: YDWKImpl,
    private var token: String,
    private var intents: List<GateWayIntent>,
) : WebSocketAdapter(), WebSocketListener {
    private val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger

    // Tha main websocket
    private var webSocket: WebSocket? = null
    private var resumeUrl: String? = null
    private var sessionId: String? = null
    private var seq: Int? = null
    private var heartbeatsMissed: Int = 0
    private var heartbeatStartTime: Long = 0
    @Volatile protected var heartbeatThread: Future<*>? = null
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    @get:Synchronized @set:Synchronized var connected = false
    private var alreadySentConnectMessageOnce: Boolean = false

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
            logger.error("Failed to connect to gateway, will try again in 10 seconds", e)
            scheduler.schedule({ connect() }, 10, TimeUnit.SECONDS)
        }
        return this
    }

    @Throws(Exception::class)
    override fun onConnected(websocket: WebSocket, headers: Map<String, List<String>>) {
        if (sessionId == null) {
            if (!alreadySentConnectMessageOnce) {
                logger.info(
                    """
                
                   _____                                         _                _     _              __     __  _____   __          __  _  __
                  / ____|                                       | |              | |   | |             \ \   / / |  __ \  \ \        / / | |/ /
                 | |        ___    _ __    _ __     ___    ___  | |_    ___    __| |   | |_    ___      \ \_/ /  | |  | |  \ \  /\  / /  | ' / 
                 | |       / _ \  | '_ \  | '_ \   / _ \  / __| | __|  / _ \  / _` |   | __|  / _ \      \   /   | |  | |   \ \/  \/ /   |  <  
                 | |____  | (_) | | | | | | | | | |  __/ | (__  | |_  |  __/ | (_| |   | |_  | (_) |      | |    | |__| |    \  /\  /    | . \ 
                  \_____|  \___/  |_| |_| |_| |_|  \___|  \___|  \__|  \___|  \__,_|    \__|  \___/       |_|    |_____/      \/  \/     |_|\_\
            """.trimIndent())
                alreadySentConnectMessageOnce = true
            } else {
                logger.info("Reconnected to gateway")
            }
        } else {
            logger.info("Resuming session$sessionId")
        }

        connected = true
        if (sessionId == null) {
            identify()
        } else {
            resume()
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
            // logger.info("Received payload: ${payload.toPrettyString()}")
            onEvent(payload)
        } catch (e: Exception) {
            logger.error("Error while handling message", e)
        }
    }

    @Throws(Exception::class)
    override fun onDisconnected(
        websocket: WebSocket,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean,
    ) {
        connected = false

        val closeCode: CloseCode =
            when {
                serverCloseFrame != null -> {
                    CloseCode.from(serverCloseFrame.closeCode)
                }
                clientCloseFrame != null -> {
                    CloseCode.from(clientCloseFrame.closeCode)
                }
                else -> {
                    CloseCode.UNKNOWN
                }
            }
        logger.info(
            "Disconnected from gateway, code: ${closeCode.code} reason: ${closeCode.reason}")

        if (heartbeatThread != null) {
            heartbeatThread!!.cancel(false)
        }

        if (closeCode.isReconnect() || closeCode == CloseCode.UNKNOWN || !scheduler.isShutdown) {
            try {
                scheduler.schedule({ connect() }, 10, TimeUnit.SECONDS)
            } catch (e: RejectedExecutionException) {
                logger.error("Error while reconnecting", e)
                invalidate()
                ydwk.setLoggedIn(LoggedInImpl(false).setDisconnectedTime())
                TODO("Add shutdown event")
            }
        } else {
            ydwk.setLoggedIn(LoggedInImpl(false).setDisconnectedTime())
            TODO("When creating events, add a shutdown event here")
        }
    }

    override fun onError(websocket: WebSocket, cause: WebSocketException) {
        ydwk.setLoggedIn(LoggedInImpl(false).setDisconnectedTime())
        when (cause.cause) {
            is SocketTimeoutException -> {
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

    private fun identify() {
        // event data
        val d: ObjectNode =
            ydwk.objectNode
                .put("token", token)
                .put("intents", GateWayIntent.calculateBitmask(intents.toList()))
                .set(
                    "properties",
                    ydwk.objectNode
                        .put("os", System.getProperty("os.name"))
                        .put("browser", "YDWK")
                        .put("device", "YDWK"))

        val json: JsonNode = ydwk.objectNode.put("op", IDENTIFY.code).set("d", d)
        webSocket?.sendText(json.toString())
        ydwk.setLoggedIn(LoggedInImpl(false).setLoggedInTime())
    }

    private fun resume() {
        val json = ydwk.objectNode.put("token", token).put("session_id", sessionId).put("seq", seq)

        val identify: ObjectNode = ydwk.objectNode.put("op", RESUME.code).set("d", json)

        webSocket?.sendText(identify.toString())
        ydwk.setLoggedIn(LoggedInImpl(false).setLoggedInTime())
    }

    private fun onEvent(payload: JsonNode) {
        logger.debug("Received event {}", payload.toString())

        val s: Int? = if (payload.hasNonNull("s")) payload.get("s").asInt() else null
        if (s != null) {
            seq = s
        }

        val d: JsonNode = payload.get("d")
        val opCode: Int = payload.get("op").asInt()
        onOpCode(opCode, d, payload)
    }

    private fun onOpCode(opCode: Int, d: JsonNode, rawJson: JsonNode) {
        when (OpCode.fromCode(opCode)) {
            DISPATCH -> {
                val event: String = rawJson.get("t").asText()
                onEventType(event, d)
            }
            HEARTBEAT -> {
                logger.debug("Received $opCode")
                sendHeartbeat()
            }
            RECONNECT -> {
                logger.debug("Received $opCode")
                if (sessionId != null) {
                    resume()
                } else {
                    identify()
                }
            }
            INVALID_SESSION -> {
                logger.debug("Received $opCode")
                if (rawJson.get("d").asBoolean()) {
                    logger.info("Invalid session, reconnecting")
                    identify()
                } else {
                    logger.error("Invalid session")
                    resumeUrl = null
                    sessionId = null
                }
            }
            HELLO -> {
                logger.debug("Received $opCode")
                val heartbeatInterval: Long = d.get("heartbeat_interval").asLong()
                sendHeartbeat(heartbeatInterval)
            }
            HEARTBEAT_ACK -> {
                logger.debug("Heartbeat acknowledged")
                heartbeatsMissed = 0
            }
            else -> {
                logger.error("Unknown opcode: $opCode")
            }
        }
    }

    private fun sendHeartbeat(heartbeatInterval: Long) {
        try {
            if (webSocket != null) {
                val rawSocket: Socket = webSocket!!.socket
                rawSocket.soTimeout =
                    (heartbeatInterval + 10000).toInt() // setup a timeout when we miss
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
                heartbeatInterval,
                TimeUnit.MILLISECONDS)
    }

    private fun sendHeartbeat() {
        if (webSocket == null) {
            throw IllegalStateException("WebSocket is not connected")
        }

        val s: Int? = if (seq != null) seq else null

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", HEARTBEAT.code).put("d", s)

        if (heartbeatsMissed >= 2) {
            heartbeatsMissed = 0
            logger.warn("Heartbeat missed, will attempt to reconnect")
            webSocket!!.disconnect(CloseCode.RECONNECT.code, "ZOMBIE CONNECTION")
        } else {
            heartbeatsMissed += 1
            webSocket!!.sendText(heartbeat.toString())
            heartbeatStartTime = System.currentTimeMillis()
        }
    }

    private fun onEventType(eventType: String, d: JsonNode) {
        when (EventNames.fromString(eventType)) {
            EventNames.HELLO -> {
                // do nothing
            }
            EventNames.READY -> {
                // get ride of ?v=
                val libraryVersion = YDWKInfo.DISCORD_GATEWAY_VERSION.toString().substring(3)
                if (libraryVersion != d.get("v").asText()) {
                    logger.warn(
                        "Using library version {} but discord is using {}",
                        libraryVersion,
                        d.get("v").asText())
                }

                sessionId = d.get("session_id").asText()
                resumeUrl = d.get("resume_gateway_url").asText()

                val bot = BotImpl(d.get("user"), d.get("user").get("id").asLong(), ydwk)
                ydwk.bot = bot
                ydwk.cache[d.get("user").get("id").asText(), bot] = CacheType.USER

                val partialApplication =
                    PartialApplicationImpl(
                        d.get("application"), d.get("application").get("id").asLong(), ydwk)
                ydwk.partialApplication = partialApplication
                ydwk.cache[d.get("application").get("id").asText(), partialApplication] =
                    CacheType.APPLICATION

                val guilds: ArrayNode = d.get("guilds") as ArrayNode

                var availableGuildsAmount: Int = 0
                var unAvailableGuildsAmount: Int = 0

                for (guild in guilds) {
                    if (!guild.get("unavailable").asBoolean()) {
                        availableGuildsAmount += 1
                    } else {
                        unAvailableGuildsAmount += 1
                    }
                }

                ydwk.handleEvent(ReadyEvent(ydwk, availableGuildsAmount, unAvailableGuildsAmount))
            }
            EventNames.RESUMED -> TODO()
            EventNames.RECONNECT -> TODO()
            EventNames.INVALID_SESSION -> {
                sessionId = null
                resumeUrl = null
            }
            EventNames.APPLICATION_COMMAND_PERMISSIONS_UPDATE -> TODO()
            EventNames.CHANNEL_CREATE -> TODO()
            EventNames.CHANNEL_UPDATE -> TODO()
            EventNames.CHANNEL_DELETE -> TODO()
            EventNames.CHANNEL_PINS_UPDATE -> TODO()
            EventNames.THREAD_CREATE -> TODO()
            EventNames.THREAD_UPDATE -> TODO()
            EventNames.THREAD_DELETE -> TODO()
            EventNames.THREAD_LIST_SYNC -> TODO()
            EventNames.THREAD_MEMBERS_UPDATE -> TODO()
            EventNames.GUILD_CREATE -> GuildCreateHandler(ydwk, d).start()
            EventNames.GUILD_UPDATE -> GuildUpdateHandler(ydwk, d).start()
            EventNames.GUILD_DELETE -> GuildDeleteHandler(ydwk, d).start()
            EventNames.GUILD_BAN_ADD -> TODO()
            EventNames.GUILD_BAN_REMOVE -> TODO()
            EventNames.GUILD_EMOJIS_UPDATE -> TODO()
            EventNames.GUILD_INTEGRATIONS_UPDATE -> TODO()
            EventNames.GUILD_MEMBER_ADD -> TODO()
            EventNames.GUILD_MEMBER_REMOVE -> TODO()
            EventNames.GUILD_MEMBER_UPDATE -> TODO()
            EventNames.GUILD_ROLE_CREATE -> TODO()
            EventNames.GUILD_ROLE_UPDATE -> TODO()
            EventNames.GUILD_ROLE_DELETE -> TODO()
            EventNames.GUILD_SCHEDULED_EVENT_CREATE -> TODO()
            EventNames.GUILD_SCHEDULED_EVENT_UPDATE -> TODO()
            EventNames.GUILD_SCHEDULED_EVENT_DELETE -> TODO()
            EventNames.GUILD_SCHEDULED_EVENT_USER_ADD -> TODO()
            EventNames.GUILD_SCHEDULED_EVENT_USER_REMOVE -> TODO()
            EventNames.INTEGRATION_CREATE -> TODO()
            EventNames.INTEGRATION_UPDATE -> TODO()
            EventNames.INTEGRATION_DELETE -> TODO()
            EventNames.INTERACTION_CREATE -> TODO()
            EventNames.INVITE_CREATE -> TODO()
            EventNames.INVITE_DELETE -> TODO()
            EventNames.MESSAGE_CREATE -> TODO()
            EventNames.MESSAGE_UPDATE -> TODO()
            EventNames.MESSAGE_DELETE -> TODO()
            EventNames.MESSAGE_DELETE_BULK -> TODO()
            EventNames.MESSAGE_REACTION_ADD -> TODO()
            EventNames.MESSAGE_REACTION_REMOVE -> TODO()
            EventNames.MESSAGE_REACTION_REMOVE_ALL -> TODO()
            EventNames.PRESENCE_UPDATE -> TODO()
            EventNames.TYPING_START -> TODO()
            EventNames.USER_UPDATE -> UserUpdateHandler(ydwk, d).start()
            EventNames.VOICE_STATE_UPDATE -> TODO()
            EventNames.VOICE_SERVER_UPDATE -> TODO()
            EventNames.WEBHOOKS_UPDATE -> TODO()
            EventNames.UNKNOWN -> {
                logger.error("Unknown event type: $eventType")
            }
        }
    }

    private fun invalidate() {
        sessionId = null
        resumeUrl = null
    }

    fun shutdown() {
        heartbeatThread?.cancel(true)
        webSocket?.disconnect()
        logger.info("Disconnected from websocket")
    }
}
