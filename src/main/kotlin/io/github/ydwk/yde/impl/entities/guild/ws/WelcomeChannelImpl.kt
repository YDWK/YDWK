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
package io.github.ydwk.yde.impl.entities.guild.ws

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.guild.ws.WelcomeChannel
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

class WelcomeChannelImpl(override val yde: YDE, override val json: JsonNode) : WelcomeChannel {

    override val channelId: GetterSnowFlake
        get() = GetterSnowFlake.of(json.get("channel_id").asLong())

    override var description: String = json.get("description").asText()

    override val emojiId: GetterSnowFlake?
        get() =
            if (json.has("emoji_id")) GetterSnowFlake.of(json.get("emoji_id").asLong()) else null

    override var emojiName: String? =
        if (json.has("emoji_name")) json.get("emoji_name").asText() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
