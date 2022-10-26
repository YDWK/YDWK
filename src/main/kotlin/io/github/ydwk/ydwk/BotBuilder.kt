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
import io.github.ydwk.ydwk.ws.util.GateWayIntent
import okhttp3.OkHttpClient

object BotBuilder {
    private var token: String? = null
    private var okHttpClient: OkHttpClient? = null
    private var intents: MutableList<GateWayIntent> = mutableListOf()
    private var allowedCache: MutableList<CacheIds> = mutableListOf()
    private var disallowedCache: MutableList<CacheIds> = mutableListOf()
    private var guildIds = mutableListOf<String>()

    fun createDefaultBot(token: String): BotBuilder {
        this.token = token
        intents.addAll(GateWayIntent.getDefaultIntents())
        allowedCache.addAll(CacheIds.getDefaultCache())
        return this
    }

    fun createDefaultBot(token: Long): BotBuilder {
        return createDefaultBot(token.toString())
    }

    fun createCustomBot(token: String, intents: MutableList<GateWayIntent>): BotBuilder {
        this.token = token
        this.intents.addAll(intents)
        return this
    }

    fun createCustomBot(token: Long, intents: MutableList<GateWayIntent>): BotBuilder {
        return createCustomBot(token.toString(), intents)
    }

    fun setOkHttpClient(okHttpClient: OkHttpClient): BotBuilder {
        this.okHttpClient = okHttpClient
        return this
    }

    fun setIntents(intents: MutableList<GateWayIntent>, clearIntent: Boolean): BotBuilder {
        if (clearIntent) {
            this.intents.clear()
        }
        this.intents.addAll(intents)
        return this
    }

    fun setIntents(intents: MutableList<GateWayIntent>): BotBuilder {
        return setIntents(intents, true)
    }

    fun setAllowedCache(allowedCache: MutableList<CacheIds>, clearCache: Boolean): BotBuilder {
        if (clearCache) {
            this.allowedCache.clear()
        }
        this.allowedCache.addAll(allowedCache)
        return this
    }

    fun setAllowedCache(allowedCache: MutableList<CacheIds>): BotBuilder {
        return setAllowedCache(allowedCache, false)
    }

    fun setDisallowedCache(
        disallowedCache: MutableList<CacheIds>,
        clearCache: Boolean
    ): BotBuilder {
        if (clearCache) {
            this.disallowedCache.clear()
        }
        this.disallowedCache.addAll(disallowedCache)
        return this
    }

    fun setDisallowedCache(disallowedCache: MutableList<CacheIds>): BotBuilder {
        return setDisallowedCache(disallowedCache, false)
    }

    fun setGuildIds(guildIds: MutableList<String>): BotBuilder {
        this.guildIds.addAll(guildIds)
        return this
    }

    fun build(): YDWK {
        val ydwk = YDWKImpl(okHttpClient ?: OkHttpClient())
        ydwk.setWebSocketManager(token!!, intents)
        ydwk.setAllowedCache(allowedCache)
        ydwk.setDisallowedCache(disallowedCache)
        return ydwk
    }
}
