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
import io.github.ydwk.ydwk.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.ydwk.impl.entities.GuildImpl
import io.github.ydwk.ydwk.voice.VoiceConnection
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import java.util.*
import java.util.concurrent.CompletableFuture
import org.slf4j.LoggerFactory

open class GuildVoiceChannelImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : GuildVoiceChannel, GuildChannelImpl(ydwk, json, idAsLong) {
    private val logger = LoggerFactory.getLogger(GuildVoiceChannelImpl::class.java)

    override fun join(isMuted: Boolean, isDeafened: Boolean): VoiceConnection {
        if (guild.voiceConnection != null) {
            logger.debug("Already connected to a voice channel!")
            guild.voiceConnection!!.disconnect()
        } else {
            logger.debug("Connecting to a voice channel!")
            CompletableFuture.completedFuture(null)
        }

        val future = CompletableFuture<VoiceConnection>()
        logger.debug("Connecting to voice channel $name")
        val connection = VoiceConnectionImpl(this, ydwk, future, isMuted, isDeafened)
        (guild as GuildImpl).setPendingVoiceConnection(connection)
        return future
            .thenApply {
                logger.debug("Connected to voice channel $name")
                (guild as GuildImpl).setVoiceConnection(it as VoiceConnectionImpl)
                it
            }
            .join()
    }

    override var bitrate: Int = json["bitrate"].asInt()

    override var userLimit: Int = json["user_limit"].asInt()

    override var rateLimitPerUser: Int = json["rate_limit_per_user"].asInt()
}
