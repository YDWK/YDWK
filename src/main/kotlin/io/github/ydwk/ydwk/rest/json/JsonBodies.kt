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
package io.github.ydwk.ydwk.rest.json

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.message.MessageFlag
import io.github.ydwk.ydwk.interaction.sub.InteractionCallbackType

fun replyJsonBody(
    ydwk: YDWK,
    content: String? = null,
    embeds: List<Embed>? = null,
    tts: Boolean? = null,
    flags: List<MessageFlag>? = null,
    allowedMentions: List<String>? = null
): ObjectNode {
    val mainBody =
        ydwk.objectNode.put("type", InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE.toInt())
    val secondBody = ydwk.objectNode
    if (content != null) secondBody.put("content", content)
    if (embeds != null)
        secondBody.set<ArrayNode>(
            "embeds", ydwk.objectNode.arrayNode().addAll(embeds.map { it.json }))
    if (tts != null) secondBody.put("tts", tts)
    if (flags != null) secondBody.put("flags", flags.sumOf { it.getValue() })
    return mainBody.set("data", secondBody)
}
