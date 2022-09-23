/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.impl.entities.guild.role

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.entities.guild.role.RoleTag
import io.github.realyusufismail.ydwk.util.GetterSnowFlake

class RoleTagImpl(override val ydwk: YDWK, override val json: JsonNode) : RoleTag {

    override val botId: GetterSnowFlake?
        get() = if (json.has("bot_id")) GetterSnowFlake.of(json.get("bot_id").asLong()) else null

    override val integrationId: GetterSnowFlake?
        get() =
            if (json.has("integration_id")) GetterSnowFlake.of(json.get("integration_id").asLong())
            else null
}
