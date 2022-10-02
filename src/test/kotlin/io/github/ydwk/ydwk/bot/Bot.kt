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
package io.github.ydwk.ydwk.bot

import io.github.realyusufismail.jconfig.util.JConfigUtils
import io.github.realyusufismail.ydwk.createDefaultBot
import io.github.realyusufismail.ydwk.event.ListenerAdapter
import io.github.realyusufismail.ydwk.event.backend.event.on
import io.github.realyusufismail.ydwk.event.events.ReadyEvent
import io.github.realyusufismail.ydwk.slash.Slash

class Bot : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Bot is ready!")
    }
}

fun main() {
    val ydwk = createDefaultBot(JConfigUtils.getString("token"))

    ydwk.on<ReadyEvent> { println("Ready!") }

    ydwk.addEvent(Bot())

    ydwk.waitForReady.slashBuilder.addSlashCommand(Slash("test", "This is a test command")).build()
}
