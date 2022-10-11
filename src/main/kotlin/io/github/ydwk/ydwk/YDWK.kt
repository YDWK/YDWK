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
import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.ydwk.event.backend.event.GenericEvent
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.slash.SlashBuilder
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant

interface YDWK {

    /**
     * Used to create a json object
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
     * Used to get the properties of the bot.
     *
     * @return the [Bot] object
     */
    val bot: Bot?

    /**
     * Used to get some application properties sent by discord's Ready event.
     *
     * @return the [PartialApplication] object
     */
    var partialApplication: PartialApplication?

    /**
     * Used to get the properties of the application.
     *
     * @return the [Application] object
     */
    val application: Application?

    /**
     * Used to get information about when the bot logged in.
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
     * Used to wait for the READY gateway event to be received.
     *
     * @return The [YDWK] instance.
     */
    val waitForReady: YDWK

    /**
     * Used to add an event listener.
     *
     * @param eventListeners The event listeners to be added.
     */
    fun addEvent(vararg eventListeners: Any)

    /**
     * Used to remove an event listener.
     *
     * @param eventListeners The event listeners to be removed.
     */
    fun removeEvent(vararg eventListeners: Any)

    /**
     * Used to emit an event
     *
     * @param event The event to be emitted.
     */
    fun emitEvent(event: GenericEvent)

    /** Used to shut down the websocket manager */
    fun shutdownAPI()

    /**
     * Used to get a guild by its id.
     *
     * @param id The id of the guild.
     * @return The [Guild] object.
     */
    fun getGuild(id: Long): Guild? = getGuild(id.toString())

    /**
     * Used to get a guild by its id.
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

    /** Used to set the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: String)

    /** Used to set the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: Long) =
        setGuildIds(*guildIds.map { it.toString() }.toTypedArray())

    /** Used to set the guild ids for guild commands */
    fun setGuildIds(guildIds: MutableList<String>) = setGuildIds(*guildIds.toTypedArray())

    /**
     * Used to set the allowed cache types.
     *
     * @param cacheTypes The cache types to be allowed.
     */
    fun setAllowedCache(vararg cacheTypes: CacheIds)

    /**
     * Used to set the allowed cache types.
     *
     * @param cacheTypes The cache types to be allowed.
     */
    fun setAllowedCache(cacheTypes: Set<CacheIds>) = setAllowedCache(*cacheTypes.toTypedArray())

    /**
     * Used to set the disallowed cache types.
     *
     * @param cacheTypes The cache types to be disallowed.
     */
    fun setDisallowedCache(vararg cacheTypes: CacheIds)

    /**
     * Used to set the disallowed cache types.
     *
     * @param cacheTypes The cache types to be disallowed.
     */
    fun setDisallowedCache(cacheTypes: Set<CacheIds>) =
        setDisallowedCache(*cacheTypes.toTypedArray())

    /**
     * Used to get a channel by its id.
     *
     * @param id The id of the channel.
     * @return The [Channel] object.
     */
    fun getChannel(asLong: Long): Channel?

    /**
     * Used to get a channel by its id.
     *
     * @param id The id of the channel.
     * @return The [Channel] object.
     */
    fun getChannel(asString: String): Channel? = getChannel(asString.toLong())

    /**
     * Used to get all the channels the bot is in.
     *
     * @return A list of [Channel] objects.
     */
    fun getChannels(): List<Channel>

    /**
     * Used to create an embed.
     *
     * @return The [EmbedBuilder] object.
     */
    val embedBuilder: EmbedBuilder
}
