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
package io.github.ydwk.yde.impl

import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.Bot
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.ThreadFactory
import io.github.ydwk.ydwk.*
import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.backend.managers.CoroutineEventManager
import io.github.ydwk.ydwk.evm.backend.managers.SampleEventManager
import io.github.ydwk.ydwk.logging.*
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient

class YDWKImpl(
    private val client: OkHttpClient,
    private var token: String? = null,
    private val guildIdList: MutableList<String> = mutableListOf(),
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

    override val defaultScheduledExecutorService: ScheduledExecutorService =
        Executors.newScheduledThreadPool(1)

    override val threadFactory: ThreadFactory
        get() = ThreadFactory

    override var webSocketManager: WebSocketManager? = null
        private set

    override fun shutdownAPI() {
        val oneToFiveSecondTimeout = Random.nextLong(1000, 5000)
        invalidateRestApi(oneToFiveSecondTimeout)
        info("Timeout for $oneToFiveSecondTimeout then shutting down")

        try {
            Thread.sleep(oneToFiveSecondTimeout)
        } catch (e: InterruptedException) {
            error("Error while sleeping" + e.message)
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
                error("Error while closing cache" + e.message)
            }
        }
        client.dispatcher.executorService.awaitTermination(
            oneToFiveSecondTimeout, TimeUnit.MILLISECONDS)
    }

    override val uptime: Instant
        get() = webSocketManager!!.upTime ?: throw IllegalStateException("Bot is not logged in")

    override fun setGuildIds(vararg guildIds: String) {
        guildIds.forEach { this.guildIdList.add(it) }
    }

    override fun toString(): String {
        return EntityToStringBuilder(this, this)
            .add("token", token)
            .add("applicationId", applicationId)
            .add("uptime", uptime)
            .toString()
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
            debug("Waiting for bot to be ready")
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
        simpleEventManager.emitEvent(event)
        coroutineEventManager.emitEvent(event)
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

    private val enabledLoggerStatus: List<YDWKLoggerStatus> = YDWKLoggerStatus.ALL
    private val ydwkLoggerManager: YDWKLogManager = YDWKLogManager(enabledLoggerStatus)

    fun info(name: String): YDWKLogger {
        return YDWKLoggerImpl(ydwkLoggerManager, name).setSeverity(YDWKLoggerSeverity.INFO)
    }

    fun error(name: String): YDWKLogger {
        return YDWKLoggerImpl(ydwkLoggerManager, name).setSeverity(YDWKLoggerSeverity.ERROR)
    }

    fun debug(name: String): YDWKLogger {
        return YDWKLoggerImpl(ydwkLoggerManager, name).setSeverity(YDWKLoggerSeverity.DEBUG)
    }

    fun warn(name: String): YDWKLogger {
        return YDWKLoggerImpl(ydwkLoggerManager, name).setSeverity(YDWKLoggerSeverity.WARN)
    }

    companion object {
        private val enabledLoggerStatus: List<YDWKLoggerStatus> = YDWKLoggerStatus.ALL
        private val ydwkLoggerManager: YDWKLogManager = YDWKLogManager(enabledLoggerStatus)

        fun info(name: String): YDWKLogger {
            return YDWKLoggerImpl(ydwkLoggerManager, name).setSeverity(YDWKLoggerSeverity.INFO)
        }

        fun error(name: String, e: Throwable?): YDWKLogger {
            return YDWKLoggerImpl(ydwkLoggerManager, name + e).setSeverity(YDWKLoggerSeverity.ERROR)
        }

        fun debug(name: String): YDWKLogger {
            return YDWKLoggerImpl(ydwkLoggerManager, name).setSeverity(YDWKLoggerSeverity.DEBUG)
        }

        fun warn(name: String): YDWKLogger {
            return YDWKLoggerImpl(ydwkLoggerManager, name).setSeverity(YDWKLoggerSeverity.WARN)
        }
    }
}
