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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Sticker
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.sticker.StickerFormatType
import io.github.ydwk.yde.entities.sticker.StickerType
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

class StickerImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : Sticker {
    override val packId: GetterSnowFlake?
        get() = if (json.has("pack_id")) GetterSnowFlake.of(json.get("pack_id").asLong()) else null

    override var description: String? =
        if (json.has("description")) json.get("description").asText() else null

    override var tags: List<String> =
        if (json.has("tags")) json.get("tags").map { it.asText() } else emptyList()

    override var type: StickerType = StickerType.fromInt(json.get("type").asInt())

    override var formatType: StickerFormatType =
        StickerFormatType.fromInt(json.get("format_type").asInt())

    override var available: Boolean = json.get("available").asBoolean()

    override val guild: Guild?
        get() = if (json.has("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null

    override var user: User? =
        if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), yde) else null

    override var sortvarue: Int? =
        if (json.has("sort_value")) json.get("sort_value").asInt() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }

    override var name: String = json.get("name").asText()
}
