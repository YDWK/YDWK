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
package io.github.ydwk.ydwk.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.guild.Category
import io.github.ydwk.ydwk.entities.channel.guild.text.GuildTextChannel
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.rest.EndPoint
import java.util.concurrent.CompletableFuture
import okhttp3.RequestBody.Companion.toRequestBody

class GuildTextChannelTest(
    override val json: JsonNode,
    override val idAsLong: Long,
    override val ydwk: YDWK
) : GuildTextChannel {
    override val topic: String
        get() = TODO("Not yet implemented")
    override val nsfw: Boolean
        get() = TODO("Not yet implemented")
    override val defaultAutoArchiveDuration: Int
        get() = TODO("Not yet implemented")
    override val position: Int
        get() = TODO("Not yet implemented")
    override val parent: Category?
        get() = TODO("Not yet implemented")
    override val guild: Guild
        get() = TODO("Not yet implemented")
    override val type: ChannelType
        get() = TODO("Not yet implemented")
    override var name: String
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun sendMessage(message: String): CompletableFuture<GuildTextChannel> {
        val body = ydwk.objectNode.put("content", message).toString().toRequestBody()
        return ydwk.restApiManager.put(body, EndPoint.ChannelEndpoint.CREATE_MESSAGE, id).execute {
            result ->
            GuildTextChannelTest(result.jsonBody!!, result.jsonBody!!["id"].asLong(), ydwk)
        }
    }

    override fun sendEmbed(embed: Embed): CompletableFuture<GuildTextChannel> {
        TODO("Not yet implemented")
    }
}
