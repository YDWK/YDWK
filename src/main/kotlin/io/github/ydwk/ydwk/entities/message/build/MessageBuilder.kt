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

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.message.MessageFlag
import io.github.ydwk.ydwk.entities.message.SendAble
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.rest.EndPoint
import java.util.concurrent.CompletableFuture
import okhttp3.RequestBody.Companion.toRequestBody

class MessageBuilder {
    private var content: String? = null
    private var embeds: MutableList<Embed> = mutableListOf()
    private var tts: Boolean? = null
    private var flags: MutableList<MessageFlag> = mutableListOf()

    /**
     * Sets the content of the message.
     *
     * @param message The content of the message.
     * @return The [MessageBuilder] instance.
     */
    fun setContent(message: String): MessageBuilder {
        content = message
        return this
    }

    /**
     * Whether the message should be sent with text-to-speech.
     *
     * @param tts Whether the message should be sent with text-to-speech.
     * @return The [MessageBuilder] instance.
     */
    fun setTts(tts: Boolean): MessageBuilder {
        this.tts = tts
        return this
    }

    /**
     * Sets the embed of the message.
     *
     * @param embed The embed of the message.
     * @return The [MessageBuilder] instance.
     */
    fun setEmbed(embed: Embed): MessageBuilder {
        embeds.add(embed)
        return this
    }

    /**
     * Sets the embeds of the message.
     *
     * @param embeds The embeds of the message.
     * @return The [MessageBuilder] instance.
     */
    fun setEmbeds(embeds: Array<out Embed>): MessageBuilder {
        this.embeds.addAll(embeds)
        return this
    }

    /**
     * Sets the embeds of the message.
     *
     * @param embeds The embeds of the message.
     * @return The [MessageBuilder] instance.
     */
    fun setEmbeds(embeds: List<Embed>): MessageBuilder {
        this.embeds.addAll(embeds)
        return this
    }

    /**
     * Set a message flag.
     *
     * @param flag The flag to set.
     * @return The [MessageBuilder] instance.
     */
    fun setFlag(flag: MessageFlag): MessageBuilder {
        flags.add(flag)
        return this
    }

    /**
     * Sets the message flags.
     *
     * @param flags The flags to set.
     * @return The [MessageBuilder] instance.
     */
    fun setFlags(flags: Array<out MessageFlag>): MessageBuilder {
        this.flags.addAll(flags)
        return this
    }

    /**
     * Sets the message flags.
     *
     * @param flags The flags to set.
     * @return The [MessageBuilder] instance.
     */
    fun setFlags(flags: List<MessageFlag>): MessageBuilder {
        this.flags.addAll(flags)
        return this
    }

    /**
     * Sends the message to the specified channel.
     *
     * @param channel The channel to send the message to.
     * @return The [Message] that was sent.
     */
    fun send(sendeadble: SendAble): CompletableFuture<Message> {
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

    /**
     * Sends the message to the specified channel.
     *
     * @param channel The channel to send the message to.
     * @return The [Message] that was sent.
     */
    private fun sendToTextChannel(channel: TextChannel): CompletableFuture<Message> {
        val body = sendMessageToChannelBody(channel.ydwk, content, tts, embeds, flags)
        return channel.ydwk.restApiManager
            .post(
                body.toString().toRequestBody(),
                EndPoint.ChannelEndpoint.CREATE_MESSAGE,
                channel.id)
            .execute { response ->
                val json = response.jsonBody
                if (json == null) {
                    throw IllegalStateException("Response body is null")
                } else {
                    MessageImpl(channel.ydwk, json, json["id"].asLong())
                }
            }
    }

    /**
     * Sends the message to the specified member.
     *
     * @param member The member to send the message to.
     * @return The [Message] that was sent.
     */
    private fun sendToMember(member: Member): CompletableFuture<Message> {
        return send(member.user as SendAble)
    }

    /**
     * Sends the message to the specified user.
     *
     * @param user The user to send the message to.
     * @return The [Message] that was sent.
     */
    private fun sendToUser(user: User): CompletableFuture<Message> {
        return user.createDmChannel.thenCompose { channel -> send(channel) }
    }

    private fun sendMessageToChannelBody(
        ydwk: YDWK,
        content: String?,
        tts: Boolean? = null,
        embeds: List<Embed> = emptyList(),
        flags: List<MessageFlag> = emptyList()
    ): ObjectNode {
        val body = ydwk.objectNode
        if (content != null) body.put("content", content)
        if (tts != null) body.put("tts", tts)
        if (embeds.isNotEmpty()) {
            val embedArray = ydwk.objectNode.arrayNode()
            embeds.forEach { embedArray.add(it.json) }
            body.set<ArrayNode>("embeds", embedArray)
        }
        if (flags.isNotEmpty()) body.put("flags", flags.sumOf { it.getValue() })
        return body
    }
}
