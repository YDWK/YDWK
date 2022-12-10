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
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent
import io.github.ydwk.ydwk.interaction.message.ActionRow
import io.github.ydwk.ydwk.interaction.message.button.Button
import io.github.ydwk.ydwk.interaction.message.button.ButtonStyle
import io.github.ydwk.ydwk.slash.Slash
import io.github.ydwk.ydwk.slash.SlashOption
import io.github.ydwk.ydwk.slash.SlashOptionType
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
        .addSlashCommand(Slash("forum_json", "Gets the json for forum"))
        .addSlashCommand(Slash("create_dm", "Creates a dm channel"))
        .addSlashCommand(
            Slash("option", "An option test")
                .addOption(
                    SlashOption(
                        "member", "The member to test the option with", SlashOptionType.USER)))
        .addSlashCommand(Slash("leave_vc", "Leaves a vc"))
        .addSlashCommand("button", "A button test")
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
                        it.slash.reply("Member is null!").isEphemeral(true).reply()
                    }
                }
            }
            "leave_vc" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    val slash = it.slash
                    if (member != null) {
                        val voiceState = member.voiceState
                        voiceState?.channel?.leave()?.get()
                    } else {
                        it.slash.reply("Member is null!").isEphemeral(true).reply()
                    }
                }
            }
            "forum_json" -> {
                withContext(Dispatchers.IO) {
                    if (it.slash.ydwk
                        .getGuildChannelById("1031971612238561390")
                        ?.guildChannelGetter != null) {
                        val forum =
                            it.slash.ydwk
                                .getGuildChannelById("1031971612238561390")
                                ?.guildChannelGetter
                                ?.asGuildForumChannel()

                        if (forum != null) {
                            it.slash.reply(forum.json.toPrettyString()).isEphemeral(true).reply()
                        } else {
                            it.slash.reply("Forum is null!").isEphemeral(true).reply()
                        }
                    } else {
                        it.slash.reply("Forum is null!").isEphemeral(true).reply()
                    }
                }
            }
            "ping" -> {
                it.slash.reply("Pong!").reply()
            }
            "create_dm" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    member?.createDmChannel?.get()?.sendMessage("Hello!")?.get()
                }
            }
            "option" -> {
                withContext(Dispatchers.IO) {
                    it.slash.reply(it.slash.getOption("member")!!.asUser.name).reply()
                }
            }
            "button" -> {
                withContext(Dispatchers.IO) {
                    it.slash
                        .reply("This is a button test!")
                        .addActionRow(ActionRow.of(Button.of(ButtonStyle.PRIMARY, "1", "Primary")))
                        .reply()
                }
            }
        }
    }
}
