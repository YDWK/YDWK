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
package io.github.ydwk.ydwk.impl.interaction.application.sub

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.message.MessageFlag
import io.github.ydwk.ydwk.impl.interaction.message.ComponentImpl
import io.github.ydwk.ydwk.interaction.application.sub.Reply
import io.github.ydwk.ydwk.interaction.message.ActionRow
import io.github.ydwk.ydwk.interaction.sub.InteractionCallbackType
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.rest.result.NoResult
import java.util.concurrent.CompletableFuture
import okhttp3.RequestBody.Companion.toRequestBody

class ReplyImpl(
    val ydwk: YDWK,
    val content: String?,
    val embed: Embed?,
    val id: String,
    val token: String
) : Reply {
    private var isEphemeral: Boolean = false
    private var isTTS: Boolean = false
    private var actionRow: ActionRow? = null
    private var actionRows = mutableListOf<ComponentImpl.ComponentCreator>()

    override fun isEphemeral(isEphemeral: Boolean): Reply {
        this.isEphemeral = isEphemeral
        return this
    }

    override fun isTTS(isTTS: Boolean): Reply {
        this.isTTS = isTTS
        return this
    }

    override fun addActionRow(actionRow: ComponentImpl.ComponentCreator): Reply {
        actionRows.add(actionRow)
        return this
    }

    override fun replyWithFuture(): CompletableFuture<NoResult> {
        val mainBody =
            ydwk.objectNode.put("type", InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE.toInt())

        val secondBody = ydwk.objectNode

        if (content != null) secondBody.put("content", content)

        if (embed != null) {
            val embedArray = ydwk.objectNode.arrayNode()
            embedArray.add(embed.json)
            secondBody.set<ArrayNode>("embeds", embedArray)
        }

        if (isTTS) secondBody.put("tts", true)

        if (isEphemeral) secondBody.put("flags", MessageFlag.EPHEMERAL.getValue())

        // secondBody.set<ArrayNode>("components", actionRow?.toJson())

        for (actionRow in actionRows) {
            secondBody.set<ArrayNode>("components", actionRow.json)
        }

        mainBody.set<JsonNode>("data", secondBody)

        return ydwk.restApiManager
            .post(
                mainBody.toString().toRequestBody(),
                EndPoint.ApplicationCommandsEndpoint.REPLY_TO_SLASH_COMMAND,
                id,
                token)
            .executeWithNoResult()
    }
}
