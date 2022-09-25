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
package io.github.realyusufismail.ydwk.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.cache.*
import io.github.realyusufismail.ydwk.entities.Application
import io.github.realyusufismail.ydwk.entities.Bot
import io.github.realyusufismail.ydwk.entities.Guild
import io.github.realyusufismail.ydwk.entities.application.PartialApplication
import io.github.realyusufismail.ydwk.event.Event
import io.github.realyusufismail.ydwk.event.recieve.CoroutineEventListener
import io.github.realyusufismail.ydwk.event.recieve.CoroutineEventReceiver
import io.github.realyusufismail.ydwk.event.recieve.IEventReceiver
import io.github.realyusufismail.ydwk.rest.RestApiManager
import io.github.realyusufismail.ydwk.rest.impl.RestApiManagerImpl
import io.github.realyusufismail.ydwk.ws.WebSocketManager
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent
import io.github.realyusufismail.ydwk.ws.util.LoggedIn
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class YDWKImpl(private val client: OkHttpClient?) : YDWK {
    // logger
    val logger: Logger = LoggerFactory.getLogger(javaClass)
    val cache: Cache = PerpetualCache()
    val memberCache: MemberCache = MemberCacheImpl()
    private var token: String? = null

    override val objectNode: ObjectNode
        get() = JsonNodeFactory.instance.objectNode()

    override val objectMapper: ObjectMapper
        get() = ObjectMapper()

    override var webSocketManager: WebSocketManager? = null
        private set

    override fun shutdown() {
        webSocketManager?.shutdown()
    }

    override fun getGuild(id: String): Guild? {
        return cache[id, CacheType.GUILD] as Guild?
    }

    override fun getGuilds(): List<Guild> {
        return cache.values(CacheType.GUILD).map { it as Guild }
    }

    override val restApiManager: RestApiManager
        get() {
            val botToken = token ?: throw IllegalStateException("Bot token is not set")
            return if (client == null) {
                RestApiManagerImpl(botToken, this, OkHttpClient())
            } else {
                RestApiManagerImpl(botToken, this, client)
            }
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

    override val eventReceiver: IEventReceiver
        get() = CoroutineEventReceiver()

    override fun addEvent(vararg eventAdapters: CoroutineEventListener) {
        eventAdapters.forEach { eventReceiver.addEventReceiver(it) }
    }

    override fun removeEvent(vararg eventAdapters: CoroutineEventListener) {
        eventAdapters.forEach { eventReceiver.removeEventReceiver(it) }
    }

    fun handleEvent(event: Event) {
        eventReceiver.handleEvent(event)
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
}
