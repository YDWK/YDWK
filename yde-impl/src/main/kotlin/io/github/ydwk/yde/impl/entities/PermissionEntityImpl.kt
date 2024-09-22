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

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.PermissionEntity
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import io.github.ydwk.yde.impl.util.getPermissions
import java.util.*

internal class PermissionEntityImpl(
    private val guild: Guild,
    private val isOwner: Boolean,
    private val roles: List<Role>,
    private val isTimedOut: Boolean?,
    override val permissions: EnumSet<GuildPermission> =
        GuildPermission.getValues(getPermissions(guild, isOwner, roles, isTimedOut ?: false))
) : PermissionEntity {

    override fun hasPermission(vararg permission: GuildPermission): Boolean {
        return permissions.containsAll(permission.toList())
    }

    override fun hasPermission(permission: Collection<GuildPermission>): Boolean {
        return permissions.containsAll(permission)
    }
}
