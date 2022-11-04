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
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.channel.guild.GenericGuildChannel
import io.github.ydwk.ydwk.entities.channel.guild.GuildCategory
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import java.util.*

class GuildCategoryImpl(
  override val ydwk: YDWK,
  override val json: JsonNode,
  override val idAsLong: Long
) : GuildCategory {

  override val channels: List<GenericGuildChannel>
    get() = ydwk.getChannelsByCategory(this)

  override val nsfw: Boolean
    get() = json.has("nsfw") && json["nsfw"].asBoolean()

  override val guild: Guild = ydwk.getGuildById(json["guild_id"].asText())!!

  override var position: Int = json["position"].asInt()

  override var parent: GuildCategory? = null

  override val type: ChannelType = ChannelType.CATEGORY

  override var name: String = json["name"].asText()

  override fun toString(): String {
    return EntityToStringBuilder(this).name(this.name).toString()
  }
}
