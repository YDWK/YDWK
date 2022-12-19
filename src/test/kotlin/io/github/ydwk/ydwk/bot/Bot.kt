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
import io.github.ydwk.ydwk.Activity
import io.github.ydwk.ydwk.BotBuilder.createDefaultBot
import io.github.ydwk.ydwk.evm.backend.event.on
import io.github.ydwk.ydwk.evm.event.events.interaction.button.ButtonClickEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent
import io.github.ydwk.ydwk.interaction.message.ActionRow
import io.github.ydwk.ydwk.interaction.message.button.Button
import io.github.ydwk.ydwk.interaction.message.button.ButtonStyle
import io.github.ydwk.ydwk.slash.Slash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun main() {
    val ydwk =
        createDefaultBot(JConfigUtils.getString("token") ?: throw Exception("Token not found!"))
            .setActivity(Activity.playing("YDWK"))
            .setETFInsteadOfJson(true)
            .build()

    ydwk.waitForReady.slashBuilder
        .addSlashCommand(Slash("join_vc", "Joins a vc"))
        .addSlashCommand(Slash("create_dm", "Creates a dm channel"))
        .addSlashCommand("channel", "Creates a Channel")
        .addSlashCommand("button", "A button test")
        .addSlashCommand("bot_info", "Gets the bot info")
        .build()

    ydwk.on<SlashCommandEvent> {
        when (it.slash.name) {
            "join_vc" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    val slash = it.slash
                    if (member != null) {
                        val voiceState = member.voiceState
                        voiceState?.channel?.joinCompletableFuture()?.get()
                    } else {
                        it.slash.reply("Member is null!").isEphemeral(true).trigger()
                    }
                }
            }
            "ping" -> {
                it.slash.reply("Pong!").trigger()
            }
            "create_dm" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    member?.createDmChannel?.get()?.sendMessage("Hello!")?.get()
                }
            }
            "button" -> {
                withContext(Dispatchers.IO) {
                    it.slash
                        .reply("This is a button test!")
                        .addActionRow(
                            ActionRow.of(
                                Button.of(ButtonStyle.PRIMARY, "1", "Primary"),
                                Button.of(ButtonStyle.SECONDARY, "2", "Secondary"),
                                Button.of(ButtonStyle.SUCCESS, "3", "Success"),
                                Button.of(ButtonStyle.DANGER, "4", "Danger"),
                                Button.of("Link", "https://google.com")))
                        .trigger()
                }
            }
            "bot_info" -> {
                withContext(Dispatchers.IO) {
                    it.slash.reply("Bot info: ${it.slash.guild?.botAsMember?.joinedAt}").trigger()
                }
            }
            "channel" -> {
                withContext(Dispatchers.IO) {
                    it.slash.ydwk.entityBuilder
                        .getGuildEntitiesBuilder()
                        .createChannel("Test Channel", it.slash.guild!!.id)
                        .createMessageChannel()
                        .create()
                    it.slash.reply("Channel created!").trigger()
                }
            }
        }
    }

    ydwk.on<ButtonClickEvent> {
        withContext(Dispatchers.IO) {
            when (it.button.customId) {
                "1" -> {
                    it.button.reply("Primary button clicked!").trigger()
                }
                "2" -> {
                    it.button.reply("Secondary button clicked!").trigger()
                }
                "3" -> {
                    it.button.reply("Success button clicked!").trigger()
                }
                "4" -> {
                    it.button.message.delete().get()
                }
            }
        }
    }
}
