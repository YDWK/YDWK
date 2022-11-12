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
package io.github.ydwk.ydwk.impl.entities.channel.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.ydwk.impl.entities.GuildImpl
import io.github.ydwk.ydwk.voice.VoiceConnection
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import java.util.concurrent.CompletableFuture

open class GuildVoiceChannelImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : GuildVoiceChannel, GenericGuildVoiceChannelImpl(ydwk, json, idAsLong) {

    override fun join(isMuted: Boolean, isDeafened: Boolean): CompletableFuture<VoiceConnection> {
        guild.voiceConnection?.disconnect()
        val future = CompletableFuture<VoiceConnection>()
        val connection = VoiceConnectionImpl(guild.idAsLong, ydwk)
        (guild as GuildImpl).setPendingVoiceConnection(connection, isMuted, isDeafened, future)
        (guild as GuildImpl).setVoiceConnection(connection)
        return future
    }

    override var bitrate: Int = json["bitrate"].asInt()

    override var userLimit: Int = json["user_limit"].asInt()

    override var rateLimitPerUser: Int = json["rate_limit_per_user"].asInt()

    override var position: Int = json["position"].asInt()

    override var parent: GuildCategory? = ydwk.getCategoryById(json["parent_id"].asText())

    override val guild: Guild
        get() =
            if (ydwk.getGuildById(json["guild_id"].asText()) != null)
                ydwk.getGuildById(json["guild_id"].asText())!!
            else throw IllegalStateException("Guild is null")

    override var name: String
        get() = json["name"].asText()
        set(value) {}
}
