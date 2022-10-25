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
package io.github.ydwk.ydwk.ws

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.neovisionaries.ws.client.*
import io.github.ydwk.ydwk.YDWKInfo
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.eventmanager.handler.handlers.ban.GuildBanAddHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.ban.GuildBanRemoveHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.channel.ChannelCreateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.channel.ChannelDeleteHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.channel.ChannelPinsUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.channel.ChannelUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.emoji.GuildEmojisUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.guild.GuildCreateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.guild.GuildDeleteHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.guild.GuildUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.interactions.InteractionCreateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.intergration.GuildIntegrationsUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.intergration.IntegrationCreateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.intergration.IntegrationDeleteHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.intergration.IntegrationUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.invite.InviteCreateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.invite.InviteDeleteHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.member.GuildMemberAddHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.member.GuildMemberRemoveHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.member.GuildMemberUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.message.*
import io.github.ydwk.ydwk.eventmanager.handler.handlers.presence.PresenceUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.role.GuildRoleCreateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.role.GuildRoleDeleteHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.role.GuildRoleUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.schedule.*
import io.github.ydwk.ydwk.eventmanager.handler.handlers.thread.*
import io.github.ydwk.ydwk.eventmanager.handler.handlers.user.UserUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.voice.VoiceServerUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.voice.VoiceStateUpdateHandler
import io.github.ydwk.ydwk.eventmanager.handler.handlers.webhook.WebhooksUpdateHandler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.BotImpl
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.impl.entities.application.PartialApplicationImpl
import io.github.ydwk.ydwk.util.convertInstantToChronoZonedDateTime
import io.github.ydwk.ydwk.util.reverseFormatZonedDateTime
import io.github.ydwk.ydwk.ws.util.CloseCode
import io.github.ydwk.ydwk.ws.util.EventNames
import io.github.ydwk.ydwk.ws.util.GateWayIntent
import io.github.ydwk.ydwk.ws.util.OpCode
import io.github.ydwk.ydwk.ws.util.OpCode.*
import io.github.ydwk.ydwk.ws.util.impl.LoggedInImpl
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.random.Random
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
    var upTime: Instant? = null
    @Volatile protected var heartbeatThread: Future<*>? = null
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    @get:Synchronized @set:Synchronized var connected = false
    @get:Synchronized @set:Synchronized var ready = false
    private var alreadySentConnectMessageOnce: Boolean = false
    private var identifyRateLimit = false
    private var identifyTime = 0L
    private var attemptedToResume = false
    private var timesTriedToConnect = 0

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
            sessionId = null
            logger.error("Failed to connect to gateway, will try again in 10 seconds", e)
            if (timesTriedToConnect > 3) {
                timesTriedToConnect = 0
                logger.error("Failed to connect to gateway 3 times, shutting down")
                ydwk.shutdownAPI()
            } else {
                scheduler.schedule(
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
        attemptedToResume = false
        if (sessionId == null) {
            identify()
            upTime = Instant.now()
        } else {
            resume()
            upTime = Instant.now()
        }
    }

    private fun sendCloseCode(code: CloseCode) {
        checkNotNull(webSocket) { "WebSocket is null" }
        webSocket!!.sendClose(code.code(), code.getReason())
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

        val closeFrame: WebSocketFrame? = if (closedByServer) serverCloseFrame else clientCloseFrame

        val closeCodeReason: String =
            if (closeFrame != null) closeFrame.closeReason else "Unknown reason"

        val closeCodeAsString: String =
            if (closeFrame != null)
                CloseCode.from(closeFrame.closeCode).name + " (" + closeFrame.closeCode + ")"
            else "Unknown code"

        logger.info(
            "Disconnected from websocket with close code $closeCodeAsString and reason $closeCodeReason")

        ydwk.emitEvent(
            io.github.ydwk.ydwk.eventmanager.event.events.gateway.DisconnectEvent(
                ydwk, closeCodeAsString, closeCodeReason, Instant.now()))

        heartbeatThread?.cancel(false)

        val closeCode = CloseCode.from(closeFrame?.closeCode ?: 1000)

        if (closeCode.isReconnect()) {
            logger.info("Reconnecting to websocket")
            connect()
        } else {
            logger.info("Not able to reconnect to websocket, shutting down")
            ydwk.emitEvent(
                io.github.ydwk.ydwk.eventmanager.event.events.gateway.ShutDownEvent(
                    ydwk, closeCode, Instant.now()))
            ydwk.shutdownAPI()
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
        identifyTime = System.currentTimeMillis()
        identifyRateLimit = true
    }

    private fun resume() {
        val json = ydwk.objectNode.put("token", token).put("session_id", sessionId).put("seq", seq)

        val identify: ObjectNode = ydwk.objectNode.put("op", RESUME.code).set("d", json)

        webSocket?.sendText(identify.toString())
        attemptedToResume = true
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
                sendCloseCode(CloseCode.RECONNECT)
            }
            INVALID_SESSION -> {
                logger.debug("Received $opCode")
                identifyRateLimit =
                    identifyRateLimit && System.currentTimeMillis() - identifyTime < 5000

                if (identifyRateLimit) {
                    logger.warn(
                        "Identify rate limit exceeded, waiting 5 seconds before reconnecting")

                    try {
                        Thread.sleep(5000)
                    } catch (e: InterruptedException) {
                        logger.error("Error while sleeping", e)
                    }
                } else if (attemptedToResume) {
                    val oneToFiveSeconds = Random.nextInt(1, 5)
                    logger.warn(
                        "Invalid session, waiting $oneToFiveSeconds seconds before reconnecting")

                    try {
                        Thread.sleep(oneToFiveSeconds * 1000L)
                    } catch (e: InterruptedException) {
                        logger.error("Error while sleeping", e)
                    }
                }

                sessionId = null
                resumeUrl = null

                sendCloseCode(CloseCode.INVALID_SESSION)
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

    @Synchronized
    private fun deleteMessageCachePast14Days() {

        val now = Instant.now()
        val fourteenDaysAgo: Instant = now.minus(14, ChronoUnit.DAYS)
        ydwk.cache.values(CacheIds.MESSAGE).forEach {
            if (it is MessageImpl &&
                reverseFormatZonedDateTime(it.time)
                    .isBefore(convertInstantToChronoZonedDateTime(fourteenDaysAgo))) {
                ydwk.cache.remove(it.id, CacheIds.MESSAGE)
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

    fun sendHeartbeat() {
        if (webSocket == null) {
            throw IllegalStateException("WebSocket is not connected")
        }

        val s: Int? = if (seq != null) seq else null

        val heartbeat: JsonNode =
            ydwk.objectMapper.createObjectNode().put("op", HEARTBEAT.code).put("d", s)

        if (heartbeatsMissed >= 2) {
            heartbeatsMissed = 0
            logger.warn("Heartbeat missed, will attempt to reconnect")
            sendCloseCode(CloseCode.MISSED_HEARTBEAT)
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
                identifyRateLimit = false
                attemptedToResume = false
                ready = true

                val bot = BotImpl(d.get("user"), d.get("user").get("id").asLong(), ydwk)
                ydwk.bot = bot
                ydwk.cache[d.get("user").get("id").asText(), bot] = CacheIds.USER

                val partialApplication =
                    PartialApplicationImpl(
                        d.get("application"), d.get("application").get("id").asLong(), ydwk)
                ydwk.applicationId = partialApplication.id
                ydwk.partialApplication = partialApplication
                ydwk.cache[d.get("application").get("id").asText(), partialApplication] =
                    CacheIds.APPLICATION

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
                ydwk.emitEvent(
                    io.github.ydwk.ydwk.eventmanager.event.events.gateway.ReadyEvent(
                        ydwk, availableGuildsAmount, unAvailableGuildsAmount))
            }
            EventNames.RESUMED -> {
                attemptedToResume = false
                ydwk.emitEvent(
                    io.github.ydwk.ydwk.eventmanager.event.events.gateway.ResumeEvent(ydwk))
            }
            EventNames.RECONNECT -> {
                attemptedToResume = false
                ydwk.emitEvent(
                    io.github.ydwk.ydwk.eventmanager.event.events.gateway.ReconnectEvent(ydwk))
            }
            EventNames.INVALID_SESSION -> {
                sessionId = null
                resumeUrl = null
            }
            EventNames.APPLICATION_COMMAND_PERMISSIONS_UPDATE -> TODO()
            EventNames.CHANNEL_CREATE -> ChannelCreateHandler(ydwk, d).start()
            EventNames.CHANNEL_UPDATE -> ChannelUpdateHandler(ydwk, d).start()
            EventNames.CHANNEL_DELETE -> ChannelDeleteHandler(ydwk, d).start()
            EventNames.CHANNEL_PINS_UPDATE -> ChannelPinsUpdateHandler(ydwk, d).start()
            EventNames.THREAD_CREATE -> ThreadCreateHandler(ydwk, d).start()
            EventNames.THREAD_UPDATE -> ThreadUpdateHandler(ydwk, d).start()
            EventNames.THREAD_DELETE -> ThreadDeleteHandler(ydwk, d).start()
            EventNames.THREAD_LIST_SYNC -> ThreadListSyncHandler(ydwk, d).start()
            EventNames.THREAD_MEMBERS_UPDATE -> ThreadMembersUpdateHandler(ydwk, d).start()
            EventNames.GUILD_CREATE -> GuildCreateHandler(ydwk, d).start()
            EventNames.GUILD_UPDATE -> GuildUpdateHandler(ydwk, d).start()
            EventNames.GUILD_DELETE -> GuildDeleteHandler(ydwk, d).start()
            EventNames.GUILD_BAN_ADD -> GuildBanAddHandler(ydwk, d).start()
            EventNames.GUILD_BAN_REMOVE -> GuildBanRemoveHandler(ydwk, d).start()
            EventNames.GUILD_EMOJIS_UPDATE -> GuildEmojisUpdateHandler(ydwk, d).start()
            EventNames.GUILD_INTEGRATIONS_UPDATE -> GuildIntegrationsUpdateHandler(ydwk, d).start()
            EventNames.GUILD_MEMBER_ADD -> GuildMemberAddHandler(ydwk, d).start()
            EventNames.GUILD_MEMBER_REMOVE -> GuildMemberRemoveHandler(ydwk, d).start()
            EventNames.GUILD_MEMBER_UPDATE -> GuildMemberUpdateHandler(ydwk, d).start()
            EventNames.GUILD_ROLE_CREATE -> GuildRoleCreateHandler(ydwk, d).start()
            EventNames.GUILD_ROLE_UPDATE -> GuildRoleUpdateHandler(ydwk, d).start()
            EventNames.GUILD_ROLE_DELETE -> GuildRoleDeleteHandler(ydwk, d).start()
            EventNames.GUILD_SCHEDULED_EVENT_CREATE ->
                GuildScheduledEventCreateHandler(ydwk, d).start()
            EventNames.GUILD_SCHEDULED_EVENT_UPDATE ->
                GuildScheduledEventUpdateHandler(ydwk, d).start()
            EventNames.GUILD_SCHEDULED_EVENT_DELETE ->
                GuildScheduledEventDeleteHandler(ydwk, d).start()
            EventNames.GUILD_SCHEDULED_EVENT_USER_ADD ->
                GuildScheduledEventUserAddHandler(ydwk, d).start()
            EventNames.GUILD_SCHEDULED_EVENT_USER_REMOVE ->
                GuildScheduledEventUserRemoveHandler(ydwk, d).start()
            EventNames.INTEGRATION_CREATE -> IntegrationCreateHandler(ydwk, d).start()
            EventNames.INTEGRATION_UPDATE -> IntegrationUpdateHandler(ydwk, d).start()
            EventNames.INTEGRATION_DELETE -> IntegrationDeleteHandler(ydwk, d).start()
            EventNames.INTERACTION_CREATE -> InteractionCreateHandler(ydwk, d).start()
            EventNames.INVITE_CREATE -> InviteCreateHandler(ydwk, d).start()
            EventNames.INVITE_DELETE -> InviteDeleteHandler(ydwk, d).start()
            EventNames.MESSAGE_CREATE -> MessageCreateHandler(ydwk, d).start()
            EventNames.MESSAGE_UPDATE -> MessageUpdateHandler(ydwk, d).start()
            EventNames.MESSAGE_DELETE -> MessageDeleteHandler(ydwk, d).start()
            EventNames.MESSAGE_DELETE_BULK -> MessageBulkDeleteHandler(ydwk, d).start()
            EventNames.MESSAGE_REACTION_ADD -> MessageReactionAddHandler(ydwk, d).start()
            EventNames.MESSAGE_REACTION_REMOVE -> MessageReactionRemoveHandler(ydwk, d).start()
            EventNames.MESSAGE_REACTION_REMOVE_ALL ->
                MessageReactionRemoveAllHandler(ydwk, d).start()
            EventNames.PRESENCE_UPDATE -> PresenceUpdateHandler(ydwk, d).start()
            EventNames.TYPING_START -> logger.debug("This event is not supported")
            EventNames.USER_UPDATE -> UserUpdateHandler(ydwk, d).start()
            EventNames.VOICE_STATE_UPDATE -> VoiceStateUpdateHandler(ydwk, d).start()
            EventNames.VOICE_SERVER_UPDATE -> VoiceServerUpdateHandler(ydwk, d).start()
            EventNames.WEBHOOKS_UPDATE -> WebhooksUpdateHandler(ydwk, d).start()
            EventNames.UNKNOWN -> {
                logger.error("Unknown event type: $eventType")
            }
        }
    }

    private fun invalidate() {
        sessionId = null
        resumeUrl = null
        ydwk.cache.clear()
        heartbeatThread?.cancel(false)
        ydwk.setLoggedIn(LoggedInImpl(false).setDisconnectedTime())
        scheduler.shutdownNow()
        upTime = null
    }

    fun shutdown() {
        invalidate()
        if (webSocket != null) {
            webSocket!!.disconnect()
        }
        logger.info("Shutting down gateway")
        try {
            Runtime.getRuntime().exit(0)
        } catch (e: Exception) {
            logger.error("Failed to shutdown vm", e)
        }
    }
}
