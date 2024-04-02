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
package io.github.ydwk.ydwk.ws

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.neovisionaries.ws.client.*
import io.github.ydwk.ydwk.*
import io.github.ydwk.ydwk.evm.event.events.gateway.DisconnectEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ReconnectEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ResumeEvent
import io.github.ydwk.ydwk.evm.event.events.gateway.ShutDownEvent
import io.github.ydwk.ydwk.evm.handler.handlers.ban.GuildBanAddHandler
import io.github.ydwk.ydwk.evm.handler.handlers.ban.GuildBanRemoveHandler
import io.github.ydwk.ydwk.evm.handler.handlers.channel.ChannelCreateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.channel.ChannelDeleteHandler
import io.github.ydwk.ydwk.evm.handler.handlers.channel.ChannelPinsUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.channel.ChannelUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.emoji.GuildEmojisUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.guild.GuildCreateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.guild.GuildDeleteHandler
import io.github.ydwk.ydwk.evm.handler.handlers.guild.GuildUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.interactions.InteractionCreateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.intergration.GuildIntegrationsUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.intergration.IntegrationCreateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.intergration.IntegrationDeleteHandler
import io.github.ydwk.ydwk.evm.handler.handlers.intergration.IntegrationUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.invite.InviteCreateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.invite.InviteDeleteHandler
import io.github.ydwk.ydwk.evm.handler.handlers.member.GuildMemberAddHandler
import io.github.ydwk.ydwk.evm.handler.handlers.member.GuildMemberRemoveHandler
import io.github.ydwk.ydwk.evm.handler.handlers.member.GuildMemberUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.message.*
import io.github.ydwk.ydwk.evm.handler.handlers.presence.PresenceUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.role.GuildRoleCreateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.role.GuildRoleDeleteHandler
import io.github.ydwk.ydwk.evm.handler.handlers.role.GuildRoleUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.schedule.*
import io.github.ydwk.ydwk.evm.handler.handlers.thread.*
import io.github.ydwk.ydwk.evm.handler.handlers.user.UserUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.voice.VoiceServerUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.voice.VoiceStateUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.webhook.WebhooksUpdateHandler
import io.github.ydwk.ydwk.evm.handler.handlers.ws.ReadyHandler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.voice.leaveVC
import io.github.ydwk.ydwk.ws.logging.WebsocketLogging
import io.github.ydwk.ydwk.ws.util.CloseCode
import io.github.ydwk.ydwk.ws.util.EventNames
import io.github.ydwk.ydwk.ws.util.HeartBeat
import io.github.ydwk.ydwk.ws.util.OpCode
import io.github.ydwk.ydwk.ws.util.OpCode.*
import io.github.ydwk.ydwk.ws.util.impl.LoggedInImpl
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.Instant
import java.util.*
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import net.sf.fmj.utility.LoggerSingleton.logger
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebSocketManager(
    protected var ydwk: YDWKImpl,
    private var token: String,
    private var intents: List<GateWayIntent>,
    private var userStatus: UserStatus? = null,
    private var activity: ActivityPayload? = null,
    val etfInsteadOfJson: Boolean,
) : WebSocketAdapter(), WebSocketListener {
    private val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger

    // Tha main websocket
    private var webSocket: WebSocket? = null
    private var resumeUrl: String? = null
    private var sessionId: String? = null
    private var seq: Int? = null
    var upTime: Instant? = null
    val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    @get:Synchronized @set:Synchronized var connected = false
    @get:Synchronized @set:Synchronized var ready = false
    private var alreadySentConnectMessageOnce: Boolean = false
    private var identifyRateLimit = false
    private var identifyTime = 0L
    private var attemptedToResume = false
    private var timesTriedToConnect = 0
    private var heartBeat: HeartBeat? = null

    @Synchronized
    fun connect(): WebSocketManager {
        val url: String =
            (resumeUrl
                ?: YDWKInfo.DISCORD_GATEWAY_URL.getUrl()) +
                YDWKInfo.DISCORD_GATEWAY_VERSION.getUrl() +
                YDWKInfo.JSON_ENCODING.getUrl()

        try {
            webSocket = getWS(url, etfInsteadOfJson, this, logger)
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
            """
                        .trimIndent())
                alreadySentConnectMessageOnce = true
            } else {
                logger.info("Reconnected to gateway")
            }
        } else {
            logger.info("Resuming session$sessionId")
        }

        connected = true
        attemptedToResume = false
        upTime =
            if (sessionId == null) {
                identify()
                Instant.now()
            } else {
                resume()
                Instant.now()
            }
    }

    private fun sendCloseCode(code: CloseCode) {
        checkNotNull(webSocket) { "WebSocket is null" }
        webSocket!!.sendClose(code.getCode(), code.getReason())
    }

    @Throws(Exception::class)
    override fun onConnectError(websocket: WebSocket, cause: WebSocketException) {
        logger.error("Error connecting to websocket", cause)
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
                CloseCode.fromInt(closeFrame.closeCode).name + " (" + closeFrame.closeCode + ")"
            else "Unknown close code"

        logger.info(
            "Disconnected from websocket with close code $closeCodeAsString and reason $closeCodeReason")

        ydwk.emitEvent(DisconnectEvent(ydwk, closeCodeAsString, closeCodeReason, Instant.now()))

        heartBeat
            ?.heartbeatJob
            ?.cancel(
                CancellationException(
                    "Disconnected from websocket, cancelling heartbeat job$closeCodeAsString"))

        val closeCode = CloseCode.fromInt(closeFrame?.closeCode ?: 1000)

        if (closeCode.shouldReconnect()) {
            logger.info("Reconnecting to websocket")
            connect()
        } else {
            logger.info("Not able to reconnect to websocket, sending shutdown code")
            ydwk.emitEvent(ShutDownEvent(ydwk, closeCode, Instant.now()))
            ydwk.shutdownAPI()
        }
    }

    private fun invalidate() {

        checkForAnyBotsInVC()
        logger.info("Disconnecting bot from any potential vcs ")
        Thread.sleep(1000)

        sessionId = null
        resumeUrl = null
        ydwk.cache.clear()
        ydwk.coroutineDispatcher.cancelChildren(CancellationException("Invalidating websocket"))
        ydwk.coroutineDispatcher.cancel(CancellationException("Invalidating websocket"))
        heartBeat?.heartbeatJob?.cancel(CancellationException("Invalidating websocket"))
        ydwk.setLoggedIn(LoggedInImpl(false).setDisconnectedTime())
        scheduler.shutdownNow()
        upTime = null
    }

    private fun checkForAnyBotsInVC() {
        CoroutineScope(ydwk.coroutineDispatcher).launch {
            ydwk.getGuilds().forEach { it ->
                val botAsMember = it.getBotAsMember()
                if (botAsMember.voiceState != null) {
                    botAsMember.leaveVC()
                }
            }
        }
    }

    fun triggerShutdown() {
        logger.info("API has requested to be sut down")
        invalidate()
        webSocket!!.disconnect()
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

        val presence: ObjectNode = ydwk.objectNode.objectNode()

        if (activity != null) {
            val activitiesPayload: ArrayNode = ydwk.objectNode.arrayNode()
            activitiesPayload.add(
                ydwk.objectNode
                    .objectNode()
                    .put("name", activity!!.name)
                    .put("type", activity!!.type)
                    .put("url", activity!!.url))

            presence.set<ArrayNode>("activities", activitiesPayload)
        }

        if (userStatus != null) {
            presence.put("status", userStatus!!.getStatus())
        }

        d.set<ObjectNode>("presence", presence)

        val json: JsonNode = ydwk.objectNode.put("op", IDENTIFY.code).set("d", d)

        webSocket sendText json.toString()
        ydwk.setLoggedIn(LoggedInImpl(false).setLoggedInTime())
        identifyTime = System.currentTimeMillis()
        identifyRateLimit = true
    }

    private fun resume() {
        val json = ydwk.objectNode.put("token", token).put("session_id", sessionId).put("seq", seq)

        val identify: ObjectNode = ydwk.objectNode.put("op", RESUME.code).set("d", json)

        webSocket sendText identify.toString()
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
        when (OpCode.fromInt(opCode)) {
            DISPATCH -> {
                val event: String = rawJson.get("t").asText()
                onEventType(event, d)
            }
            RECONNECT -> {
                logger.debug("Received $opCode - RECONNECT")
                sendCloseCode(CloseCode.RECONNECT)
            }
            INVALID_SESSION -> {
                logger.debug("Received $opCode - INVALID_SESSION")
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
                logger.debug("Received $opCode - HELLO")
                val heartbeatInterval: Long = d.get("heartbeat_interval").asLong()
                heartBeat = HeartBeat(ydwk, webSocket!!)
                heartBeat?.startGateWayHeartbeat(heartbeatInterval, connected, seq)
            }
            HEARTBEAT_ACK -> {
                logger.debug("Received $opCode - HEARTBEAT_ACK")
                heartBeat?.receivedHeartbeatAck()
            }
            else -> {
                // do nothing
            }
        }
    }

    fun sendVoiceState(guildId: Long, channelId: Long?, muted: Boolean, deafen: Boolean) {
        val voiceJson: ObjectNode = ydwk.objectNode.put("op", VOICE_STATE.code)
        val dataObjectNode = ydwk.objectNode
        dataObjectNode
            .put("guild_id", guildId)
            .put("channel_id", channelId)
            .put("self_mute", muted)
            .put("self_mute", deafen)

        voiceJson.set<JsonNode>("d", dataObjectNode)

        webSocket sendText voiceJson.toString()
    }

    private fun onEventType(eventType: String, d: JsonNode) {
        CoroutineScope(ydwk.coroutineDispatcher).launch {
            when (EventNames.fromString(eventType)) {
                EventNames.HELLO -> {
                    // do nothing
                }
                EventNames.READY -> {
                    // get rid of ?v=
                    val libraryVersion = YDWKInfo.DISCORD_GATEWAY_VERSION.getUrl().substring(3)
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
                    ReadyHandler(ydwk, d).start()
                }
                EventNames.RESUMED -> {
                    attemptedToResume = false
                    ydwk.emitEvent(ResumeEvent(ydwk))
                }
                EventNames.RECONNECT -> {
                    attemptedToResume = false
                    ydwk.emitEvent(ReconnectEvent(ydwk))
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
                EventNames.GUILD_INTEGRATIONS_UPDATE ->
                    GuildIntegrationsUpdateHandler(ydwk, d).start()
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
    }

    private infix fun WebSocket?.sendText(text: String) {
        sendText(text)
    }

    companion object {
        fun getWS(
            url: String,
            etfInsteadOfJson: Boolean,
            webSocketListener: WebSocketListener,
            logger: Logger
        ): WebSocket {
            val webSocketFactory = WebSocketFactory()
            if (webSocketFactory.socketTimeout > 0)
                webSocketFactory.socketTimeout = 1000.coerceAtLeast(webSocketFactory.socketTimeout)
            else webSocketFactory.socketTimeout = 10000

            return webSocketFactory
                .createSocket(url)
                .addHeader("Accept-Encoding", "gzip")
                .setDirectTextMessage(etfInsteadOfJson)
                .addListener(webSocketListener)
                .addListener(WebsocketLogging(logger))
                .connect()
        }
    }
}
