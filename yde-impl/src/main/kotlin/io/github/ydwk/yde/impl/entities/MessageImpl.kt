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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.message.*
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

internal class MessageImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val channel: Channel,
    override val author: User,
    override val content: String,
    override val time: String,
    override val editedTime: String?,
    override val tts: Boolean,
    override val mentionEveryone: Boolean,
    override val mentionedUsers: List<User>,
    override val mentionedRoles: List<Role>,
    override val mentionedChannels: List<Channel>,
    override val attachments: List<Attachment>,
    override val embeds: List<Embed>,
    override val reactions: List<Reaction>,
    override val nonce: String?,
    override val pinned: Boolean,
    override val webhookId: GetterSnowFlake?,
    override val type: MessageType,
    override val activity: MessageActivity?,
    override val application: PartialApplication?,
    override val messageReference: MessageReference?,
    override val flags: MessageFlag?,
    override val referencedMessage: Message?,
    override val interaction: MessageInteraction?,
    override val thread: Channel?,
    override val components: List<Component>,
    override val stickerItems: List<StickerItem>,
    override val position: Long?,
) : Message {
    override suspend fun delete(): RestResult<NoResult> {
        return yde.restAPIMethodGetters
            .getMessageRestAPIMethods()
            .deleteMessage(channel.idAsLong, idAsLong)
    }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
