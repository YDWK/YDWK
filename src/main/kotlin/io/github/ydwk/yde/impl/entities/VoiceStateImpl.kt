/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.util.EntityToStringBuilder

abstract class VoiceStateImpl(
    override val yde: YDE,
    override val json: JsonNode,
    private val backupGuild: Guild? = null,
    override val guild: Guild?,
    override val channel: GuildVoiceChannel?,
    override val user: User?,
    override val member: Member?,
    override val sessionId: String,
    override val isDeafened: Boolean,
    override val isMuted: Boolean,
    override val isSelfDeafened: Boolean,
    override val isSelfMuted: Boolean,
    override val isStreaming: Boolean,
    override val isSuppressed: Boolean,
    override val requestToSpeakTimestamp: String?,
) : VoiceState {

    class VoiceRegionImpl(
        override val yde: YDE,
        override val json: JsonNode,
        override val idAsLong: Long,
        override val isOptimal: Boolean,
        override val isDeprecated: Boolean,
        override val isCustom: Boolean,
        override var name: String,
    ) : VoiceState.VoiceRegion {
        override fun toString(): String {
            return EntityToStringBuilder(yde, this).name(name).toString()
        }
    }
}
