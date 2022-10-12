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
package io.github.ydwk.ydwk.impl.entities.channel

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.impl.entities.ChannelImpl
import io.github.ydwk.ydwk.rest.EndPoint
import java.util.concurrent.CompletableFuture
import okhttp3.RequestBody.Companion.toRequestBody

class TextChannelImpl<T : Channel>(ydwk: YDWK, json: JsonNode, idAsLong: Long) :
    TextChannel<T>, ChannelImpl(ydwk, json, idAsLong) {

    override fun sendMessage(message: String, tts: Boolean): CompletableFuture<T> {
        val body = ydwk.objectNode.put("content", message).put("tts", tts)
        return ydwk.restApiManager
            .put(body.toString().toRequestBody(), EndPoint.ChannelEndpoint.CREATE_MESSAGE, id)
            .execute { result ->
                ChannelImpl(ydwk, result.jsonBody!!, result.jsonBody!!["id"].asLong()) as T
            }
    }

    override fun sendEmbed(embed: Embed, tts: Boolean): CompletableFuture<T> {
        val body = ydwk.objectNode
        body.set<ArrayNode>("embed", embed.json)
        body.put("tts", tts)
        return ydwk.restApiManager
            .put(body.toString().toRequestBody(), EndPoint.ChannelEndpoint.CREATE_MESSAGE, id)
            .execute { result ->
                ChannelImpl(ydwk, result.jsonBody!!, result.jsonBody!!["id"].asLong()) as T
            }
    }
}
