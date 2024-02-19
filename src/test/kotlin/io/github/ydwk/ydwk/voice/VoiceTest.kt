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
package io.github.ydwk.ydwk.voice

import io.github.realyusufismail.jconfig.util.JConfigUtils
import io.github.ydwk.yde.builders.slash.SlashCommandBuilder
import io.github.ydwk.ydwk.Activity
import io.github.ydwk.ydwk.BotBuilder
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent
import io.github.ydwk.ydwk.evm.listeners.InteractionEventListener
import io.github.ydwk.ydwk.util.ydwk
import java.io.File

class VoiceTest : InteractionEventListener {

    override suspend fun onSlashCommandEvent(event: SlashCommandEvent) {

        if (event.slash.name == "join_vc") {
            val slash = event.slash

            val voice =
                slash.ydwk
                    .getGuildChannelById("938122131949097056")!!
                    .guildChannelGetter
                    .asGuildVoiceChannel()

            voice!!
                .joinVc(false, false)
                .join()
                .setSource(
                    VoiceSource.ConvertVoiceSource(
                        File("io/github/ydwk/ydwk/voice/mp3-sample.mp3")))
                .also { slash.reply("Joined vc").trigger() }
        } else if (event.slash.name == "leave_vc") {
            val slash = event.slash
            slash.guild!!.botAsMember.leaveVC().join()
        }
    }
}

suspend fun main() {
    val ydwk =
        BotBuilder.createDefaultBot(
                JConfigUtils.getString("token") ?: throw Exception("Token not found!"))
            .setActivity(Activity.playing("YDWK"))
            .setETFInsteadOfJson(true)
            .build()

    ydwk.addEventListeners(VoiceTest())

    ydwk
        .awaitReady()
        .slashBuilder
        .addSlashCommand(SlashCommandBuilder("join_vc", "Used to join a vc"))
        .addSlashCommand("leave_vc", "Leaves the vc")
        .build()
}
