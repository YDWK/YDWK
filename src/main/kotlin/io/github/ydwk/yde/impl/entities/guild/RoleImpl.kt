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
package io.github.ydwk.yde.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import io.github.ydwk.yde.entities.guild.role.RoleTag
import io.github.ydwk.yde.util.EntityToStringBuilder
import java.awt.Color
import java.util.*

class RoleImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val permissions: EnumSet<GuildPermission>,
    override var color: Color,
    override var isHoisted: Boolean,
    override var icon: String?,
    override var unicodeEmoji: String?,
    override var position: Int,
    override var isManaged: Boolean,
    override var isMentionable: Boolean,
    override var tags: RoleTag?,
    override var rawPermissions: Long,
    override var name: String
) : Role {
    override fun hasPermission(vararg permission: GuildPermission): Boolean {
        return permissions.containsAll(listOf(*permission))
    }

    override fun hasPermission(permission: Collection<GuildPermission>): Boolean {
        return permissions.containsAll(permission)
    }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
