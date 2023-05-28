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
package io.github.ydwk.ydwk.bot

import io.github.realyusufismail.jconfig.util.JConfigUtils
import io.github.ydwk.yde.builders.slash.SlashCommandBuilder
import io.github.ydwk.yde.builders.slash.SlashSubCommand
import io.github.ydwk.yde.interaction.message.ActionRow
import io.github.ydwk.yde.interaction.message.button.Button
import io.github.ydwk.yde.interaction.message.button.ButtonStyle
import io.github.ydwk.yde.interaction.message.selectmenu.types.RoleSelectMenu
import io.github.ydwk.ydwk.Activity
import io.github.ydwk.ydwk.BotBuilder.createDefaultBot
import io.github.ydwk.ydwk.evm.backend.event.on
import io.github.ydwk.ydwk.evm.event.events.interaction.button.ButtonClickEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.selectmenu.RoleSelectMenuEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent
import io.github.ydwk.ydwk.voice.impl.util.joinNow
import io.github.ydwk.ydwk.voice.impl.util.leaveNow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main() {
    val ydwk =
        createDefaultBot(JConfigUtils.getString("token") ?: throw Exception("Token not found!"))
            .setActivity(Activity.playing("YDWK"))
            .setETFInsteadOfJson(true)
            .build()

    ydwk
        .awaitReady()
        .slashBuilder
        .addSlashCommand(SlashCommandBuilder("join_vc", "Joins a vc"))
        .addSlashCommand("leave_vc", "Leaves a vc")
        .addSlashCommand(SlashCommandBuilder("create_dm", "Creates a dm channel"))
        .addSlashCommand("button", "A button test")
        .addSlashCommand("bot_info", "The bot info")
        .addSlashCommand("add_roles", "Add roles")
        .addSlashCommand(
            SlashCommandBuilder("subcommand", "A subcommand test")
                .addSubCommand(SlashSubCommand("subcommand", "A subcommand test"))
                .addSubCommand(SlashSubCommand("subcommandtwo", "A subcommand test")))
        .build()

    ydwk.on<SlashCommandEvent> {
        when (it.slash.name) {
            "join_vc" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    if (member != null) {
                        val voiceState = member.voiceState
                        it.slash.reply("Joined vc!").trigger()
                        voiceState?.channel?.joinNow()
                    } else {
                        it.slash.reply("Member is null!").setEphemeral(true).trigger()
                    }
                }
            }
            "leave_vc" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    if (member != null) {
                        val voiceState = member.voiceState
                        it.slash.reply("Left vc!").trigger()
                        voiceState?.channel?.leaveNow()
                    } else {
                        it.slash.reply("Member is null!").setEphemeral(true).trigger()
                    }
                }
            }
            "ping" -> {
                it.slash.reply("Pong!").trigger()
            }
            "create_dm" -> {
                withContext(Dispatchers.IO) {
                    val member = it.slash.member
                    member
                        ?.createDmChannel
                        ?.getCompleted()
                        ?.setContent("Hello!")
                        ?.send()
                        ?.getCompleted()
                        ?: throw Exception("Member is null!")
                }
            }
            "button" -> {
                withContext(Dispatchers.IO) {
                    it.slash
                        .reply("This is a button test!")
                        .addActionRow(
                            ActionRow.invoke(
                                Button.invoke(ButtonStyle.PRIMARY, "1", "Primary"),
                                Button.invoke(ButtonStyle.SECONDARY, "2", "Secondary"),
                                Button.invoke(ButtonStyle.SUCCESS, "3", "Success"),
                                Button.invoke(ButtonStyle.DANGER, "4", "Danger"),
                                Button.invoke("Link", "https://google.com")))
                        .trigger()
                }
            }
            "bot_info" -> {
                withContext(Dispatchers.IO) {
                    it.slash.reply("Bot info: ${it.slash.guild?.botAsMember?.joinedAt}").trigger()
                }
            }
            "add_roles" -> {
                withContext(Dispatchers.IO) {
                    it.slash
                        .reply("Add your role by choose the roles through the select menu")
                        .addActionRow(
                            ActionRow(
                                RoleSelectMenu("role_select")
                                    .setPlaceholder("Select your role")
                                    .create()))
                        .trigger()
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
                    it.button.message.delete().getCompleted()
                }
            }
        }
    }

    ydwk.on<RoleSelectMenuEvent> {
        withContext(Dispatchers.IO) {
            it.selectMenu.reply("Role added!").trigger()
            for (role in it.selectMenu.selectedRoles) {
                it.selectMenu.member?.addRole(role)?.getCompleted()
            }
        }
    }
}
