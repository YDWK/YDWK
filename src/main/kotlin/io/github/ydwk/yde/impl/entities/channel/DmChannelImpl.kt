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
package io.github.ydwk.yde.impl.entities.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.impl.entities.ChannelImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

class DmChannelImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : ChannelImpl(yde, json, idAsLong, false, true), DmChannel {

    override var lastMessageId: GetterSnowFlake? =
        if (json.has("last_message_id")) GetterSnowFlake.of(json["last_message_id"].asLong())
        else null

    override var recipient: User? =
        if (json.has("recipients"))
            UserImpl(json["recipients"][0], json["recipients"][0]["id"].asLong(), yde)
        else null

    override val type: ChannelType
        get() = ChannelType.DM

    override var name: String = if (json.has("name")) json["name"].asText() else ""

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
