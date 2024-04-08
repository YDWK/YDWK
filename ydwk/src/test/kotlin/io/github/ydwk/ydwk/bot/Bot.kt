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
import io.github.ydwk.yde.util.exception.LoginException
import io.github.ydwk.ydwk.Activity
import io.github.ydwk.ydwk.BotBuilder.Companion.buildBot
import io.github.ydwk.ydwk.evm.event.EventListeners.Companion.onSlashCommandEvent
import io.github.ydwk.ydwk.util.ydwk
import io.github.ydwk.ydwk.voice.example.MP3VoiceSource
import io.github.ydwk.ydwk.voice.joinVc
import java.io.File
import kotlinx.coroutines.runBlocking

fun main() {
    val jConfig = JConfig.build()

    val tokenAsNode = jConfig["token"]

    val token = tokenAsNode?.asText() ?: throw LoginException("Token not found!")

    val ydwk =
        buildBot(token)
            .activity(Activity.playing("YDWK"))
            .etfInsteadOfJson(true)
            .build()
            .buildYDWK()

    runBlocking {
        ydwk.awaitReady().slashBuilder.addSlashCommand(SlashCommandBuilder("ping", "Pong!")).build()
    }

    ydwk onSlashCommandEvent
        {
            when (it.slash.name) {
                "ping" -> {
                    it.slash.reply("Pong!").trigger()

                    val guilds = it.slash.ydwk.requestGuilds().getOrNull()

                    if (guilds != null) {
                        it.slash.reply("Guilds: ${guilds.size}").trigger()
                    }
                }
                "join_vc" -> {
                    val voice =
                        it.slash.ydwk
                            .getGuildChannelById("938122131949097056")!!
                            .guildChannelGetter
                            .asGuildVoiceChannel()

                    if (voice == null) {
                        it.slash.reply("Error").trigger()
                    }

                    val vc = voice!!.joinVc(false, false).join()

                    vc.setSource(MP3VoiceSource(File("mp3-sample.mp3"))).join()
                }
            }
        }
}
