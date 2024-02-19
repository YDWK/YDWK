/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.entities.message

import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.message.build.MessageBuilder
import kotlinx.coroutines.CompletableDeferred

/** Represents an object that can be used to send a message to a discord text channel. */
interface SendAble {
    /**
     * Sets the content of the message.
     *
     * @param message The content of the message.
     * @return The current instance in order to chain call methods.
     */
    fun setContent(message: String): SendAble {
        messageBuilder.setContent(message)
        return this
    }

    /**
     * Whether the message should be sent with text-to-speech.
     *
     * @param tts Whether the message should be sent with text-to-speech.
     * @return The current instance in order to chain call methods.
     */
    fun setTts(tts: Boolean): SendAble {
        messageBuilder.setTts(tts)
        return this
    }

    /**
     * Sets the embed of the message.
     *
     * @param embed The embed of the message.
     * @return The current instance in order to chain call methods.
     */
    fun setEmbed(embed: Embed): SendAble {
        messageBuilder.setEmbed(embed)
        return this
    }

    /**
     * Sets the embeds of the message.
     *
     * @param embeds The embeds of the message.
     * @return The current instance in order to chain call methods.
     */
    fun setEmbeds(vararg embeds: Embed): SendAble {
        messageBuilder.setEmbeds(embeds)
        return this
    }

    /**
     * Sets the embeds of the message.
     *
     * @param embeds The embeds of the message.
     * @return The current instance in order to chain call methods.
     */
    fun setEmbeds(embeds: List<Embed>): SendAble {
        messageBuilder.setEmbeds(embeds)
        return this
    }

    /**
     * Set a message flag.
     *
     * @param flag The flag to set.
     * @return The current instance in order to chain call methods.
     */
    fun setFlag(flag: MessageFlag): SendAble {
        messageBuilder.setFlag(flag)
        return this
    }

    /**
     * Sets the message flags.
     *
     * @param flags The flags to set.
     * @return The current instance in order to chain call methods.
     */
    fun setFlags(vararg flags: MessageFlag): SendAble {
        messageBuilder.setFlags(flags)
        return this
    }

    /**
     * Sets the message flags.
     *
     * @param flags The flags to set.
     * @return The current instance in order to chain call methods.
     */
    fun setFlags(flags: List<MessageFlag>): SendAble {
        messageBuilder.setFlags(flags)
        return this
    }

    /**
     * Sends the message.
     *
     * @return The [CompletableDeferred] of the message.
     */
    suspend fun send(): CompletableDeferred<Message> {
        return messageBuilder.send(this)
    }

    /**
     * The actual message builder instance.
     *
     * @return The message builder instance.
     */
    val messageBuilder: MessageBuilder
        get() = MessageBuilder()
}
