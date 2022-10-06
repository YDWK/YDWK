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
package io.github.ydwk.ydwk.entities

import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.message.Attachment
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.message.MessageType
import io.github.ydwk.ydwk.entities.message.Reaction
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.SnowFlake

// TODO : Add more properties
interface Message : SnowFlake {
    /**
     * Gets the channel where this message was sent.
     *
     * @return The channel where this message was sent.
     */
    val channel : Channel

    /**
     * Gets the author of this message.
     *
     * @return The author of this message.
     */
    val author : User

    /**
     * Gets the content of this message.
     *
     * @return The content of this message.
     */
    val content : String

    /**
     * Gets the time when this message was sent.
     *
     * @return The time when this message was sent.
     */
    val time : String

    /**
     * Gets the time when this message was edited.
     *
     * @return The time when this message was edited.
     */
    val editedTime : String?

    /**
     * Gets weather this message is TTS.
     *
     * @return Weather this message is TTS.
     */
    val tts : Boolean

    /**
     * Gets weather this message mentions everyone.
     *
     * @return Weather this message mentions everyone.
     */
    val mentionEveryone : Boolean

    /**
     * Gets the mentioned users.
     *
     * @return The mentioned users.
     */
    val mentionedUsers : List<User>

    /**
     * Gets the mentioned roles.
     *
     * @return The mentioned roles.
     */
    val mentionedRoles : List<Role>

    /**
     * Gets the mentioned channels.
     *
     * @return The mentioned channels.
     */
    val mentionedChannels : List<Channel>

    /**
     * Gets the attachments.
     *
     * @return The attachments.
     */
    val attachments : List<Attachment>

    /**
     * Gets the embedded contents.
     *
     * @return The embedded contents.
     */
    val embeds : List<Embed>

    /**
     * Gets the reactions.
     *
     * @return The reactions.
     */
    val reactions : List<Reaction>

    /**
     * Gets the nonce.
     *
     * @return The nonce.
     */
    val nonce : String?

    /**
     * Gets weather this message is pinned.
     *
     * @return Weather this message is pinned.
     */
    val pinned : Boolean

    /**
     * Gets the webhook id of this message.
     *
     * @return The webhook id of this message.
     */
    val webhookId : GetterSnowFlake?

    /**
     * Gets the type of this message.
     *
     * @return The type of this message.
     */
    val type : MessageType
}
