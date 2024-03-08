/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.ydwk

import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.util.exception.LoginException
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher

internal class BotBuilder
private constructor(
    private val token: String,
    private val httpClient: HttpClient = HttpClient(),
    private val intents: List<GateWayIntent> = GateWayIntent.getDefaultIntents(),
    private val allowedCache: Set<CacheIds> = CacheIds.getDefaultCache(),
    private val disallowedCache: Set<CacheIds> = emptySet(),
    private val guildIds: List<String> = emptyList(),
    private val userStatus: UserStatus? = null,
    private val activity: ActivityPayload? = null,
    private val etfInsteadOfJson: Boolean = false,
    private val enableShutDownHook: Boolean = true,
    private val dispatcher: CoroutineDispatcher? = null
) {
    companion object {
        fun buildBot(token: String) = Builder(token)
    }

    class Builder(private val token: String) {
        private var httpClient: HttpClient = HttpClient()
        private var intents: MutableList<GateWayIntent> = mutableListOf()
        private var allowedCache: MutableSet<CacheIds> = mutableSetOf()
        private var disallowedCache: MutableSet<CacheIds> = mutableSetOf()
        private var guildIds: MutableList<String> = mutableListOf()
        private var userStatus: UserStatus? = null
        private var activity: ActivityPayload? = null
        private var etfInsteadOfJson: Boolean = false
        private var enableShutDownHook: Boolean = true
        private var dispatcher: CoroutineDispatcher? = null

        fun httpClient(httpClient: HttpClient) = apply { this.httpClient = httpClient }

        fun intents(intents: List<GateWayIntent>, clearIntent: Boolean = true) = apply {
            if (clearIntent) {
                this.intents.clear()
            }
            this.intents.addAll(intents)
        }

        fun allowedCache(allowedCache: Set<CacheIds>, clearCache: Boolean = false) = apply {
            if (clearCache) {
                this.allowedCache.clear()
            }
            this.allowedCache.addAll(allowedCache)
        }

        fun disallowedCache(disallowedCache: Set<CacheIds>, clearCache: Boolean = false) = apply {
            if (clearCache) {
                this.disallowedCache.clear()
            }
            this.disallowedCache.addAll(disallowedCache)
        }

        fun guildIds(guildIds: List<String>) = apply { this.guildIds.addAll(guildIds) }

        fun userStatus(userStatus: UserStatus?) = apply { this.userStatus = userStatus }

        fun activity(activity: ActivityPayload?) = apply { this.activity = activity }

        fun etfInsteadOfJson(etfInsteadOfJson: Boolean) = apply {
            this.etfInsteadOfJson = etfInsteadOfJson
        }

        fun enableShutDownHook(enableShutDownHook: Boolean) = apply {
            this.enableShutDownHook = enableShutDownHook
        }

        fun dispatcher(dispatcher: CoroutineDispatcher?) = apply { this.dispatcher = dispatcher }

        fun build(): BotBuilder {
            return BotBuilder(
                token = token,
                httpClient = httpClient,
                intents = intents,
                allowedCache = allowedCache,
                disallowedCache = disallowedCache,
                guildIds = guildIds,
                userStatus = userStatus,
                activity = activity,
                etfInsteadOfJson = etfInsteadOfJson,
                enableShutDownHook = enableShutDownHook,
                dispatcher = dispatcher)
        }
    }

    fun buildYDWK(): YDWK {
        val ydwk = YDWKImpl(httpClient)
        val maxLength = 100 // Maximum length allowed for the JWT token
        val minLength = 50 // Minimum length allowed for the JWT token

        when {
            token.isEmpty() -> {
                throw LoginException("Token cannot be null or empty")
            }
            token.contains("/n") -> {
                throw LoginException("Token cannot contain a new line")
            }
            token.length < minLength -> {
                throw LoginException("Token cannot be shorter than $minLength characters")
            }
            token.length > maxLength -> {
                throw LoginException("Token cannot be longer than $maxLength characters")
            }
            else -> {
                ydwk.setWebSocketManager(token, intents, userStatus, activity, etfInsteadOfJson)
                ydwk.setAllowedCache(allowedCache)
                ydwk.setDisallowedCache(disallowedCache)
                ydwk.enableShutDownHook()
                dispatcher?.let { ydwk.coroutineDispatcher = it }
                return ydwk
            }
        }
    }
}
