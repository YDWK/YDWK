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
import io.github.ydwk.ydwk.evm.ListenerAdapter
import io.github.ydwk.ydwk.evm.backend.event.on
import io.github.ydwk.ydwk.slash.Slash
import io.github.ydwk.ydwk.slash.SlashOption
import io.github.ydwk.ydwk.slash.SlashOptionType
import java.awt.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Bot : ListenerAdapter() {
    override fun onReady(event: io.github.ydwk.ydwk.evm.event.events.gateway.ReadyEvent) {
        println("Bot is ready!")
    }
}

fun main() {
    val ydwk =
        createDefaultBot(JConfigUtils.getString("token") ?: throw Exception("Token not found!"))
    ydwk.addEvent(Bot())

    // TODO: having more than 6 commands leads to rate limit need to fix
    ydwk.waitForReady.slashBuilder
        .addSlashCommand(Slash("embed", "This is a test command"))
        .addSlashCommand(Slash("json", "Gets the json for member"))
        .addSlashCommand(Slash("forum_json", "Gets the json for forum"))
        .addSlashCommand(Slash("create_dm", "Creates a dm channel"))
        .addSlashCommand(
            Slash("option", "An option test")
                .addOption(
                    SlashOption(
                        "member", "The member to test the option with", SlashOptionType.USER)))
        .build()

    ydwk.on<io.github.ydwk.ydwk.evm.event.events.interaction.SlashCommandEvent> {
        when (it.slash.name) {
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
            "option" -> {
                withContext(Dispatchers.IO) {
                    it.slash.reply(it.slash.getOption("member")!!.asUser.name).get()
                }
            }
        }
    }
}
