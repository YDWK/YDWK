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
package io.github.ydwk.ydwk.bot

import io.github.realyusufismail.jconfig.JConfig
import io.github.ydwk.yde.builders.slash.SlashCommandBuilder
import io.github.ydwk.yde.builders.slash.SlashSubCommand
import io.github.ydwk.yde.interaction.message.ActionRow
import io.github.ydwk.yde.interaction.message.button.Button
import io.github.ydwk.yde.interaction.message.button.ButtonStyle
import io.github.ydwk.yde.interaction.message.selectmenu.types.RoleSelectMenu
import io.github.ydwk.yde.util.LOOM
import io.github.ydwk.ydwk.Activity
import io.github.ydwk.ydwk.BotBuilder.createDefaultBot
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent
import io.github.ydwk.ydwk.evm.listeners.InteractionEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun main() {
    val ydwk =
        createDefaultBot(
                JConfig.build().get("token")?.asText() ?: throw Exception("Token not found!"))
            .setActivity(Activity.playing("YDWK"))
            .setETFInsteadOfJson(true)
            .build()

    ydwk
        .awaitReady()
        .slashBuilder
        .addSlashCommand(SlashCommandBuilder("create_dm", "Creates a dm channel"))
        .addSlashCommand("button", "A button test")
        .addSlashCommand("bot_info", "The bot info")
        .addSlashCommand("add_roles", "Add roles")
        .addSlashCommand(
            SlashCommandBuilder("subcommand", "A subcommand test")
                .addSubCommand(SlashSubCommand("subcommand", "A subcommand test"))
                .addSubCommand(SlashSubCommand("subcommandtwo", "A subcommand test")))
        .build()

    ydwk.awaitReady().addEventListeners(Test())

    ydwk.eventListener.onSlashCommandEvent {
        when (it.slash.name) {
            "ping" -> {
                it.slash.reply("Pong!").trigger()
            }
            "create_dm" -> {
                withContext(Dispatchers.LOOM) {
                    val member = it.slash.member
                    member?.createDmChannel?.await()?.setContent("Hello!")?.send()?.await()
                        ?: throw Exception("Member is null!")
                }
            }
            "button" -> {
                withContext(Dispatchers.LOOM) {
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
            "add_roles" -> {
                withContext(Dispatchers.LOOM) {
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

    ydwk.eventListener.onButtonClickEvent {
        withContext(Dispatchers.LOOM) {
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
                    it.button.message.delete().await()
                }
            }
        }
    }

    ydwk.eventListener.onRoleSelectMenuEvent {
        withContext(Dispatchers.LOOM) {
            it.selectMenu.reply("Role added!").trigger()
            for (role in it.selectMenu.selectedRoles) {
                it.selectMenu.member?.addRole(role)?.await()
            }
        }
    }
}

class Test : InteractionEventListener {
    override suspend fun onSlashCommandEvent(event: SlashCommandEvent) {
        if (event.slash.name == "bot_info") {
            event.slash.reply("Bot info: ${event.slash.guild?.getBotAsMember()?.joinedAt}").trigger()
        }
    }
}
