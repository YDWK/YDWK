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
package io.github.ydwk.ydwk.entities.message.build

import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.message.Sendeadble
import java.util.concurrent.CompletableFuture

class MessageBuilder {
    private var content: String? = null
    private var embed: Embed? = null
    private var tts: Boolean? = null

    fun setContent(message: String): MessageBuilder {
        content = message
        return this
    }

    fun setTts(tts: Boolean): MessageBuilder {
        this.tts = tts
        return this
    }

    fun setEmbed(embed: Embed): MessageBuilder {
        this.embed = embed
        return this
    }

    fun send(sendeadble: Sendeadble): CompletableFuture<Message> {
        return when (sendeadble) {
            is TextChannel -> {
                sendToTextChannel(sendeadble)
            }
            is Member -> {
                sendToMember(sendeadble)
            }
            is User -> {
                sendToUser(sendeadble)
            }
            else -> {
                throw IllegalArgumentException("Sendeadble must be a TextChannel, Member, or User")
            }
        }
    }

    private fun sendToTextChannel(channel: TextChannel): CompletableFuture<Message> {
        return TODO()
    }

    private fun sendToMember(member: Member): CompletableFuture<Message> {
        return TODO()
    }

    private fun sendToUser(user: User): CompletableFuture<Message> {
        return TODO()
    }
}
