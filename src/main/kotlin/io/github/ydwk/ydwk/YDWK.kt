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
package io.github.ydwk.ydwk

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.builders.message.IMessageCommandBuilder
import io.github.ydwk.ydwk.builders.slash.SlashBuilder
import io.github.ydwk.ydwk.builders.user.IUserCommandBuilder
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.*
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.builder.EntityBuilder
import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.entities.channel.getter.guild.GuildChannelGetter
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.util.Incubating
import io.github.ydwk.ydwk.util.ThreadFactory
import io.github.ydwk.ydwk.voice.VoiceConnection
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ScheduledExecutorService

/**
 * The main class of the YDWK library. This class is used to interact with the Discord API. Also
 * contains many things such as the embed builder for example.
 */
interface YDWK {

    /**
     * Creates a json object
     *
     * @return a new [ObjectNode]
     */
    val objectNode: ObjectNode

    /**
     * Parses json, i.e. convert plain text json to Jackson classes such as JsonNode.
     *
     * @return a new json [ObjectMapper]
     */
    val objectMapper: ObjectMapper

    /** This is where the websocket is created. */
    val webSocketManager: WebSocketManager?

    /**
     * The properties of the bot.
     *
     * @return the [Bot] object
     */
    val bot: Bot?

    /**
     * Gets some application properties sent by discord's Ready event.
     *
     * @return the [PartialApplication] object
     */
    var partialApplication: PartialApplication?

    /**
     * The properties of the application.
     *
     * @return the [Application] object
     */
    val application: Application?

    /**
     * Gets information about when the bot logged in.
     *
     * @return the [LoggedIn] object
     */
    val loggedInStatus: LoggedIn?

    /**
     * A suspend function that waits for the READY gateway event to be received.
     *
     * @return The [YDWK] instance.
     */
    suspend fun awaitReady(): YDWK

    /**
     * Adds an event listener.
     *
     * @param eventListeners The event listeners to be added.
     */
    fun addEvent(vararg eventListeners: Any)

    /**
     * Removes an event listener.
     *
     * @param eventListeners The event listeners to be removed.
     */
    fun removeEvent(vararg eventListeners: Any)

    /**
     * Emits an event
     *
     * @param event The event to be emitted.
     */
    fun emitEvent(event: GenericEvent)

    /** Shuts down the websocket manager */
    @Incubating fun shutdownAPI()

    /**
     * Gets a guild by its id.
     *
     * @param id The id of the guild.
     * @return The [Guild] object.
     */
    fun getGuildById(id: Long): Guild? = getGuildById(id.toString())

    /**
     * Gets a guild by its id.
     *
     * @param id The id of the guild.
     * @return The [Guild] object.
     */
    fun getGuildById(id: String): Guild?

    /**
     * Gets all the guilds the bot is in.
     *
     * @return A list of guilds.
     */
    val guilds: List<Guild>

    /**
     * The rest api manager.
     *
     * @return The rest api manager.
     */
    val restApiManager: RestApiManager

    /**
     * The bot's uptime.
     *
     * @return The bot's uptime.
     */
    val uptime: Instant

    /** Adds or removes slash commands */
    val slashBuilder: SlashBuilder

    /** Adds or removes user commands. */
    val userCommandBuilder: IUserCommandBuilder

    /** Adds or removes message commands. */
    val messageCommandBuilder: IMessageCommandBuilder

    /** Sets the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: String)

    /** Sets the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: Long) =
        setGuildIds(*guildIds.map { it.toString() }.toTypedArray())

    /** Sets the guild ids for guild commands */
    fun setGuildIds(guildIds: MutableList<String>) = setGuildIds(*guildIds.toTypedArray())

    /**
     * Sets the allowed cache types.
     *
     * @param cacheTypes The cache types to be allowed.
     */
    fun setAllowedCache(vararg cacheTypes: CacheIds)

