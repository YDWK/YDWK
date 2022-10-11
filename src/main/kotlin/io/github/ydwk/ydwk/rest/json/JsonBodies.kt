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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.entities.message.MessageFlag
import io.github.ydwk.ydwk.interaction.sub.InteractionCallbackType

fun replyJsonBody(
    ydwk: YDWK,
    content: String? = null,
    embeds: List<Embed> = emptyList(),
    tts: Boolean? = null,
    flags: List<MessageFlag> = emptyList(),
    allowedMentions: List<String> = emptyList(),
): ObjectNode {
    val mainBody =
        ydwk.objectNode.put("type", InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE.toInt())
    val secondBody = ydwk.objectNode

    if (content != null) secondBody.put("content", content)

    if (tts != null) secondBody.put("tts", tts)

    if (embeds.isNotEmpty()) {
        val embedArray = ydwk.objectNode.arrayNode()
        embeds.forEach { embedArray.add(it.toJson()) }
        secondBody.set<ArrayNode>("embeds", embedArray)
    }

    if (flags.isNotEmpty()) secondBody.put("flags", flags.sumOf { it.getValue() })

    if (allowedMentions.isNotEmpty()) {
        val allowedMentionsArray = ydwk.objectNode.arrayNode()
        allowedMentions.forEach { allowedMentionsArray.add(it) }
        secondBody.set<ArrayNode>("allowed_mentions", allowedMentionsArray)
    }

    println(mainBody.set<JsonNode?>("data", secondBody).toPrettyString())
    return mainBody.set("data", secondBody)
}

fun replyJsonBody(
    ydwk: YDWK,
    embeds: List<Embed> = emptyList(),
    tts: Boolean? = null,
    flags: List<MessageFlag> = emptyList(),
    allowedMentions: List<String> = emptyList()
): ObjectNode {
    return replyJsonBody(ydwk, null, embeds, tts, flags, allowedMentions)
}

fun replyJsonBody(
    ydwk: YDWK,
    embeds: Embed,
    tts: Boolean? = null,
    flag: MessageFlag? = null
): ObjectNode {
    return replyJsonBody(
        ydwk,
        null,
        listOf(embeds),
        tts,
        if (flag != null) listOf(flag) else emptyList(),
        emptyList())
}

fun replyJsonBody(
    ydwk: YDWK,
    embeds: Embed,
    tts: Boolean? = null,
    flag: MessageFlag? = null,
    allowedMentions: List<String> = emptyList()
): ObjectNode {
    return replyJsonBody(
        ydwk,
        null,
        listOf(embeds),
        tts,
        if (flag != null) listOf(flag) else emptyList(),
        allowedMentions)
}

fun replyJsonBody(
    ydwk: YDWK,
    content: String,
    tts: Boolean? = null,
    flag: MessageFlag? = null
): ObjectNode {
    return replyJsonBody(
        ydwk, content, listOf(), tts, if (flag != null) listOf(flag) else emptyList(), emptyList())
}

fun replyJsonBody(
    ydwk: YDWK,
    content: String,
    tts: Boolean? = null,
    flag: MessageFlag? = null,
    allowedMentions: List<String> = emptyList()
): ObjectNode {
    return replyJsonBody(
        ydwk,
        content,
        listOf(),
        tts,
        if (flag != null) listOf(flag) else emptyList(),
        allowedMentions)
}
