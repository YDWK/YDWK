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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake
import kotlinx.coroutines.CompletableDeferred

interface VoiceState : GenericEntity {

    /**
     * The guild this voice state is for.
     *
     * @return The guild this voice state is for.
     */
    val guild: Guild?

    /**
     * The channel this voice state is for.
     *
     * @return The channel this voice state is for.
     */
    val channel: GuildVoiceChannel?

    /**
     * The user this voice state is for.
     *
     * @return The user this voice state is for.
     */
    val user: User?

    /**
     * The guild member this voice state is for.
     *
     * @return The guild member this voice state is for.
     */
    val member: Member?

    /**
     * The session id of this voice state.
     *
     * @return The session id of this voice state.
     */
    val sessionId: String

    /**
     * Whether this voice state is deafened.
     *
     * @return Whether this voice state is deafened.
     */
    val isDeafened: Boolean

    /**
     * Whether this voice state is muted.
     *
     * @return Whether this voice state is muted.
     */
    val isMuted: Boolean

    /**
     * Whether this voice state is self deafened.
     *
     * @return Whether this voice state is self deafened.
     */
    val isSelfDeafened: Boolean

    /**
     * Whether this voice state is self muted.
     *
     * @return Whether this voice state is self muted.
     */
    val isSelfMuted: Boolean

    /**
     * Whether this voice state is streaming.
     *
     * @return Whether this voice state is streaming.
     */
    val isStreaming: Boolean

    /**
     * Whether this voice state is suppressed.
     *
     * @return Whether this voice state is suppressed.
     */
    val isSuppressed: Boolean

    /**
     * The time at which the user requested to speak.
     *
     * @return The time at which the user requested to speak.
     */
    val requestToSpeakTimestamp: String?

    /**
     * Requests the voice region of this voice state.
     *
     * @return The voice region of this voice state.
     */
    suspend fun requestVoiceRegion(): CompletableDeferred<List<VoiceRegion>> {
        return yde.restAPIMethodGetters.getVoiceRestAPIMethods().requestVoiceRegions()
    }

    interface VoiceRegion : GenericEntity, SnowFlake, NameAbleEntity {

        /**
         * Whether the single server in this voice region is optimal.
         *
         * @return true for a single server that is closest to the current user's client.
         */
        val isOptimal: Boolean

        /**
         * Whether this voice region is deprecated.
         *
         * @return Whether this voice region is deprecated.
         */
        val isDeprecated: Boolean

        /**
         * Whether this voice region is custom.
         *
         * @return Whether this voice region is custom.
         */
        val isCustom: Boolean
    }
}
