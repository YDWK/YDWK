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

import io.github.ydwk.ydwk.YDWKWebSocket
import io.github.ydwk.ydwk.cache.*
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.channel.VoiceChannel
import io.github.ydwk.ydwk.entities.channel.guild.Category
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.event.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.event.backend.event.GenericEvent
import io.github.ydwk.ydwk.event.backend.event.IEventListener
import io.github.ydwk.ydwk.event.backend.managers.CoroutineEventManager
import io.github.ydwk.ydwk.event.backend.managers.SampleEventManager
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.GateWayIntent
import java.time.Instant
import java.util.*
import kotlin.random.Random
import okhttp3.OkHttpClient

class YDWKWebSocketImpl(
    val OkHttpClient: OkHttpClient,
    private val simpleEventManager: SampleEventManager = SampleEventManager(),
    private val coroutineEventManager: CoroutineEventManager = CoroutineEventManager(),
    private val allowedCache: MutableSet<CacheIds> = mutableSetOf(),
    val cache: Cache = PerpetualCache(allowedCache),
    val memberCache: MemberCache = MemberCacheImpl(allowedCache)
) : YDWKWebSocket, YDWKImpl(OkHttpClient) {
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

    override fun getGuild(id: String): Guild? {
        return cache[id, CacheIds.GUILD] as Guild?
    }

    override fun getGuilds(): List<Guild> {
        return cache.values(CacheIds.GUILD).map { it as Guild }
    }

    override fun getCategory(id: Long): Category? {
        return cache[id.toString(), CacheIds.CATEGORY] as Category?
    }

    override fun getCategories(): List<Category> {
        return cache.values(CacheIds.CATEGORY).map { it as Category }
    }

    override fun getMember(guildId: Long, userId: Long): Member? {
        return memberCache[userId.toString(), guildId.toString()] as Member?
    }

    override fun getMembers(): List<Member> {
        return memberCache.values(CacheIds.MEMBER).map { it as Member }
    }

    override fun getUser(id: Long): User? {
        return cache[id.toString(), CacheIds.USER] as User?
    }

    override fun getUsers(): List<User> {
        return cache.values(CacheIds.USER).map { it as User }
    }

    @get:Synchronized
    override val waitForConnection: YDWKWebSocket
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
    override val waitForReady: YDWKWebSocket
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
     * Starts the websocket manager
     *
     * @param token The token of the bot which is used to authenticate the bot.
     * @param intents The gateway intent which will decide what events are sent by discord.
     */
    fun setWebSocketManager(token: String, intents: List<GateWayIntent>) {
        var ws: WebSocketManager? = null
        ws = WebSocketManager(this, token, intents)
        this.webSocketManager = ws.connect()
        this.timer(Timer(), ws)
        this.token = token
    }

    @Synchronized
    private fun timer(timer: Timer, ws: WebSocketManager) {
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    ws.sendHeartbeat()
                }
            },
            0,
            14 * 24 * 60 * 60 * 1000)
    }

    override val uptime: Instant
        get() = webSocketManager!!.upTime ?: throw IllegalStateException("Bot is not logged in")
}
