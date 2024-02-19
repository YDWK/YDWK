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
package io.github.ydwk.ydwk.impl

import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.Bot
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.ydwk.*
import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.backend.managers.CoroutineEventManager
import io.github.ydwk.ydwk.evm.backend.managers.SampleEventManager
import io.github.ydwk.ydwk.evm.event.EventListeners
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory

class YDWKImpl(
    override var client: OkHttpClient,
    override var token: String? = null,
    override var guildIdList: MutableList<String> = mutableListOf(),
    applicationId: String? = null,
) :
    YDWK,
    YDEImpl(
        token,
        applicationId,
        client,
        guildIdList,
        YDWKInfo.GITHUB_URL.getUrl(),
        YDWKInfo.YDWK_VERSION.getUrl()) {

    private val simpleEventManager: SampleEventManager = SampleEventManager()
    private val coroutineEventManager: CoroutineEventManager = CoroutineEventManager()
    private val ydwkLogger = LoggerFactory.getLogger(YDWK::class.java)

    override val defaultScheduledExecutorService: ScheduledExecutorService =
        Executors.newScheduledThreadPool(1)
    override val eventListener: EventListeners
        get() = EventListeners(this)

    override var webSocketManager: WebSocketManager? = null
        private set

    override fun shutdownAPI() {
        val oneToFiveSecondTimeout = Random.nextLong(1000, 5000)
        shutDownRestApi()
        ydwkLogger.info("Timeout for $oneToFiveSecondTimeout then shutting down")

        try {
            Thread.sleep(oneToFiveSecondTimeout)
        } catch (e: InterruptedException) {
            ydwkLogger.error("Error while sleeping" + e.message)
        }

        try {
            webSocketManager?.triggerShutdown()
        } catch (e: Exception) {
            ydwkLogger.error("Error while shutting down websocket" + e.message)
        }
    }

    private fun shutDownRestApi() {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
        client.cache?.close()
    }

    fun enableShutDownHook() {
        Runtime.getRuntime()
            .addShutdownHook(
                Thread {
                    ydwkLogger.info(
                        "Received shutdown code triggered by user or API, shutting down")
                    shutdownAPI()
                })
    }

    override val uptime: Instant
        get() = webSocketManager!!.upTime ?: throw IllegalStateException("Bot is not logged in")

    override fun setGuildIds(vararg guildIds: String) {
        guildIds.forEach { this.guildIdList.add(it) }
    }

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
                        error("Partial Application is null")
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
                        error("Application is null")
                    }
                }
            } // wait for application to be set
            return field
        }

    override var loggedInStatus: LoggedIn? = null
        private set

    override suspend fun awaitReady(): YDWK {
        val ws = webSocketManager ?: throw IllegalStateException("Bot is not logged in")
        while (!ws.ready) {
            delay(1000)
            ydwkLogger.debug("Waiting for bot to be ready")
        } // wait for bot to be ready
        return this
    }

    override fun addEventListeners(vararg eventListeners: Any) {
        for (eventListener in eventListeners) {
            when (eventListener) {
                is IEventListener -> {
                    simpleEventManager.addEvent(eventListener)
                }
                is CoroutineEventListener -> {
                    coroutineEventManager.addEvent(eventListener)
                }
                else -> {
                    error(
                        "Event listener is not an instance of EventListener or CoroutineEventListener")
                }
            }
        }
    }

    override fun removeEventListeners(vararg eventListeners: Any) {
        for (eventListener in eventListeners) {
            when (eventListener) {
                is IEventListener -> {
                    simpleEventManager.removeEvent(eventListener)
                }
                is CoroutineEventListener -> {
                    coroutineEventManager.removeEvent(eventListener)
                }
                else -> {
                    error(
                        "Event listener is not an instance of EventListener or CoroutineEventListener")
                }
            }
        }
    }

    override fun emitEvent(event: GenericEvent) {
        try {
            runBlocking {
                simpleEventManager.emitEvent(event)
                coroutineEventManager.emitEvent(event)
            }
        } catch (e: Exception) {
            ydwkLogger.error("Error while emitting event" + e.message)
        }
    }

    /**
     * Starts the websocket manager
     *
     * @param token The token of the bot which is used to authenticate the bot.
     * @param intents The gateway intent which will decide what events are sent by discord.
     * @param userStatus The status of the bot.
     * @param activity The activity of the bot.
     * @param etfInsteadOfJson Whether to use ETF instead of JSON.
     */
    fun setWebSocketManager(
        token: String,
        intents: List<GateWayIntent>,
        userStatus: UserStatus? = null,
        activity: ActivityPayload? = null,
        etfInsteadOfJson: Boolean,
    ) {
        val ws: WebSocketManager?
        ws = WebSocketManager(this, token, intents, userStatus, activity, etfInsteadOfJson)
        this.webSocketManager = ws.connect()
        // this.webSocketManager!!.deleteMessageCachePast14Days()
        this.token = token
    }

    /**
     * Sets the logged in status
     *
     * @param loggedIn The logged in status which is used to send messages to discord.
     */
    fun setLoggedIn(loggedIn: LoggedIn) {
        this.loggedInStatus = loggedIn
    }

    override fun toString(): String {
        return EntityToStringBuilder(this, this)
            .add("token", token)
            .add("applicationId", applicationId)
            .add("uptime", uptime)
            .toString()
    }
}
