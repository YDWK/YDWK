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
package io.github.ydwk.yde.impl.entities.channel.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import org.slf4j.LoggerFactory

open class GuildVoiceChannelImpl(yde: YDE, json: JsonNode, idAsLong: Long) :
    GuildVoiceChannel, GuildChannelImpl(yde, json, idAsLong) {
    private val logger = LoggerFactory.getLogger(GuildVoiceChannelImpl::class.java)

    override var bitrate: Int = json["bitrate"].asInt()

    override var userLimit: Int = json["user_limit"].asInt()

    override var rateLimitPerUser: Int = json["rate_limit_per_user"].asInt()
}
