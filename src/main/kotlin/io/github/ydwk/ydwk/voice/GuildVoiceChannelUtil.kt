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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.voice

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.ydwk.entity.VoiceConnection
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import java.util.concurrent.CompletableFuture
import org.slf4j.LoggerFactory

/**
 * Connects to the voice channel.
 *
 * @param muted Whether the bot should be muted
 * @param deafen Whether the bot should be deafened
 */
fun GuildVoiceChannel.joinVc(muted: Boolean, deafen: Boolean): CompletableFuture<VoiceConnection> {
    val future = CompletableFuture<VoiceConnection>()

    fun createVoiceConnectionAsync(
        connectFuture: CompletableFuture<VoiceConnection>,
        guild: Guild,
        voiceChannel: GuildVoiceChannel,
        muted: Boolean,
        deafen: Boolean
    ): VoiceConnectionImpl {
        return VoiceConnectionImpl(connectFuture, guild, voiceChannel, muted, deafen)
    }

    future
        .completeAsync { createVoiceConnectionAsync(future, guild, this, muted, deafen) }
        .thenApply {
            (guild as GuildImpl).setVoiceConnection(it as VoiceConnectionImpl)
            it
        }
        .exceptionally {
            (yde as YDEImpl).logger.error("Error creating voice connection for channel $name", it)
            null
        }

    return future
}

private var voiceConnection: VoiceConnectionImpl? = null

/**
 * Sets the voice connection for the guild
 *
 * @param vc The voice connection.
 */
fun GuildImpl.setVoiceConnection(vc: VoiceConnectionImpl) {
    voiceConnection = vc
}

/** Removes the voice connection from the guild. */
fun GuildImpl.removeVoiceConnection() {
    voiceConnection = null
}

/** Gets the voice connection. */
fun GuildImpl.getVoiceConnection(): VoiceConnectionImpl? {
    return voiceConnection
}

fun Member.leaveVC(): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
        if (this.voiceState != null) {
            try {
                voiceConnection?.disconnect().also {
                    LoggerFactory.getLogger(Member::class.java).info("Disconnection successful.")
                }
                    ?: LoggerFactory.getLogger(Member::class.java).warn("voice connection is null")
            } catch (e: Exception) {
                LoggerFactory.getLogger(Member::class.java)
                    .error("Error occurred while disconnecting.", e)
            }
        } else {
            LoggerFactory.getLogger(Member::class.java).info("Voice state is null")
        }
    }
}
