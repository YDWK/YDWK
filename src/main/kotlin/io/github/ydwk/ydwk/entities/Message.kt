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

import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildChannel
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildTextChannel
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.message.*
import io.github.ydwk.ydwk.entities.sticker.StickerItem
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.SnowFlake

interface Message : SnowFlake, GenericEntity {
  /**
   * Gets the channel where this message was sent.
   *
   * @return The channel where this message was sent.
   */
  val channel: GenericGuildTextChannel

  /**
   * Gets the author of this message.
   *
   * @return The author of this message.
   */
  val author: User

  /**
   * Gets the content of this message.
   *
   * @return The content of this message.
   */
  val content: String

  /**
   * Gets the time when this message was sent.
   *
   * @return The time when this message was sent.
   */
  val time: String

  /**
   * Gets the time when this message was edited.
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
   * Gets the mentioned users.
   *
   * @return The mentioned users.
   */
  val mentionedUsers: List<User>

  /**
   * Gets the mentioned roles.
   *
   * @return The mentioned roles.
   */
  val mentionedRoles: List<Role>

  /**
   * Gets the mentioned channels.
   *
   * @return The mentioned channels.
   */
  val mentionedChannels: List<GenericGuildChannel>

  /**
   * Gets the attachments.
   *
   * @return The attachments.
   */
  val attachments: List<Attachment>

  /**
   * Gets the embedded contents.
   *
   * @return The embedded contents.
   */
  val embeds: List<Embed>

  /**
   * Gets the reactions.
   *
   * @return The reactions.
   */
  val reactions: List<Reaction>

  /**
   * Gets the nonce.
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
   * Gets the webhook id of this message.
   *
   * @return The webhook id of this message.
   */
  val webhookId: GetterSnowFlake?

  /**
   * Gets the type of this message.
   *
   * @return The type of this message.
   */
  val type: MessageType

  /**
   * Gets the activity of this message.
   *
   * @return The activity of this message.
   */
  val activity: MessageActivity?

  /**
   * Gets the application of this message.
   *
   * @return The application of this message.
   */
  val application: PartialApplication?

  /**
   * Gets the message reference of this message.
   *
   * @return The message reference of this message.
   */
  val messageReference: MessageReference?

  /**
   * Gets the flags of this message.
   *
   * @return The flags of this message.
   */
  val flags: MessageFlag?

  /**
   * Gets the referenced message of this message.
   *
   * @return The referenced message of this message.
   */
  val referencedMessage: Message?

  /**
   * Gets the interaction of this message.
   *
   * @return The interaction of this message.
   */
  val interaction: MessageInteraction?

  /**
   * Gets the thread of this message.
   *
   * @return The thread of this message.
   */
  val thread: GenericGuildChannel?

  /**
   * Gets the components of this message.
   *
   * @return The components of this message.
   */
  val components: List<MessageComponent>

  /**
   * Gets the sticker items of this message.
   *
   * @return The sticker items of this message.
   */
  val stickerItems: List<StickerItem>

  /**
   * Gets the position of this message.
   *
   * @return A generally increasing integer (there may be gaps or duplicates) that represents the
   * approximate position of the message in a thread, it can be used to estimate the relative
   * position of the message in a thread in company with total_message_sent on parent thread.
   */
  val position: Long?
}
