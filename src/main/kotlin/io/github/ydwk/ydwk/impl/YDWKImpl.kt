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
package io.github.ydwk.ydwk.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.cache.*
import io.github.ydwk.ydwk.entities.Application
import io.github.ydwk.ydwk.entities.Bot
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.channel.VoiceChannel
import io.github.ydwk.ydwk.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.ydwk.event.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.event.backend.event.GenericEvent
import io.github.ydwk.ydwk.event.backend.event.IEventListener
import io.github.ydwk.ydwk.event.backend.managers.CoroutineEventManager
import io.github.ydwk.ydwk.event.backend.managers.SampleEventManager
import io.github.ydwk.ydwk.impl.entities.message.embed.builder.EmbedBuilderImpl
import io.github.ydwk.ydwk.impl.rest.RestApiManagerImpl
import io.github.ydwk.ydwk.impl.slash.SlashBuilderImpl
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.slash.SlashBuilder
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.GateWayIntent
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class YDWKImpl(
    private val client: OkHttpClient,
    private val simpleEventManager: SampleEventManager = SampleEventManager(),
    private val coroutineEventManager: CoroutineEventManager = CoroutineEventManager(),
    val logger: Logger = LoggerFactory.getLogger(YDWKImpl::class.java),
    private val allowedCache: MutableSet<CacheIds> = mutableSetOf(),
    val cache: Cache = PerpetualCache(allowedCache),
    val memberCache: MemberCache = MemberCacheImpl(allowedCache),
    private var token: String? = null,
    private var guildIdList: MutableList<String> = mutableListOf(),
    var applicationId: String? = null
) : YDWK {
    // logger

    override val objectNode: ObjectNode
        get() = JsonNodeFactory.instance.objectNode()

    override val objectMapper: ObjectMapper
        get() = ObjectMapper()

    override var webSocketManager: WebSocketManager? = null
        private set

    override fun shutdownAPI() {
        val oneToFiveSecondTimeout = Random.nextLong(1000, 5000)
        invalidateRestApi(oneToFiveSecondTimeout)
        logger.info("Timeout for $oneToFiveSecondTimeout then shutting down")

        try {
            Thread.sleep(oneToFiveSecondTimeout)
        } catch (e: InterruptedException) {
            logger.error("Error while sleeping", e)
        }
        webSocketManager?.shutdown()
    }

    private fun invalidateRestApi(oneToFiveSecondTimeout: Long) {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
        client.dispatcher.cancelAll()
        if (client.cache != null) {
            try {
                client.cache!!.close()
            } catch (e: Exception) {
                logger.error("Error while closing cache", e)
            }
        }
        client.dispatcher.executorService.awaitTermination(
            oneToFiveSecondTimeout, TimeUnit.MILLISECONDS)
    }

    override fun getGuild(id: String): Guild? {
        return cache[id, CacheIds.GUILD] as Guild?
    }

    override fun getGuilds(): List<Guild> {
        return cache.values(CacheIds.GUILD).map { it as Guild }
    }

    override val restApiManager: RestApiManager
        get() {
            val botToken = token ?: throw IllegalStateException("Bot token is not set")
            return RestApiManagerImpl(botToken, this, client)
        }
    override val uptime: Instant
        get() = webSocketManager!!.upTime ?: throw IllegalStateException("Bot is not logged in")

    override val slashBuilder: SlashBuilder
        get() =
            SlashBuilderImpl(
                this,
                guildIdList,
                applicationId ?: throw IllegalStateException("Application ID is not set"))

    override fun setGuildIds(vararg guildIds: String) {
        guildIds.forEach { this.guildIdList.add(it) }
    }

    override fun setAllowedCache(vararg cacheTypes: CacheIds) {
        allowedCache.addAll(cacheTypes.toSet())
    }

    override fun setDisallowedCache(vararg cacheTypes: CacheIds) {
        allowedCache.removeAll(cacheTypes.toSet())
    }

    override fun getTextChannel(id: Long): TextChannel? {
        return cache[id.toString(), CacheIds.TEXT_CHANNEL] as TextChannel?
    }

    override fun getTextChannels(): List<TextChannel> {
        return cache.values(CacheIds.TEXT_CHANNEL).map { it as TextChannel }
    }

    override fun getVoiceChannel(id: Long): VoiceChannel? {
        return cache[id.toString(), CacheIds.VOICE_CHANNEL] as VoiceChannel?
    }

    override fun getVoiceChannels(): List<VoiceChannel> {
        return cache.values(CacheIds.VOICE_CHANNEL).map { it as VoiceChannel }
    }

    override val embedBuilder: EmbedBuilder
        get() = EmbedBuilderImpl(this)

    override var bot: Bot? = null
        get() {
            while (field == null) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } // wait for bot to be set
            return field
        }

    override var partialApplication: PartialApplication? = null
        get() {
            while (field == null) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (field == null) {
                        logger.error("Partial Application is null")
                    }
                }
            } // wait for application to be set
            return field
        }

    override var application: Application? = null
        get() {
            while (field == null) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (field == null) {
                        logger.error("Application is null")
                    }
                }
            } // wait for application to be set
            return field
        }

    override var loggedInStatus: LoggedIn? = null
        private set

    @get:Synchronized
    override val waitForConnection: YDWK
        get() {
            val ws = webSocketManager
            if (ws == null) {
                throw IllegalStateException("WebSocketManager is not initialized")
            } else {
                while (!ws.connected) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        logger.error("Error while waiting for connection", e)
                    } finally {
                        if (ws.connected) {
                            logger.info("WebSocketManager connected")
                        } else {
                            logger.info("WebSocketManager not connected, retrying")
                            waitForConnection // retry
                        }
                    }
                }
            }
            return this
        }
    override val waitForReady: YDWK
        get() {
            val ws = webSocketManager
            if (ws == null) {
                throw IllegalStateException("WebSocketManager is not initialized")
            } else {
                while (!ws.ready) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        logger.error("Error while waiting for ready", e)
                    } finally {
                        if (!ws.ready) {
                            waitForReady // retry
                        }
                    }
                }
            }
            return this
        }

    override fun addEvent(vararg eventListeners: Any) {
        for (eventListener in eventListeners) {
            when (eventListener) {
                is IEventListener -> {
                    simpleEventManager.addEvent(eventListener)
                }
                is CoroutineEventListener -> {
                    coroutineEventManager.addEvent(eventListener)
                }
                else -> {
                    logger.error(
                        "Event listener is not an instance of EventListener or CoroutineEventListener")
                }
            }
        }
    }

    override fun removeEvent(vararg eventListeners: Any) {
        for (eventListener in eventListeners) {
            when (eventListener) {
                is IEventListener -> {
                    simpleEventManager.removeEvent(eventListener)
                }
                is CoroutineEventListener -> {
                    coroutineEventManager.removeEvent(eventListener)
                }
                else -> {
                    logger.error(
                        "Event listener is not an instance of EventListener or CoroutineEventListener")
                }
            }
        }
    }

    override fun emitEvent(event: GenericEvent) {
        simpleEventManager.emitEvent(event)
        coroutineEventManager.emitEvent(event)
    }

    /**
     * Used to start the websocket manager
     *
     * @param token The token of the bot which is used to authenticate the bot.
     * @param intents The gateway intent which will decide what events are sent by discord.
     */
    fun setWebSocketManager(token: String, intents: List<GateWayIntent>) {
        this.webSocketManager = WebSocketManager(this, token, intents).connect()
        this.token = token
    }

    /**
     * Used to set the logged in status
     *
     * @param loggedIn The logged in status which is used to send messages to discord.
     */
    fun setLoggedIn(loggedIn: LoggedIn) {
        this.loggedInStatus = loggedIn
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(YDWKImpl::class.java)
    }
}
