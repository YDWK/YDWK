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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import io.github.ydwk.yde.entities.util.GenericEntity
import java.util.*

interface PermissionEntity : GenericEntity {
    /**
     * The permissions of this member.
     *
     * @return The permissions of this member.
     */
    val permissions: EnumSet<GuildPermission>

    /**
     * Checks if the member has a specific permission.
     *
     * @param permission The permission to check.
     * @return True if the member has the permission, false otherwise.
     */
    fun hasPermission(vararg permission: GuildPermission): Boolean

    /**
     * Checks if the member has a specific permission.
     *
     * @param permission The permission to check.
     * @return True if the member has the permission, false otherwise.
     */
    fun hasPermission(permission: Collection<GuildPermission>): Boolean
}
