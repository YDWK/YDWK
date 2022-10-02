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

import io.github.ydwk.ydwk.cache.CacheType
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.ws.util.GateWayIntent
import okhttp3.OkHttpClient

/**
 * Used to create a bot instance and used the default intents decided by YDWK.
 *
 * @param token Used to authenticate the bot.
 */
fun createDefaultBot(token: String): YDWK {
    val ydwk = YDWKImpl(OkHttpClient())
    ydwk.setWebSocketManager(token, GateWayIntent.getDefaultIntents())
    ydwk.setAllowedCache(CacheType.getDefaultCache())
    return ydwk
}

/**
 * Used to create a bot instance and used the default intents decided by YDWK.
 *
 * @param token Used to authenticate the bot.
 * @param httpClient Used to create the websocket connection.
 */
fun createDefaultBot(token: String, httpClient: OkHttpClient): YDWK {
    val ydwk = YDWKImpl(httpClient)
    ydwk.setWebSocketManager(token, GateWayIntent.getDefaultIntents())
    return ydwk
}

/**
 * Used to create a bot instance.
 *
 * @param token Used to authenticate the bot.
 * @param intents The gateway intent which will decide what events are sent by discord.
 * @param allowedCache The cache type which will be cached.
 * @param disallowedCache The cache type which will not be cached.
 */
fun createCustomBot(
    token: String,
    intents: List<GateWayIntent>,
    allowedCache: Set<CacheType>,
    disallowedCache: Set<CacheType>
): YDWK {
    val ydwk = YDWKImpl(OkHttpClient())
    ydwk.setWebSocketManager(token, intents)
    ydwk.setAllowedCache(allowedCache)
    ydwk.setDisallowedCache(disallowedCache)
    return ydwk
}

/**
 * Used to create a bot instance.
 *
 * @param token Used to authenticate the bot.
 * @param intents The gateway intent which will decide what events are sent by discord.
 * @param httpClient Used to create the websocket connection.
 * @param allowedCache The cache type which will be cached.
 */
fun createCustomBot(
    token: String,
    intents: List<GateWayIntent>,
    allowedCache: Set<CacheType>,
    httpClient: OkHttpClient
): YDWK {
    val ydwk = YDWKImpl(httpClient)
    ydwk.setWebSocketManager(token, intents)
    ydwk.setAllowedCache(allowedCache)
    return ydwk
}

/**
 * Used to create a bot instance.
 *
 * @param token Used to authenticate the bot.
 * @param intents The gateway intent which will decide what events are sent by discord.
 * @param httpClient Used to create the websocket connection.
 * @param allowedCache The cache type which will be cached.
 * @param disallowedCache The cache type which will not be cached.
 */
fun createCustomBot(
    token: String,
    intents: List<GateWayIntent>,
    allowedCache: Set<CacheType>,
    disallowedCache: Set<CacheType>,
    httpClient: OkHttpClient
): YDWK {
    val ydwk = YDWKImpl(httpClient)
    ydwk.setWebSocketManager(token, intents)
    ydwk.setAllowedCache(allowedCache)
    ydwk.setDisallowedCache(disallowedCache)
    return ydwk
}

/**
 * Used to create a bot instance.
 *
 * @param token Used to authenticate the bot.
 * @param intents The gateway intent which will decide what events are sent by discord.
 * @param httpClient Used to create the websocket connection.
 * @param allowedCache The cache type which will be cached.
 */
fun createCustomBot(
    token: String,
    intents: List<GateWayIntent>,
    allowedCache: Set<CacheType>
): YDWK {
    val ydwk = YDWKImpl(OkHttpClient())
    ydwk.setWebSocketManager(token, intents)
    ydwk.setAllowedCache(allowedCache)
    return ydwk
}
