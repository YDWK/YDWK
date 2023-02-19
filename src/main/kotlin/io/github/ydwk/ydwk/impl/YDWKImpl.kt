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
package io.github.ydwk.ydwk.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.ActivityPayload
import io.github.ydwk.ydwk.GateWayIntent
import io.github.ydwk.ydwk.UserStatus
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.builders.message.IMessageCommandBuilder
import io.github.ydwk.ydwk.builders.slash.ISlashCommandBuilder
import io.github.ydwk.ydwk.builders.user.IUserCommandBuilder
import io.github.ydwk.ydwk.cache.*
import io.github.ydwk.ydwk.entities.*
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.builder.EntityBuilder
import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.backend.managers.CoroutineEventManager
import io.github.ydwk.ydwk.evm.backend.managers.SampleEventManager
import io.github.ydwk.ydwk.exceptions.ApplicationIdNotSetException
import io.github.ydwk.ydwk.impl.builders.message.IMessageCommandBuilderImpl
import io.github.ydwk.ydwk.impl.builders.slash.SlashBuilderImpl
import io.github.ydwk.ydwk.impl.builders.user.IUserCommandBuilderImpl
import io.github.ydwk.ydwk.impl.entities.GuildImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.impl.entities.builder.EntityBuilderImpl
import io.github.ydwk.ydwk.impl.entities.channel.DmChannelImpl
import io.github.ydwk.ydwk.impl.entities.channel.guild.GuildChannelImpl
import io.github.ydwk.ydwk.impl.entities.message.embed.builder.EmbedBuilderImpl
import io.github.ydwk.ydwk.impl.rest.RestApiManagerImpl
import io.github.ydwk.ydwk.logging.*
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.ThreadFactory
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class YDWKImpl(
    private val client: OkHttpClient,
    private val simpleEventManager: SampleEventManager = SampleEventManager(),
    private val coroutineEventManager: CoroutineEventManager = CoroutineEventManager(),
    private val allowedCache: MutableSet<CacheIds> = mutableSetOf(),
    val logger: Logger = LoggerFactory.getLogger(YDWKImpl::class.java),
    val cache: Cache = PerpetualCache(allowedCache),
    val memberCache: MemberCache = MemberCacheImpl(allowedCache),
    private var token: String? = null,
    private var guildIdList: MutableList<String> = mutableListOf(),
    var applicationId: String? = null,
) : YDWK {
    override val defaultScheduledExecutorService: ScheduledExecutorService =
        Executors.newScheduledThreadPool(1)
    override val threadFactory: ThreadFactory
        get() = ThreadFactory

    override fun getChannelById(id: Long): Channel? {
        return cache[id.toString(), CacheIds.CHANNEL] as Channel?
    }

    override fun getChannels(): List<Channel> {
        return cache.values(CacheIds.CHANNEL).map { it as Channel }
    }

    override fun requestChannelById(id: Long): CompletableFuture<Channel> {
        return this.restApiManager
            .get(EndPoint.ChannelEndpoint.GET_CHANNEL, id.toString())
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    val channelType = ChannelType.fromInt(jsonBody["type"].asInt())
                    if (ChannelType.isGuildChannel(channelType)) {
                        GuildChannelImpl(this, jsonBody, jsonBody["id"].asLong())
                    } else {
                        DmChannelImpl(this, jsonBody, jsonBody["id"].asLong())
                    }
                }
            }
    }

    override fun requestGuildChannelById(id: Long, guildId: Long): CompletableFuture<GuildChannel> {
        return this.restApiManager
            .get(EndPoint.ChannelEndpoint.GET_CHANNEL, id.toString())
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    GuildChannelImpl(this, jsonBody, jsonBody["id"].asLong())
                }
            }
    }

    override fun requestGuildChannels(guildId: Long): CompletableFuture<List<GuildChannel>> {
        return this.restApiManager
            .get(EndPoint.GuildEndpoint.GET_GUILD_CHANNELS, guildId.toString())
            .execute { it ->
                val jsonBody = it.jsonBody
                jsonBody?.map { GuildChannelImpl(this, it, it["id"].asLong()) }
                    ?: throw IllegalStateException("json body is null")
            }
    }

    override val entityBuilder: EntityBuilder
        get() = EntityBuilderImpl(this)

    override val objectNode: ObjectNode
        get() = JsonNodeFactory.instance.objectNode()

    override val objectMapper: ObjectMapper
        get() = ObjectMapper()

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

    override fun getGuildById(id: String): Guild? {
        return cache[id, CacheIds.GUILD] as Guild?
    }

    override val guilds: List<Guild>
        get() = cache.values(CacheIds.GUILD).map { it as Guild }

    override val restApiManager: RestApiManager
        get() {
            val botToken = token ?: throw IllegalStateException("Bot token is not set")
            return RestApiManagerImpl(botToken, this, client)
        }
    override val uptime: Instant
        get() = webSocketManager!!.upTime ?: throw IllegalStateException("Bot is not logged in")

    override val slashBuilder: ISlashCommandBuilder
        get() =
            SlashBuilderImpl(
                this, guildIdList, applicationId ?: throw ApplicationIdNotSetException())
    override val userCommandBuilder: IUserCommandBuilder
        get() =
            IUserCommandBuilderImpl(
                this, guildIdList, applicationId ?: throw ApplicationIdNotSetException())

    override val messageCommandBuilder: IMessageCommandBuilder
        get() =
            IMessageCommandBuilderImpl(
                this, guildIdList, applicationId ?: throw ApplicationIdNotSetException())

    override fun setGuildIds(vararg guildIds: String) {
        guildIds.forEach { this.guildIdList.add(it) }
    }

    override fun setAllowedCache(vararg cacheTypes: CacheIds) {
        allowedCache.addAll(cacheTypes.toSet())
    }

    override fun setDisallowedCache(vararg cacheTypes: CacheIds) {
        allowedCache.removeAll(cacheTypes.toSet())
    }

    override val embedBuilder: EmbedBuilder
        get() = EmbedBuilderImpl(this)

    override fun createDmChannel(userId: Long): CompletableFuture<DmChannel> {
        return this.restApiManager
            .post(
                this.objectMapper
                    .createObjectNode()
                    .put("recipient_id", userId)
                    .toString()
                    .toRequestBody(),
                EndPoint.UserEndpoint.CREATE_DM)
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    DmChannelImpl(this, jsonBody, jsonBody["id"].asLong())
                }
            }
    }

    override fun getMemberById(guildId: Long, userId: Long): Member? {
        return memberCache[guildId.toString(), userId.toString()]
    }

    override fun getMembers(): List<Member> {
        return memberCache.values().map { it }
    }

    override fun getUserById(id: Long): User? {
        return cache[id.toString(), CacheIds.USER] as User?
    }

    override fun getUsers(): List<User> {
        return cache.values(CacheIds.USER).map { it as User }
    }

    override fun requestUser(id: Long): CompletableFuture<User> {
        return this.restApiManager.get(EndPoint.UserEndpoint.GET_USER, id.toString()).execute {
            val jsonBody = it.jsonBody
            if (jsonBody == null) {
                throw IllegalStateException("json body is null")
            } else {
                UserImpl(jsonBody, jsonBody["id"].asLong(), this)
            }
        }
    }

    override fun requestGuild(guildId: Long): CompletableFuture<Guild> {
        return this.restApiManager
            .get(EndPoint.GuildEndpoint.GET_GUILD, guildId.toString())
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    GuildImpl(this, jsonBody, jsonBody["id"].asLong())
                }
            }
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
        etfInsteadOfJson: Boolean
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
