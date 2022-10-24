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
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.event.ListenerAdapter
import io.github.ydwk.ydwk.event.backend.event.on
import io.github.ydwk.ydwk.event.events.ReadyEvent
import io.github.ydwk.ydwk.event.events.interaction.SlashCommandEvent
import io.github.ydwk.ydwk.slash.Slash
import java.awt.Color
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

    ydwk.waitForReady.slashBuilder
        .addSlashCommand(Slash("test", "This is a test command"))
        .addSlashCommand(Slash("embed", "This is a test command"))
        .addSlashCommand(Slash("json", "Gets the json for member"))
        .addSlashCommand(Slash("forum_json", "Gets the json for forum"))
        .addSlashCommand(Slash("create_dm", "Creates a dm channel"))
        .build()

    ydwk.on<SlashCommandEvent> {
        when (it.slash.name) {
            "test" -> {
                withContext(Dispatchers.IO) { it.slash.reply("This is a test command!").get() }
            }
            "embed" -> {
                withContext(Dispatchers.IO) {
                    val embed = ydwk.embedBuilder
                    val member = it.slash.member
                    if (member != null) {
                        embed.setTitle(member.user!!.name)
                        embed.setDescription("Yo this is a test embed")
                        embed.setColor(Color.blue)
                        it.slash.reply(embed.build()).get()
                    }
                }
            }
            "json" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    if (member != null) {
                        it.slash.reply(member.json.toPrettyString()).get()
                    }
                }
            }
            "forum_json" -> {
                withContext(Dispatchers.IO) {
                    val forum = it.slash.ydwk.getGuildTextChannel("1031971612238561390")
                    if (forum != null) {
                        it.slash.reply(forum.json.toPrettyString()).get()
                    }
                }
            }
            "create_dm" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    member?.createDmChannel?.get()?.sendMessage("Hello!")?.get()
                }
            }
            "ban" -> {
                withContext(Dispatchers.IO) {
                    val memberOption =
                        it.slash.options?.find { it.name == "member" }?.value as Member
                    it.slash.guild?.banUser(memberOption.user)?.get()
                }
            }
        }
    }
}
