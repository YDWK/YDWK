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
import io.github.ydwk.ydwk.createDefaultBot
import io.github.ydwk.ydwk.event.ListenerAdapter
import io.github.ydwk.ydwk.event.backend.event.on
import io.github.ydwk.ydwk.event.events.ReadyEvent
import io.github.ydwk.ydwk.event.events.interaction.SlashCommandEvent
import io.github.ydwk.ydwk.slash.Slash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Bot : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Bot is ready!")
    }
}

fun main() {
    val ydwk =
        createDefaultBot(JConfigUtils.getString("token") ?: throw Exception("Token not found!"))
    ydwk.addEvent(Bot())

    ydwk.waitForReady.slashBuilder.addSlashCommand(Slash("test", "This is a test command")).build()
    ydwk.waitForReady.slashBuilder.addSlashCommand(Slash("embed", "This is a test command")).build()

    ydwk.on<SlashCommandEvent> {
        if (it.slash.name == "test") {
            withContext(Dispatchers.IO) {
                it.slash.channel?.asGuildTextChannel()?.sendMessage("Hello World!")
                it.slash.reply("This is a test command!").get()
            }
        } else if (it.slash.name == "embed") {
            withContext(Dispatchers.IO) {
                val embed = ydwk.embedBuilder.setTitle("This is a test command!").build()
                it.slash.reply(embed).get()
            }
        }
    }
}
