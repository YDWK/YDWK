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
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildChannel
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import java.util.*

class GuildCategoryImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : GuildCategory, GenericGuildChannelImpl(ydwk, json, idAsLong) {

    override val channels: List<GenericGuildChannel>
        get() =
            Collections.unmodifiableList(
                guild.getUnorderedChannels
                    .stream()
                    .filter { this.asGuildCategory()?.equals(this) ?: false }
                    .map { it as GenericGuildChannel }
                    .toList())

    override val nsfw: Boolean
        get() = json.has("nsfw") && json["nsfw"].asBoolean()
}
