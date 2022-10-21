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

import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.channel.VoiceChannel
import io.github.ydwk.ydwk.entities.channel.guild.Category
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.event.backend.event.GenericEvent
import io.github.ydwk.ydwk.ws.WebSocketManager
import java.time.Instant

interface YDWKWebSocket : YDWK {

    /** This is where the websocket is created. */
    val webSocketManager: WebSocketManager?

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
     * Gets a text channel by its id.
     *
     * @param id The id of the text channel.
     * @return The [TextChannel] object.
     */
    fun getTextChannel(id: Long): TextChannel?

    /**
     * Gets a text channel by its id.
     *
     * @param id The id of the text channel.
     * @return The [TextChannel] object.
     */
    fun getTextChannel(id: String): TextChannel? = getTextChannel(id.toLong())

    /**
     * Gets all the text channels the bot is in.
     *
     * @return A list of [TextChannel] objects.
     */
    fun getTextChannels(): List<TextChannel>

    /**
     * Gets a voice channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The [VoiceChannel] object.
     */
    fun getVoiceChannel(id: Long): VoiceChannel?

    /**
     * Gets a voice channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The [VoiceChannel] object.
     */
    fun getVoiceChannel(id: String): VoiceChannel? = getVoiceChannel(id.toLong())

    /**
     * Gets all the voice channels the bot is in.
     *
     * @return A list of [VoiceChannel] objects.
     */
    fun getVoiceChannels(): List<VoiceChannel>

    /**
     * Gets a category by its id.
     *
     * @param id The id of the category.
     * @return The [Category] object.
     */
    fun getCategory(id: Long): Category?

    /**
     * Gets a category by its id.
     *
     * @param id The id of the category.
     * @return The [Category] object.
     */
    fun getCategory(id: String): Category? = getCategory(id.toLong())

    /**
     * Gets all the categories the bot is in.
     *
     * @return A list of [Category] objects.
     */
    fun getCategories(): List<Category>

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

    /**
     * Indicates that bot has connected to the websocket.
     *
     * @return The [YDWK] instance.
     */
    val waitForConnection: YDWKWebSocket

    /**
     * Waits for the READY gateway event to be received.
     *
     * @return The [YDWK] instance.
     */
    val waitForReady: YDWKWebSocket

    /**
     * adds an event listener.
     *
     * @param eventListeners The event listeners to be added.
     */
    fun addEvent(vararg eventListeners: Any)

    /**
     * removes an event listener.
     *
     * @param eventListeners The event listeners to be removed.
     */
    fun removeEvent(vararg eventListeners: Any)

    /**
     * emits an event
     *
     * @param event The event to be emitted.
     */
    fun emitEvent(event: GenericEvent)

    /** shut's down the websocket manager */
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
     * Gets the bot's uptime.
     *
     * @return The bot's uptime.
     */
    val uptime: Instant
}
