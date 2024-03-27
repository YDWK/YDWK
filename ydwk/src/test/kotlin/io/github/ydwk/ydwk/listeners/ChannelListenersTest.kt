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
package io.github.ydwk.ydwk.listeners

import io.github.realyusufismail.jconfig.JConfig
import io.github.ydwk.ydwk.Activity
import io.github.ydwk.ydwk.BotBuilder
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelCreateEvent
import io.github.ydwk.ydwk.evm.listeners.ChannelEventListener

class ChannelListenersTest : ChannelEventListener {
    override suspend fun onChannelCreateEvent(event: ChannelCreateEvent) {
        println("Channel")
    }
}

suspend fun main() {
    val ydwk =
        BotBuilder.buildBot(
                JConfig.build().get("token")?.asText() ?: throw Exception("Token not found!"))
            .activity(Activity.playing("YDWK"))
            .etfInsteadOfJson(true)
            .build()
            .buildYDWK()

    ydwk.awaitReady().addEventListeners(ChannelListenersTest())
}