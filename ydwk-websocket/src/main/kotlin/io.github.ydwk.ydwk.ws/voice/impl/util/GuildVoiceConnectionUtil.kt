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
package io.github.ydwk.ydwk.voice.impl.util

import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.GuildImpl
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.voice.VoiceConnection
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import java.util.concurrent.CompletableFuture

fun GuildVoiceChannel.getVoiceConnection(
    isMuted: Boolean,
    isDeafened: Boolean,
): CompletableFuture<VoiceConnection> {
    val future = CompletableFuture<VoiceConnection>()
    future
        .completeAsync { VoiceConnectionImpl(this, (yde as YDWK), future, isMuted, isDeafened) }
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

fun GuildVoiceChannel.leave(): CompletableFuture<Void> {
    val future = CompletableFuture<Void>()
    (guild as GuildImpl).getVoiceConnection()?.disconnect()
    future
        .completeAsync { null }
        .exceptionally {
            (yde as YDEImpl).logger.error("Error destroying voice connection for channel $name", it)
            null
        }
    return future
}

fun GuildVoiceChannel.leaveNow(): Void? {
    return leave().join()
}

fun GuildVoiceChannel.join(): CompletableFuture<VoiceConnection> {
    return getVoiceConnection(false, false)
}

fun GuildVoiceChannel.joinNow(): VoiceConnection? {
    return join().join()
}

var voiceConnection: VoiceConnectionImpl? = null

fun GuildImpl.getVoiceConnection(): VoiceConnectionImpl? {
    return voiceConnection
}

fun GuildImpl.setVoiceConnection(vc: VoiceConnectionImpl) {
    voiceConnection = vc
}

fun GuildImpl.removeVoiceConnection() {
    voiceConnection = null
}
