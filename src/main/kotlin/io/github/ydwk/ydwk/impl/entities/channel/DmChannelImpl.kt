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
package io.github.ydwk.ydwk.impl.entities.channel

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.GetterSnowFlake

class DmChannelImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : DmChannel {

    override var lastMessageId: GetterSnowFlake? =
        if (json.has("last_message_id")) GetterSnowFlake.of(json["last_message_id"].asLong())
        else null

    override var recipient: User? =
        if (json.has("recipients"))
            UserImpl(json["recipients"][0], json["recipients"][0]["id"].asLong(), ydwk)
        else null

    override val type: ChannelType
        get() = ChannelType.DM

    override var name: String = json["name"].asText()

    override fun toString(): String {
        return EntityToStringBuilder(this).name(this.name).toString()
    }
}
