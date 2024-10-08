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
import io.github.ydwk.yde.entities.Sticker
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.sticker.StickerFormatType
import io.github.ydwk.yde.entities.sticker.StickerType
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake

internal class StickerImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val packId: GetterSnowFlake?,
    override var description: String?,
    override var tags: List<String>,
    override var type: StickerType,
    override var formatType: StickerFormatType,
    override var available: Boolean,
    override val guildId: GetterSnowFlake?,
    override var user: User?,
    override var sortvarue: Int?,
    override var name: String,
) : Sticker {
    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
