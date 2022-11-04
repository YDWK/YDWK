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
package io.github.ydwk.ydwk.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.guild.enums.GuildPermission
import io.github.ydwk.ydwk.entities.guild.role.RoleTag
import io.github.ydwk.ydwk.impl.entities.guild.role.RoleTagImpl
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import java.awt.Color
import java.util.*

class RoleImpl(override val ydwk: YDWK, override val json: JsonNode, override val idAsLong: Long) :
  Role {

  override var color: Color = Color(json["color"].asInt())

  override var isHoisted: Boolean = json["hoist"].asBoolean()

  override var icon: String? = if (json["icon"].isNull) null else json["icon"].asText()

  override var unicodeEmoji: String? =
    if (json["unicode_emoji"].isNull) null else json["unicode_emoji"].asText()

  override var position: Int = json["position"].asInt()

  override fun hasPermission(vararg permission: GuildPermission): Boolean {
    return permissions.containsAll(listOf(*permission))
  }

  override fun hasPermission(permission: Collection<GuildPermission>): Boolean {
    return permissions.containsAll(permission)
  }

  override var isManaged: Boolean = json["managed"].asBoolean()

  override var isMentionable: Boolean = json["mentionable"].asBoolean()

  override var tags: RoleTag? = if (json["tags"].isNull) null else RoleTagImpl(ydwk, json["tags"])

  override var rawPermissions: Long = json["permissions"].asLong()

  override var permissions: EnumSet<GuildPermission> = GuildPermission.fromValues(rawPermissions)

  override var name: String = json["name"].asText()

  override fun toString(): String {
    return EntityToStringBuilder(this).name(this.name).toString()
  }
}
