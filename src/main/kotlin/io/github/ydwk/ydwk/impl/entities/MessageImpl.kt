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
package io.github.ydwk.ydwk.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.message.*
import io.github.ydwk.ydwk.entities.sticker.StickerItem
import io.github.ydwk.ydwk.impl.entities.application.PartialApplicationImpl
import io.github.ydwk.ydwk.impl.entities.guild.RoleImpl
import io.github.ydwk.ydwk.impl.entities.message.*
import io.github.ydwk.ydwk.impl.entities.sticker.StickerItemImpl
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.formatZonedDateTime

class MessageImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : Message {
    override val channel: Channel
        get() = ydwk.getChannel(json["channel_id"].asLong())!!

    override val author: User
        get() = UserImpl(json.get("author"), json.get("author").get("id").asLong(), ydwk)

    override val content: String
        get() = json.get("content").asText()

    override val time: String
        get() = formatZonedDateTime(json.get("timestamp").asText())

    override val editedTime: String?
        get() =
            if (json.has("edited_timestamp"))
                formatZonedDateTime(json.get("edited_timestamp").asText())
            else null

    override val tts: Boolean
        get() = json.get("tts").asBoolean()

    override val mentionEveryone: Boolean
        get() = json.get("mention_everyone").asBoolean()

    override val mentionedUsers: List<User>
        get() {
            val list = mutableListOf<User>()
            json.get("mentions").forEach { list.add(UserImpl(it, it.get("id").asLong(), ydwk)) }
            return list
        }

    override val mentionedRoles: List<Role>
        get() {
            val list = mutableListOf<Role>()
            json.get("mention_roles").forEach {
                list.add(RoleImpl(ydwk, it, it.get("id").asLong()))
            }
            return list
        }

    override val mentionedChannels: List<Channel>
        get() {
            val list = mutableListOf<Channel>()
            json.get("mention_channels").forEach {
                list.add(ChannelImpl(ydwk, it, it.get("id").asLong()))
            }
            return list
        }

    override val attachments: List<Attachment>
        get() {
            val list = mutableListOf<Attachment>()
            json.get("attachments").forEach {
                list.add(AttachmentImpl(ydwk, it, it.get("id").asLong()))
            }
            return list
        }

    override val embeds: List<Embed>
        get() {
            val list = mutableListOf<Embed>()
            json.get("embeds").forEach { list.add(EmbedImpl(ydwk, it)) }
            return list
        }

    override val reactions: List<Reaction>
        get() {
            val list = mutableListOf<Reaction>()
            json.get("reactions").forEach { list.add(ReactionImpl(ydwk, it)) }
            return list
        }

    override val nonce: String?
        get() = if (json.has("nonce")) json.get("nonce").asText() else null

    override val pinned: Boolean
        get() = json.get("pinned").asBoolean()

    override val webhookId: GetterSnowFlake?
        get() =
            if (json.has("webhook_id")) GetterSnowFlake.of(json.get("webhook_id").asLong())
            else null

    override val type: MessageType
        get() = MessageType.fromType(json.get("type").asInt())

    override val activity: MessageActivity?
        get() = if (json.has("activity")) MessageActivityImpl(ydwk, json.get("activity")) else null

    override val application: PartialApplication?
        get() =
            if (json.has("application"))
                PartialApplicationImpl(
                    json.get("application"), json.get("application").get("id").asLong(), ydwk)
            else null

    override val messageReference: MessageReference?
        get() =
            if (json.has("message_reference"))
                MessageReferenceImpl(ydwk, json.get("message_reference"))
            else null

    override val flags: MessageFlag?
        get() = if (json.has("flags")) MessageFlag.fromValue(json.get("flags").asLong()) else null

    override val referencedMessage: Message?
        get() =
            if (json.has("referenced_message"))
                MessageImpl(
                    ydwk,
                    json.get("referenced_message"),
                    json.get("referenced_message").get("id").asLong())
            else null

    override val interaction: MessageInteraction?
        get() =
            if (json.has("interaction"))
                MessageInteractionImpl(
                    ydwk, json.get("interaction"), json.get("interaction").get("id").asLong())
            else null

    override val thread: Channel?
        get() = TODO("Not yet implemented")

    override val components: List<MessageComponent>
        get() = TODO("Not yet implemented")

    override val stickerItems: List<StickerItem>
        get() {
            val list = mutableListOf<StickerItem>()
            json.get("sticker_items").forEach {
                list.add(StickerItemImpl(ydwk, it, it.get("id").asLong()))
            }
            return list
        }

    override val position: Long?
        get() = if (json.has("position")) json.get("position").asLong() else null
}
