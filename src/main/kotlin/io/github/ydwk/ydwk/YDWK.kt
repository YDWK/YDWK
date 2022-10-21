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
import io.github.ydwk.ydwk.entities.*
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.slash.SlashBuilder
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.util.concurrent.CompletableFuture

interface YDWK {

    /**
     * Used to get ydwk as YDWKWebSocket.
     *
     * @return YDWKWebSocket
     */
    fun asYDWKWebSocket(): YDWKWebSocket

    /**
     * Used to get ydwk as YDWKRestClient
     *
     * @return YDWKRestClient
     */
    fun asYDWKRestClient(): YDWKRestClient

    /**
     * Create a json object
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
     * Gets the rest api manager.
     *
     * @return The rest api manager.
     */
    val restApiManager: RestApiManager

    /** adds or remove slash commands */
    val slashBuilder: SlashBuilder

    /** Sets the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: String)

    /** Sets the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: Long) =
        setGuildIds(*guildIds.map { it.toString() }.toTypedArray())

    /** Sets the guild ids for guild commands */
    fun setGuildIds(guildIds: MutableList<String>) = setGuildIds(*guildIds.toTypedArray())

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
     * UCreates a dm channel.
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
}