    /**
     * Sets the allowed cache types.
     *
     * @param cacheTypes The cache types to be allowed.
     */
    fun setAllowedCache(cacheTypes: MutableList<CacheIds>) =
        setAllowedCache(*cacheTypes.toTypedArray())

    /**
     * Sets the disallowed cache types.
     *
     * @param cacheTypes The cache types to be disallowed.
     */
    fun setDisallowedCache(vararg cacheTypes: CacheIds)

    /**
     * Sets the disallowed cache types.
     *
     * @param cacheTypes The cache types to be disallowed.
     */
    fun setDisallowedCache(cacheTypes: MutableList<CacheIds>) =
        setDisallowedCache(*cacheTypes.toTypedArray())

    /**
     * Creates an embed.
     *
     * @return The [EmbedBuilder] object.
     */
    val embedBuilder: EmbedBuilder

    /**
     * Creates a dm channel.
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: Long): CompletableFuture<DmChannel>

    /**
     * Creates a dm channel.
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: String): CompletableFuture<DmChannel> =
        createDmChannel(userId.toLong())

    /**
     * Creates a dm channel.
     *
     * @param user The user who you want to create a dm channel with.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(user: User): CompletableFuture<DmChannel> = createDmChannel(user.id)

    /**
     * Gets a member by its id.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @return The [Member] object.
     */
    fun getMemberById(guildId: Long, userId: Long): Member?

    /**
     * Gets a member by its id.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @return The [Member] object.
     */
    fun getMemberById(guildId: String, userId: String): Member? =
        getMemberById(guildId.toLong(), userId.toLong())

    /**
     * Gets all the members in all the guilds the bot is in.
     *
     * @return A list of [Member] objects.
     */
    fun getMembers(): List<Member>

    /**
     * Gets a user by its id.
     *
     * @param id The id of the user.
     * @return The [User] object.
     */
    fun getUserById(id: Long): User?

    /**
     * Gets a user by its id.
     *
     * @param id The id of the user.
     * @return The [User] object.
     */
    fun getUserById(id: String): User? = getUserById(id.toLong())

    /**
     * Gets all the users the bot can see.
     *
     * @return A list of [User] objects.
     */
    fun getUsers(): List<User>

    /**
     * Requests a user using its id.
     *
     * @param id The id of the user.
     * @return The [CompletableFuture] object.
     */
    fun requestUser(id: Long): CompletableFuture<User>

    /**
     * Requests a user using its id.
     *
     * @param id The id of the user.
     * @return The [CompletableFuture] object.
     */
    fun requestUser(id: String): CompletableFuture<User> = requestUser(id.toLong())

    /**
     * Requests a guild using its id.
     *
     * @param id The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuild(guildId: Long): CompletableFuture<Guild>

    /**
     * Requests a guild using its id.
     *
     * @param id The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuild(guildId: String): CompletableFuture<Guild> = requestGuild(guildId.toLong())

    /**
     * Sets the voice connection.
     *
     * @param guildId The id of the guild.
     * @param voiceConnection The voice connection.
     */
    fun setVoiceConnection(guildId: Long, voiceConnection: VoiceConnection)

    /**
     * Sets the voice connection.
     *
     * @param guildId The id of the guild.
     * @param voiceConnection The voice connection.
     */
    fun setVoiceConnection(guildId: String, voiceConnection: VoiceConnection) =
        setVoiceConnection(guildId.toLong(), voiceConnection)

    /**
     * The voice connection.
     *
     * @param guildId The id of the guild.
     * @return The [VoiceConnection] object.
     */
    fun getVoiceConnectionById(guildId: Long): VoiceConnection?

    /**
     * Removes the voice connection.
     *
     * @param guildId The id of the guild.
     */
    fun removeVoiceConnectionById(guildId: Long)

    /**
     * Sets the pending voice connection.
     *
     * @param guildId The id of the guild.
     * @param voiceConnection The voice connection.
     * @return The [VoiceConnection] object.
     */
    fun setPendingVoiceConnection(
        guildId: Long,
        voiceConnection: VoiceConnection,
    )

