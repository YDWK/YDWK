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
package io.github.ydwk.yde.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.builders.message.IMessageCommandBuilder
import io.github.ydwk.yde.builders.slash.ISlashCommandBuilder
import io.github.ydwk.yde.builders.user.IUserCommandBuilder
import io.github.ydwk.yde.cache.*
import io.github.ydwk.yde.entities.Bot
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.builder.EntityBuilder
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.message.embed.builder.EmbedBuilder
import io.github.ydwk.yde.exceptions.ApplicationIdNotSetException
import io.github.ydwk.yde.impl.builders.message.IMessageCommandBuilderImpl
import io.github.ydwk.yde.impl.builders.slash.SlashBuilderImpl
import io.github.ydwk.yde.impl.builders.user.IUserCommandBuilderImpl
import io.github.ydwk.yde.impl.entities.builder.EntityBuilderImpl
import io.github.ydwk.yde.impl.entities.message.embed.builder.EmbedBuilderImpl
import io.github.ydwk.yde.impl.rest.RestApiManagerImpl
import io.github.ydwk.yde.rest.RestApiManager
import io.github.ydwk.yde.rest.methods.RestAPIMethodGetterImpl
import io.github.ydwk.yde.rest.methods.RestAPIMethodGetters
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.ThreadFactory
import java.time.Duration
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class YDEImpl(
    protected open var token: String? = null,
    open var applicationId: String? = null,
    protected open val client: OkHttpClient,
    protected open var guildIdList: MutableList<String> = mutableListOf(),
    override val githubRepositoryUrl: String,
    override val wrapperVersion: String,
) : YDE {
    val logger: Logger = LoggerFactory.getLogger(YDEImpl::class.java)

    private val allowedCache: MutableSet<CacheIds> = mutableSetOf()
    val cache: Cache = PerpetualCache(allowedCache, this)
    val memberCache: MemberCache = MemberCacheImpl(allowedCache, this)

    override val objectNode: ObjectNode
        get() = JsonNodeFactory.instance.objectNode()

    override val objectMapper: ObjectMapper
        get() = ObjectMapper()

    override val restApiManager: RestApiManager
        get() {
            val botToken = token ?: throw IllegalStateException("Bot token is not set")
            return RestApiManagerImpl(botToken, this, client)
        }

    override val restAPIMethodGetters: RestAPIMethodGetters
        get() = RestAPIMethodGetterImpl(this)

    override fun getMemberById(guildId: Long, userId: Long): Member? {
        return memberCache[guildId.toString(), userId.toString()]
    }

    override fun getMembers(): List<Member> {
        return memberCache.values().map { it }
    }

    override fun getUserById(id: Long): User? {
        return cache[id.toString(), CacheIds.USER].let { it as User? }
    }

    override fun getUsers(): List<User> {
        return cache.values(CacheIds.USER).map { it as User }
    }

    override fun getGuildById(id: String): Guild? {
        return cache[id, CacheIds.GUILD].let { it as Guild? }
    }

    override fun getGuilds(): List<Guild> {
        return cache.values(CacheIds.GUILD).map { it as Guild }
    }

    override fun getChannelById(id: Long): Channel? {
        return cache[id.toString(), CacheIds.CHANNEL].let { it as Channel? }
    }

    override fun getChannels(): List<Channel> {
        return cache.values(CacheIds.CHANNEL).map { it as Channel }
    }

    override val entityBuilder: EntityBuilder
        get() = EntityBuilderImpl(this)

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

    override val embedBuilder: EmbedBuilder
        get() = EmbedBuilderImpl(this)

    override fun triggerCacheTypeClear(cacheId: CacheIds, duration: Duration, repeat: Boolean) {
        cache.triggerCacheTypeClear(cacheId, duration, repeat)
    }

    override fun triggerCacheTypeClear(
        cacheIds: List<CacheIds>,
        duration: Duration,
        repeat: Boolean
    ) {
        cache.triggerCacheTypesClear(cacheIds, duration, repeat)
    }

    override fun triggerCacheClear(duration: Duration, repeat: Boolean) {
        cache.triggerCacheClear(duration, repeat)
    }

    override fun setGuildIds(vararg guildIds: String) {
        guildIds.forEach { this.guildIdList.add(it) }
    }

    override fun setAllowedCache(vararg cacheIds: CacheIds) {
        allowedCache.addAll(cacheIds.toSet())
    }

    override fun setDisallowedCache(vararg cacheIds: CacheIds) {
        allowedCache.removeAll(cacheIds.toSet())
    }

    override val bot: Bot? = null
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

    private val threadBuilder = ThreadFactoryBuilder()

    override val threadFactory: ThreadFactory = ThreadFactory(threadBuilder)

    override fun toString(): String {
        return EntityToStringBuilder(this, this).add("applicationId", applicationId).toString()
    }
}
