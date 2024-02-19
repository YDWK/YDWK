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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.message.*
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.impl.entities.application.PartialApplicationImpl
import io.github.ydwk.yde.impl.entities.channel.DmChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildChannelImpl
import io.github.ydwk.yde.impl.entities.guild.RoleImpl
import io.github.ydwk.yde.impl.entities.message.*
import io.github.ydwk.yde.impl.entities.sticker.StickerItemImpl
import io.github.ydwk.yde.impl.interaction.message.ComponentImpl
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.formatZonedDateTime

class MessageImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : Message {
    override val channel: Channel
        get() =
            if (yde.getChannelById(json["channel_id"].asLong()) != null)
                yde.getChannelById(json["channel_id"].asLong())!!
            else throw IllegalStateException("Channel is null")

    override val author: User
        get() = UserImpl(json.get("author"), json.get("author").get("id").asLong(), yde)

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
            json.get("mentions").forEach { list.add(UserImpl(it, it.get("id").asLong(), yde)) }
            return list
        }

    override val mentionedRoles: List<Role>
        get() {
            val list = mutableListOf<Role>()
            json.get("mention_roles").forEach { list.add(RoleImpl(yde, it, it.get("id").asLong())) }
            return list
        }

    override val mentionedChannels: List<Channel>
        get() {
            val list = mutableListOf<Channel>()
            json.get("mention_channels").forEach {
                val channelType = ChannelType.fromInt(it["type"].asInt())
                if (ChannelType.isGuildChannel(channelType)) {
                    list.add(GuildChannelImpl(yde, it, it["id"].asLong()))
                } else {
                    list.add(DmChannelImpl(yde, it, it["id"].asLong()))
                }
            }
            return list
        }

    override val attachments: List<Attachment>
        get() {
            val list = mutableListOf<Attachment>()
            json.get("attachments").forEach {
                list.add(AttachmentImpl(yde, it, it.get("id").asLong()))
            }
            return list
        }

    override val embeds: List<Embed>
        get() {
            val list = mutableListOf<Embed>()
            json.get("embeds").forEach { list.add(EmbedImpl(yde, it)) }
            return list
        }

    override val reactions: List<Reaction>
        get() {
            val list = mutableListOf<Reaction>()
            json.get("reactions").forEach { list.add(ReactionImpl(yde, it)) }
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
        get() = MessageType.fromInt(json.get("type").asInt())

    override val activity: MessageActivity?
        get() = if (json.has("activity")) MessageActivityImpl(yde, json.get("activity")) else null

    override val application: PartialApplication?
        get() =
            if (json.has("application"))
                PartialApplicationImpl(
                    json.get("application"), json.get("application").get("id").asLong(), yde)
            else null

    override val messageReference: MessageReference?
        get() =
            if (json.has("message_reference"))
                MessageReferenceImpl(yde, json.get("message_reference"))
            else null

    override val flags: MessageFlag?
        get() = if (json.has("flags")) MessageFlag.fromLong(json.get("flags").asLong()) else null

    override val referencedMessage: Message?
        get() =
            if (json.has("referenced_message"))
                MessageImpl(
                    yde,
                    json.get("referenced_message"),
                    json.get("referenced_message").get("id").asLong())
            else null

    override val interaction: MessageInteraction?
        get() =
            if (json.has("interaction"))
                MessageInteractionImpl(
                    yde, json.get("interaction"), json.get("interaction").get("id").asLong())
            else null

    override val thread: Channel?
        get() =
            if (json.has("thread")) {
                val newJson = json.get("thread")
                val channelType = ChannelType.fromInt(newJson["type"].asInt())
                if (ChannelType.isGuildChannel(channelType)) {
                    GuildChannelImpl(yde, newJson, newJson["id"].asLong())
                } else {
                    DmChannelImpl(yde, newJson, newJson["id"].asLong())
                }
            } else null

    override val components: List<Component>
        get() = json.get("components").map { ComponentImpl(yde, it) }

    override val stickerItems: List<StickerItem>
        get() {
            val list = mutableListOf<StickerItem>()
            json.get("sticker_items").forEach {
                list.add(StickerItemImpl(yde, it, it.get("id").asLong()))
            }
            return list
        }

    override val position: Long?
        get() = if (json.has("position")) json.get("position").asLong() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
