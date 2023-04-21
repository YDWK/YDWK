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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk

import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.ws.WebSocketManager
import io.github.ydwk.ydwk.ws.util.LoggedIn
import java.time.Instant
import kotlin.random.Random
import kotlinx.coroutines.delay

/** This is where the websocket is created. */
var YDWK.webSocketManager: WebSocketManager?
    get() = null
    set(value) {}

fun YDWK.shutdownAPI() {
    val oneToFiveSecondTimeout = Random.nextLong(1000, 5000)
    (this as YDWKImpl).shutDownRestApi()
    ydwkLogger.info("Timeout for $oneToFiveSecondTimeout then shutting down")

    try {
        Thread.sleep(oneToFiveSecondTimeout)
    } catch (e: InterruptedException) {
        ydwkLogger.error("Error while sleeping" + e.message)
    }

    try {
        webSocketManager?.triggerShutdown()
    } catch (e: Exception) {
        ydwkLogger.error("Error while shutting down websocket" + e.message)
    }
}

/**
 * Starts the websocket manager
 *
 * @param token The token of the bot which is used to authenticate the bot.
 * @param intents The gateway intent which will decide what events are sent by discord.
 * @param userStatus The status of the bot.
 * @param activity The activity of the bot.
 * @param etfInsteadOfJson Whether to use ETF instead of JSON.
 */
fun YDWKImpl.setWebSocketManager(
    token: String,
    intents: List<GateWayIntent>,
    userStatus: UserStatus? = null,
    activity: ActivityPayload? = null,
    etfInsteadOfJson: Boolean,
) {
    val ws: WebSocketManager?
    ws = WebSocketManager(this, token, intents, userStatus, activity, etfInsteadOfJson)
    this.webSocketManager = ws.connect()
    // this.webSocketManager!!.deleteMessageCachePast14Days()
    this.token = token
}

/**
 * Sets the logged in status
 *
 * @param loggedIn The logged in status which is used to send messages to discord.
 */
fun YDWK.setLoggedIn(loggedIn: LoggedIn) {
    this.loggedInStatus = loggedIn
}

var YDWK.loggedInStatus: LoggedIn?
    get() = null
    private set(value) {}

suspend fun YDWK.awaitReady(): YDWK {
    val ws = webSocketManager ?: throw IllegalStateException("Bot is not logged in")
    while (!ws.ready) {
        delay(1000)
        ydwkLogger.debug("Waiting for bot to be ready")
    } // wait for bot to be ready
    return this
}

val YDWK.uptime: Instant
    get() = webSocketManager!!.upTime ?: throw IllegalStateException("Bot is not logged in")

fun YDWKImpl.enableShutDownHook() {
    Runtime.getRuntime()
        .addShutdownHook(
            Thread {
                ydwkLogger.info("Shutting down YDWK")
                shutdownAPI()
            })
}
