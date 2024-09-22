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
import io.github.ydwk.yde.entities.PartialGuild
import io.github.ydwk.yde.entities.guild.enums.GuildFeature
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl

internal class PartialGuildImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override var icon: String,
    override var banner: String,
    override var isOwner: Boolean,
    override var permissions: String,
    override var features: Set<GuildFeature>,
    override var approximateMemberCount: Int,
    override var approximatePresenceCount: Int
) : PartialGuild, ToStringEntityImpl<PartialGuild>(yde, PartialGuild::class.java)
