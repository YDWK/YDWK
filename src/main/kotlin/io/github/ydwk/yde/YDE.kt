/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.builders.message.IMessageCommandBuilder
import io.github.ydwk.yde.builders.slash.ISlashCommandBuilder
import io.github.ydwk.yde.builders.user.IUserCommandBuilder
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.entities.Bot
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.builder.EntityBuilder
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.getter.guild.GuildChannelGetter
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.yde.rest.RestApiManager
import io.github.ydwk.yde.rest.methods.RestAPIMethodGetters
import io.github.ydwk.yde.util.Incubating
import io.github.ydwk.yde.util.ThreadFactory
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.CompletableDeferred

interface YDE {

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

    /**
     * The rest api manager.
     *
     * @return The rest api manager.
     */
    val restApiManager: RestApiManager

    /**
     * The place where all the RestAPI Methods are stored.
     *
     * @return The rest api methods.
     */
    val restAPIMethodGetters: RestAPIMethodGetters

    /**
     * The thread factory used by the bot.
     *
     * @return The thread factory.
     */
    val threadFactory: ThreadFactory

    /**
     * Creates a dm channel.
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: Long): CompletableDeferred<DmChannel> =
        restAPIMethodGetters.getUserRestAPIMethods().createDm(userId)

    /**
     * Creates a dm channel.
     *
     * @param userId The id of the user.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(userId: String): CompletableDeferred<DmChannel> =
        createDmChannel(userId.toLong())

    /**
     * Creates a dm channel.
     *
     * @param user The user who you want to create a dm channel with.
     * @return The [DmChannel] object.
     */
    fun createDmChannel(user: User): CompletableDeferred<DmChannel> = createDmChannel(user.id)

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
    fun requestUser(id: Long): CompletableDeferred<User> =
        restAPIMethodGetters.getUserRestAPIMethods().requestUser(id)

    /**
     * Requests a user using its id.
     *
     * @param id The id of the user.
     * @return The [CompletableFuture] object.
     */
    fun requestUser(id: String): CompletableDeferred<User> = requestUser(id.toLong())

    /**
     * Requests all the users the bot can see.
     *
     * @return The [CompletableFuture] object.
     */
    fun requestUsers(): CompletableDeferred<List<User>> =
        restAPIMethodGetters.getUserRestAPIMethods().requestUsers()

    /**
     * Requests a guild using its id.
     *
     * @param id The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuild(guildId: Long): CompletableDeferred<Guild> =
        restAPIMethodGetters.getGuildRestAPIMethods().requestedGuild(guildId)

    /**
     * Requests a guild using its id.
     *
     * @param id The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuild(guildId: String): CompletableDeferred<Guild> = requestGuild(guildId.toLong())

    /**
     * Requests all the guilds the bot is in.
     *
     * @return The [CompletableFuture] object.
     */
    fun requestGuilds(): CompletableDeferred<List<Guild>> =
        restAPIMethodGetters.getGuildRestAPIMethods().requestedGuilds()

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
     * @return A list of [Guild] objects.
     */
    fun getGuilds(): List<Guild>

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
    fun requestChannelById(id: Long): CompletableDeferred<Channel> =
        restAPIMethodGetters.getChannelRestAPIMethods().requestChannel(id)

    /**
     * Requests a channel using its id.
     *
     * @param id The id of the channel.
     * @return The [CompletableFuture] object.
     */
    fun requestChannelById(id: String): CompletableDeferred<Channel> =
        requestChannelById(id.toLong())

    /**
     * Requests a guild channel using its id.
     *
     * @param id The id of the channel.
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannelById(id: Long, guildId: Long): CompletableDeferred<GuildChannel> =
        restAPIMethodGetters.getChannelRestAPIMethods().requestGuildChannel(id, guildId)

    /**
     * Requests a guild channel using its id.
     *
     * @param id The id of the channel.
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannelById(id: String, guildId: String): CompletableDeferred<GuildChannel> =
        requestGuildChannelById(id.toLong(), guildId.toLong())

    /**
     * Requests a list of guild channels by the guild id.
     *
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannels(guildId: Long): CompletableDeferred<List<GuildChannel>> =
        restAPIMethodGetters.getChannelRestAPIMethods().requestGuildChannels(guildId)

    /**
     * Requests a list of guild channels by the guild id.
     *
     * @param guildId The id of the guild.
     * @return The [CompletableFuture] object.
     */
    fun requestGuildChannels(guildId: String): CompletableDeferred<List<GuildChannel>> =
        requestGuildChannels(guildId.toLong())

