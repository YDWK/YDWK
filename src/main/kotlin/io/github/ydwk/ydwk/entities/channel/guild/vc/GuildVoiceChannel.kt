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
package io.github.ydwk.ydwk.entities.channel.guild.vc

import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.voice.VoiceConnection
import java.util.concurrent.CompletableFuture

interface GuildVoiceChannel : GuildChannel {

    /**
     * Joins the voice channel.
     *
     * @param isMuted Whether the bot should be muted.
     * @param isDeafened Whether the bot should be deafened.
     * @return The [VoiceConnection] object.
     */
    fun join(isMuted: Boolean = false, isDeafened: Boolean = true): VoiceConnection {
        return joinCompletableFuture(isMuted, isDeafened).join()
    }

    /**
     * Connect the voice channel.
     *
     * @param isMuted Whether the bot should be muted.
     * @param isDeafened Whether the bot should be deafened.
     * @return A [CompletableFuture] that completes with the [VoiceConnection] when the bot has
     * joined the voice channel.
     */
    fun joinCompletableFuture(
        isMuted: Boolean = false,
        isDeafened: Boolean = true
    ): CompletableFuture<VoiceConnection>

    /**
     * Gets the bitrate (in bits) of the voice channel
     *
     * @return the bitrate
     */
    var bitrate: Int

    /**
     * Gets the user limit of the voice channel
     *
     * @return the user limit
     */
    var userLimit: Int

    /**
     * Gets the rate limit per user of the voice channel
     *
     * @return the rate limit per user
     */
    var rateLimitPerUser: Int
}
