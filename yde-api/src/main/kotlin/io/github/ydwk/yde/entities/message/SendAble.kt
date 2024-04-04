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
package io.github.ydwk.yde.entities.message

import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.message.build.MessageBuilder
import io.github.ydwk.yde.rest.RestResult

/** Represents an object that can be used to send a message to a discord text channel. */
interface SendAble {
    /**
     * Sends a message.
     *
     * @param message The content of the message.
     * @return The [RestResult] of the message.
     */
    suspend fun sendMessage(message: String): RestResult<Message> {
        return messageBuilder.setContent(message).send(this)
    }

    /**
     * Sends an embed message.
     *
     * @param embed The embed message.
     * @return The [RestResult] of the message.
     */
    suspend fun sendEmbed(embed: Embed): RestResult<Message> {
        return messageBuilder.setEmbed(embed).send(this)
    }

    /**
     * The actual message builder instance.
     *
     * @return The message builder instance.
     */
    val messageBuilder: MessageBuilder
        get() = MessageBuilder()
}
