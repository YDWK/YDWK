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
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.Application
import io.github.ydwk.ydwk.entities.Bot
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildTextChannel
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildVoiceChannel
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.ydwk.event.backend.event.GenericEvent
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.slash.SlashBuilder
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import java.util.concurrent.CompletableFuture

interface YDWK {

    /**
     * Creates a json object
     *
     * @return a new [ObjectNode]
     */
    val objectNode: ObjectNode

    /**
     * Used to parse json, i.e. convert plain text json to Jackson classes such as JsonNode.
     *
     * @return a new json [ObjectMapper]
     */
    val objectMapper: ObjectMapper

    /** This is where the websocket is created. */
    val webSocketManager: WebSocketManager?

    /**
     * Gets the properties of the bot.
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
     * Gets the properties of the application.
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
     * Used to indicated that bot has connected to the websocket.
     *
     * @return The [YDWK] instance.
     */
    val waitForConnection: YDWK

    /**
     * Waits for the READY gateway event to be received.
     *
     * @return The [YDWK] instance.
     */
    val waitForReady: YDWK

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
    fun shutdownAPI()

    /**
     * Gets a guild by its id.
     *
     * @param id The id of the guild.
     * @return The [Guild] object.
     */
    fun getGuild(id: Long): Guild? = getGuild(id.toString())

    /**
     * Gets a guild by its id.
     *
     * @param id The id of the guild.
     * @return The [Guild] object.
     */
    fun getGuild(id: String): Guild?

    /**
     * Gets all the guilds the bot is in.
     *
     * @return A list of guilds.
     */
    fun getGuilds(): List<Guild>

    /**
     * Gets the rest api manager.
     *
     * @return The rest api manager.
     */
    val restApiManager: RestApiManager

    /**
     * Gets the bot's uptime.
     *
     * @return The bot's uptime.
     */
    val uptime: Instant

    /** Used to add or remove slash commands */
    val slashBuilder: SlashBuilder

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
    fun setAllowedCache(cacheTypes: Set<CacheIds>) = setAllowedCache(*cacheTypes.toTypedArray())

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
    fun setDisallowedCache(cacheTypes: Set<CacheIds>) =
        setDisallowedCache(*cacheTypes.toTypedArray())

    /**
     * Gets a guild text channel by its id.
     *
     * @param id The id of the text channel.
     * @return The [GenericGuildTextChannel] object.
     */
    fun getGuildTextChannel(id: Long): GenericGuildTextChannel?

    /**
     * Gets a guild text channel by its id.
     *
     * @param id The id of the text channel.
     * @return The [GenericGuildTextChannel] object.
     */
    fun getGuildTextChannel(id: String): GenericGuildTextChannel? = getGuildTextChannel(id.toLong())

    /**
     * Gets all the guild text channels the bot is in.
     *
     * @return A list of [GenericGuildTextChannel] objects.
     */
    fun getGuildTextChannels(): List<GenericGuildTextChannel>

    /**
     * Gets a voice guild channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The [GenericGuildVoiceChannel] object.
     */
    fun getGuildVoiceChannel(id: Long): GenericGuildVoiceChannel?

    /**
     * Gets a voice guild channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The [GenericGuildVoiceChannel] object.
     */
    fun getGuildVoiceChannel(id: String): GenericGuildVoiceChannel? =
        getGuildVoiceChannel(id.toLong())

    /**
     * Gets all the guild voice channels the bot is in.
     *
     * @return A list of [GenericGuildVoiceChannel] objects.
     */
    fun getGuildVoiceChannels(): List<GenericGuildVoiceChannel>

    /**
     * Creates an embed.
     *
     * @return The [EmbedBuilder] object.
     */
    val embedBuilder: EmbedBuilder

    /**
     * Gets a category by its id.
     *
     * @param id The id of the category.
     * @return The [GuildCategory] object.
     */
    fun getCategory(id: Long): GuildCategory?

    /**
     * Gets a category by its id.
     *
     * @param id The id of the category.
     * @return The [GuildCategory] object.
     */
    fun getCategory(id: String): GuildCategory? = getCategory(id.toLong())

    /**
     * Gets all the categories the bot is in.
     *
     * @return A list of [GuildCategory] objects.
     */
    fun getCategories(): List<GuildCategory>

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
    fun getMember(guildId: Long, userId: Long): Member?

    /**
     * Gets a member by its id.
     *
     * @param guildId The id of the guild.
     * @param userId The id of the user.
     * @return The [Member] object.
     */
    fun getMember(guildId: String, userId: String): Member? =
        getMember(guildId.toLong(), userId.toLong())

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
    fun getUser(id: Long): User?

    /**
     * Gets a user by its id.
     *
     * @param id The id of the user.
     * @return The [User] object.
     */
    fun getUser(id: String): User? = getUser(id.toLong())

    /**
     * Gets all the users the bot can see.
     *
     * @return A list of [User] objects.
     */
    fun getUsers(): List<User>
}