    /**
     * The pending voice connection.
     *
     * @param guildId The id of the guild.
     * @return The [VoiceConnection] object.
     */
    fun getPendingVoiceConnectionById(guildId: Long): VoiceConnection?

    /**
     * Removes the pending voice connection.
     *
     * @param guildId The id of the guild.
     */
    fun removePendingVoiceConnectionById(guildId: Long)

    /**
     * The default ScheduledExecutorService.
     *
     * @return The [ScheduledExecutorService] object.
     */
    val defaultScheduledExecutorService: ScheduledExecutorService

    /**
     * The thread factory.
     *
     * @return The [ThreadFactory] object.
     */
    val threadFactory: ThreadFactory

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The [Channel] object.
     */
    fun getChannelById(id: Long): Channel?

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The [Channel] object.
     */
    fun getChannelById(id: String): Channel? = getChannelById(id.toLong())

    /**
     * Gets all the channels the bot can see.
     *
     * @return A list of [Channel] objects.
     */
    fun getChannels(): List<Channel>

    /**
     * Gets a guild channel by its id.
     *
     * @param id The id of the guild channel.
     * @return The [GuildChannel] object.
     */
    fun getGuildChannelById(id: Long): GuildChannel? =
        getChannelById(id)?.channelGetter?.asGuildChannel()

    /**
     * Gets a guild channel by its id.
     *
     * @param id The id of the guild channel.
     * @return The [GuildChannel] object.
     */
    fun getGuildChannelById(id: String): GuildChannel? = getGuildChannelById(id.toLong())

    /**
     * Gets all the guild channels the bot can see.
     *
     * @return A list of [GuildChannel] objects.
     */
    fun getGuildChannels(): List<GuildChannel> =
        getChannels().mapNotNull { it.channelGetter.asGuildChannel() }

    /**
     * Gets guild channels by their ids.
     *
     * @param id The id of the guild channel.
     * @return The [GuildChannelGetter] object.
     */
    fun getGuildChannelGetterById(id: Long): GuildChannelGetter? =
        getGuildChannelById(id)?.guildChannelGetter

    /**
     * Gets guild channels by their ids.
     *
     * @param id The id of the guild channel.
     * @return The [GuildChannelGetter] object.
     */
    fun getGuildChannelGetterById(id: String): GuildChannelGetter? =
        getGuildChannelGetterById(id.toLong())

    /**
     * Requests a channel using its id.
     *
     * @param id The id of the channel.
     * @return The [CompletableFuture] object.
     */
    fun requestChannelById(id: Long): CompletableFuture<Channel>

    /**
     * Requests a channel using its id.
     *
     * @param id The id of the channel.
     * @return The [CompletableFuture] object.
     */
    fun requestChannelById(id: String): CompletableFuture<Channel> = requestChannelById(id.toLong())

    /**
     * Requests a guild channel using its id.
     *
     * @param id The id of the channel.
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannelById(id: Long, guildId: Long): CompletableFuture<GuildChannel>

    /**
     * Requests a guild channel using its id.
     *
     * @param id The id of the channel.
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannelById(id: String, guildId: String): CompletableFuture<GuildChannel> =
        requestGuildChannelById(id.toLong(), guildId.toLong())

    /**
     * Requests a list of guild channels by the guild id.
     *
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannels(guildId: Long): CompletableFuture<List<GuildChannel>>

    /**
     * Requests a list of guild channels by the guild id.
     *
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannels(guildId: String): CompletableFuture<List<GuildChannel>> =
        requestGuildChannels(guildId.toLong())

    /**
     * The entity builder.
     *
     * @return The [EntityBuilder] object.
     */
    @get:Incubating val entityBuilder: EntityBuilder

    /**
     * Overrides the custom to string method.
     *
     * @return The string representation of the object.
     */
    override fun toString(): String
}
