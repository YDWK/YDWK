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
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.util.exception.LoginException
import javax.annotation.CheckReturnValue
import okhttp3.OkHttpClient

object BotBuilder {
    private var token: String? = null
    private var okHttpClient: OkHttpClient? = null
    private var intents: MutableList<GateWayIntent> = mutableListOf()
    private var allowedCache: MutableList<CacheIds> = mutableListOf()
    private var disallowedCache: MutableList<CacheIds> = mutableListOf()
    private var guildIds = mutableListOf<String>()
    private var userStatus: UserStatus? = null
    private var activity: ActivityPayload? = null
    private var etfInsteadOfJson: Boolean = false

    /**
     * Creates a new bot builder with the default settings
     *
     * @param token The bot token
     * @return The bot builder
     */
    @CheckReturnValue
    fun createDefaultBot(token: String): BotBuilder {
        this.token = token
        intents.addAll(GateWayIntent.getDefaultIntents())
        allowedCache.addAll(CacheIds.getDefaultCache())
        return this
    }

    /**
     * Creates a new bot builder with the default settings
     *
     * @param token The bot token
     * @return The bot builder
     */
    @CheckReturnValue
    fun createDefaultBot(token: Long): BotBuilder {
        return createDefaultBot(token.toString())
    }

    /**
     * Creates a custom bot builder
     *
     * @param token The bot token
     * @param intents The intents to use
     * @return The bot builder
     */
    @CheckReturnValue
    fun createCustomBot(token: String, intents: MutableList<GateWayIntent>): BotBuilder {
        this.token = token
        this.intents.addAll(intents)
        return this
    }

    /**
     * Creates a custom bot builder
     *
     * @param token The bot token
     * @param intents The intents to use
     * @return The bot builder
     */
    @CheckReturnValue
    fun createCustomBot(token: Long, intents: MutableList<GateWayIntent>): BotBuilder {
        return createCustomBot(token.toString(), intents)
    }

    /**
     * Sets a custom OkHttpClient
     *
     * @param okHttpClient The OkHttpClient to use
     * @return The bot builder
     */
    fun setOkHttpClient(okHttpClient: OkHttpClient): BotBuilder {
        this.okHttpClient = okHttpClient
        return this
    }

    /**
     * Adds intents to the bot
     *
     * @param intents The intents to use
     * @param clearIntent Whether to override the current intents
     * @return The bot builder
     */
    fun setIntents(intents: MutableList<GateWayIntent>, clearIntent: Boolean): BotBuilder {
        if (clearIntent) {
            this.intents.clear()
        }
        this.intents.addAll(intents)
        return this
    }

    /**
     * Adds intents to the bot
     *
     * @param intents The intents to use
     * @return The bot builder
     */
    fun setIntents(intents: MutableList<GateWayIntent>): BotBuilder {
        return setIntents(intents, true)
    }

    /**
     * Adds cache to the bot
     *
     * @param allowedCache The cache to use
     * @param clearCache Whether to override the current cache
     * @return The bot builder
     */
    @SuppressWarnings("WeakerAccess")
    fun setAllowedCache(allowedCache: MutableList<CacheIds>, clearCache: Boolean): BotBuilder {
        if (clearCache) {
            this.allowedCache.clear()
        }
        this.allowedCache.addAll(allowedCache)
        return this
    }

    /**
     * Adds cache to the bot
     *
     * @param allowedCache The cache to use
     * @return The bot builder
     */
    fun setAllowedCache(allowedCache: MutableList<CacheIds>): BotBuilder {
        return setAllowedCache(allowedCache, false)
    }

    /**
     * Removes cache from the bot
     *
     * @param disallowedCache The cache to remove
     * @param clearCache Whether to override the current cache
     * @return The bot builder
     */
    @SuppressWarnings("WeakerAccess")
    fun setDisallowedCache(
        disallowedCache: MutableList<CacheIds>,
        clearCache: Boolean,
    ): BotBuilder {
        if (clearCache) {

            this.disallowedCache.clear()
        }
        this.disallowedCache.addAll(disallowedCache)
        return this
    }

    /**
     * Removes cache from the bot
     *
     * @param disallowedCache The cache to remove
     * @return The bot builder
     */
    fun setDisallowedCache(disallowedCache: MutableList<CacheIds>): BotBuilder {
        return setDisallowedCache(disallowedCache, false)
    }

    /**
     * Sets the guild ids to use which will be used for example with guild only commands
     *
     * @param guildIds The guild ids to use
     * @return The bot builder
     */
    fun setGuildIds(guildIds: MutableList<String>): BotBuilder {
        this.guildIds.addAll(guildIds)
        return this
    }

    /**
     * Sets the user status to use
     *
     * @param userStatus The user status to use
     * @return The bot builder
     */
    fun setUserStatus(userStatus: UserStatus): BotBuilder {
        this.userStatus = userStatus
        return this
    }

    /**
     * Sets the activity of the bot
     *
     * @param activity use [Activity] as [ActivityPayload] is just returned by the function.
     * @return The bot builder
     */
    fun setActivity(activity: ActivityPayload): BotBuilder {
        this.activity = activity
        return this
    }

    /**
     * Uses ETF instead of JSON for the websocket
     *
     * @param etfInsteadOfJson Whether to use ETF instead of JSON
     * @return The bot builder
     */
    fun setETFInsteadOfJson(etfInsteadOfJson: Boolean): BotBuilder {
        this.etfInsteadOfJson = etfInsteadOfJson
        return this
    }

    /**
     * Builds the bot
     *
     * @return The bot
     */
    fun build(): YDWK {
        val ydwk = YDWKImpl(okHttpClient ?: OkHttpClient())

        if (token.isNullOrEmpty()) {
            throw LoginException("Token cannot be null or empty")
        }

        ydwk.setWebSocketManager(token!!, intents, userStatus, activity, etfInsteadOfJson)
        ydwk.setAllowedCache(allowedCache)
        ydwk.setDisallowedCache(disallowedCache)
        return ydwk
    }
}
