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
package io.github.ydwk.yde.impl.interaction.application.sub

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.message.Embed
import io.github.ydwk.yde.entities.message.MessageFlag
import io.github.ydwk.yde.impl.interaction.message.ComponentImpl
import io.github.ydwk.yde.interaction.application.sub.Reply
import io.github.ydwk.yde.interaction.sub.InteractionCallbackType
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.result.NoResult
import kotlinx.coroutines.CompletableDeferred
import okhttp3.RequestBody.Companion.toRequestBody

class ReplyImpl(
    val yde: YDE,
    val content: String?,
    val embed: Embed?,
    val id: String,
    val token: String,
) : Reply {
    private var isEphemeral: Boolean = false
    private var isTTS: Boolean = false
    private var actionRows = mutableListOf<ComponentImpl.ComponentCreator>()
    private var flags = mutableListOf<MessageFlag>()

    override fun setEphemeral(isEphemeral: Boolean): Reply {
        this.isEphemeral = isEphemeral
        return this
    }

    override fun setTTS(isTTS: Boolean): Reply {
        this.isTTS = isTTS
        return this
    }

    override fun addFlags(vararg flags: MessageFlag): Reply {
        this.flags.addAll(flags)
        return this
    }

    override fun addFlags(flags: List<MessageFlag>): Reply {
        this.flags.addAll(flags)
        return this
    }

    override fun addActionRow(actionRow: ComponentImpl.ComponentCreator): Reply {
        actionRows.add(actionRow)
        return this
    }

    override fun triggerWithFuture(): CompletableDeferred<NoResult> {
        val mainBody =
            yde.objectNode.put(
                "type", InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE.getType())

        val secondBody = yde.objectNode

        if (content != null) secondBody.put("content", content)

        if (embed != null) {
            val embedArray = yde.objectNode.arrayNode()
            embedArray.add(embed.json)
            secondBody.set<ArrayNode>("embeds", embedArray)
        }

        if (isTTS) secondBody.put("tts", true)

        if (flags.isNotEmpty()) {
            if (isEphemeral && !flags.contains(MessageFlag.EPHEMERAL)) {
                flags.add(MessageFlag.EPHEMERAL)
            }

            var flagValue: Long = 0
            for (flag in flags) {
                flagValue = flagValue or flag.getValue()
            }

            secondBody.put("flags", flagValue)
        }

        if (actionRows.isNotEmpty()) {
            for (actionRow in actionRows) {
                secondBody.set<ArrayNode>("components", actionRow.json)
            }
        }

        mainBody.set<JsonNode>("data", secondBody)

        return yde.restApiManager
            .post(
                mainBody.toString().toRequestBody(),
                EndPoint.ApplicationCommandsEndpoint.REPLY_TO_SLASH_COMMAND,
                id,
                token)
            .executeWithNoResult()
    }
}