    /** Sets the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: String)

    /** Sets the guild ids for guild commands */
    fun setGuildIds(vararg guildIds: Long) =
        setGuildIds(*guildIds.map { it.toString() }.toTypedArray())

    /** Sets the guild ids for guild commands */
    fun setGuildIds(guildIds: MutableList<String>) = setGuildIds(*guildIds.toTypedArray())

    /**
     * Sets the allowed cache ids.
     *
     * @param cacheIds The cache ics to be allowed.
     */
    fun setAllowedCache(vararg cacheIds: CacheIds)

    /**
     * Sets the allowed cache ids.
     *
     * @param cacheIds The cache ids to be allowed.
     */
    fun setAllowedCache(cacheIds: MutableList<CacheIds>) = setAllowedCache(*cacheIds.toTypedArray())

    /**
     * Sets the disallowed cache ids.
     *
     * @param cacheIds The cache ids to be disallowed.
     */
    fun setDisallowedCache(vararg cacheIds: CacheIds)

    /**
     * Sets the disallowed cache ids.
     *
     * @param cacheIds The cache ids to be disallowed.
     */
    fun setDisallowedCache(cacheIds: MutableList<CacheIds>) =
        setDisallowedCache(*cacheIds.toTypedArray())

    /**
     * Triggers a thread to clear a certain cache type after a certain amount of time
     *
     * @param cacheId The id of the cache
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    @Incubating
    fun triggerCacheTypeClear(cacheId: CacheIds, duration: Duration, repeat: Boolean = true)

    /**
     * Triggers a thread to clear a certain cache type after a certain amount of time
     *
     * @param cacheId The id of the cache
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    @Incubating
    fun triggerCacheTypeClear(cacheId: CacheIds, duration: Long, repeat: Boolean = true) =
        triggerCacheTypeClear(cacheId, Duration.ofMillis(duration), repeat)

    /**
     * Triggers a thread to clear a list of cache types after a certain amount of time
     *
     * @param cacheIds The ids of the caches
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    @Incubating
    fun triggerCacheTypeClear(cacheIds: List<CacheIds>, duration: Duration, repeat: Boolean = true)

    /**
     * Triggers a thread to clear a list of cache types after a certain amount of time
     *
     * @param cacheIds The ids of the caches
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    @Incubating
    fun triggerCacheTypeClear(cacheIds: List<CacheIds>, duration: Long, repeat: Boolean = true) =
        triggerCacheTypeClear(cacheIds, Duration.ofMillis(duration), repeat)

    /**
     * Triggers a thread to clear the entire cache after a certain amount of time
     *
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    @Incubating fun triggerCacheClear(duration: Duration, repeat: Boolean = true)

    /**
     * Triggers a thread to clear the entire cache after a certain amount of time
     *
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    @Incubating
    fun triggerCacheClear(duration: Long, repeat: Boolean = true) =
        triggerCacheClear(Duration.ofMillis(duration), repeat)

    /**
     * The github repository url.
     *
     * @return The url.
     */
    val githubRepositoryUrl: String

    /**
     * The wrapper version.
     *
     * @return The version.
     */
    val wrapperVersion: String

    /**
     * The properties of the bot.
     *
     * @return the [Bot] object
     */
    val bot: Bot?

    /**
     * The entity builder.
     *
     * @return The [EntityBuilder] object.
     */
    val entityBuilder: EntityBuilder

    /**
     * Adds or removes slash commands.
     *
     * @return The [ISlashCommandBuilder] object.
     */
    val slashBuilder: ISlashCommandBuilder

    /**
     * Adds or removes user commands.
     *
     * @return The [IUserCommandBuilder] object.
     */
    val userCommandBuilder: IUserCommandBuilder

    /**
     * Adds or removes message commands.
     *
     * @return The [IMessageCommandBuilder] object.
     */
    val messageCommandBuilder: IMessageCommandBuilder

    /**
     * Used to build embeds.
     *
     * @return The [EmbedBuilder] object.
     */
    val embedBuilder: EmbedBuilder

    /**
     * Overrides the custom to string method.
     *
     * @return The string representation of the object.
     */
    override fun toString(): String
}
