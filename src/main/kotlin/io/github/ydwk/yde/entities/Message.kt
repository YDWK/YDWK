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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.message.*
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.SnowFlake
import kotlinx.coroutines.CompletableDeferred

interface Message : SnowFlake, GenericEntity {
    /**
     * The channel where this message was sent.
     *
     * @return The channel where this message was sent.
     */
    val channel: Channel

    /**
     * The author of this message.
     *
     * @return The author of this message.
     */
    val author: User

    /**
     * The content of this message.
     *
     * @return The content of this message.
     */
    val content: String

    /**
     * The time when this message was sent.
     *
     * @return The time when this message was sent.
     */
    val time: String

    /**
     * The time when this message was edited.
     *
     * @return The time when this message was edited.
     */
    val editedTime: String?

    /**
     * Gets weather this message is TTS.
     *
     * @return Weather this message is TTS.
     */
    val tts: Boolean

    /**
     * Gets weather this message mentions everyone.
     *
     * @return Weather this message mentions everyone.
     */
    val mentionEveryone: Boolean

    /**
     * The mentioned users.
     *
     * @return The mentioned users.
     */
    val mentionedUsers: List<User>

    /**
     * The mentioned roles.
     *
     * @return The mentioned roles.
     */
    val mentionedRoles: List<Role>

    /**
     * The mentioned channels.
     *
     * @return The mentioned channels.
     */
    val mentionedChannels: List<Channel>

    /**
     * The attachments.
     *
     * @return The attachments.
     */
    val attachments: List<Attachment>

    /**
     * The embedded contents.
     *
     * @return The embedded contents.
     */
    val embeds: List<Embed>

    /**
     * The reactions.
     *
     * @return The reactions.
     */
    val reactions: List<Reaction>

    /**
     * The nonce.
     *
     * @return The nonce.
     */
    val nonce: String?

    /**
     * Gets weather this message is pinned.
     *
     * @return Weather this message is pinned.
     */
    val pinned: Boolean

    /**
     * The webhook id of this message.
     *
     * @return The webhook id of this message.
     */
    val webhookId: GetterSnowFlake?

    /**
     * The type of this message.
     *
     * @return The type of this message.
     */
    val type: MessageType

    /**
     * The activity of this message.
     *
     * @return The activity of this message.
     */
    val activity: MessageActivity?

    /**
     * The application of this message.
     *
     * @return The application of this message.
     */
    val application: PartialApplication?

    /**
     * The message reference of this message.
     *
     * @return The message reference of this message.
     */
    val messageReference: MessageReference?

    /**
     * The flags of this message.
     *
     * @return The flags of this message.
     */
    val flags: MessageFlag?

    /**
     * The referenced message of this message.
     *
     * @return The referenced message of this message.
     */
    val referencedMessage: Message?

    /**
     * The interaction of this message.
     *
     * @return The interaction of this message.
     */
    val interaction: MessageInteraction?

    /**
     * The thread of this message.
     *
     * @return The thread of this message.
     */
    val thread: Channel?

    /**
     * The components of this message.
     *
     * @return The components of this message.
     */
    val components: List<Component>

    /**
     * The sticker items of this message.
     *
     * @return The sticker items of this message.
     */
    val stickerItems: List<StickerItem>

    /**
     * The position of this message.
     *
     * @return A generally increasing integer (there may be gaps or duplicates) that represents the
     *   approximate position of the message in a thread, it can be used to estimate the relative
     *   position of the message in a thread in company with total_message_sent on parent thread.
     */
    val position: Long?

    /**
     * Deletes this message.
     *
     * @return A future which returns nothing.
     */
    fun delete(): CompletableDeferred<NoResult> {
        return yde.restAPIMethodGetters
            .getMessageRestAPIMethods()
            .deleteMessage(channel.idAsLong, idAsLong)
    }
}
