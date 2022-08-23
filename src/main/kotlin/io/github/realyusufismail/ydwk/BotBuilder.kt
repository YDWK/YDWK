/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk

import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.ws.util.GateWayIntent

/**
 * Used to create a bot instance and used the default intents decided by YDWK.
 *
 * @param token Used to authenticate the bot.
 */
fun createDefaultBot(token: String): YDWK {
    val ydwk = YDWKImpl()
    ydwk.setWebSocketManager(token, GateWayIntent.getAllIntents())
    return ydwk
}

/**
 * Used to create a bot instance.
 *
 * @param token Used to authenticate the bot.
 * @param intents The gateway intent which will decide what events are sent by discord.
 */
fun createCustomBot(token: String, intents: List<GateWayIntent>): YDWK {
    val ydwk = YDWKImpl()
    ydwk.setWebSocketManager(token, intents)
    return ydwk
}