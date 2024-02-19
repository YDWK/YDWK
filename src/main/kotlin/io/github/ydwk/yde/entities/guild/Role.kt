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
package io.github.ydwk.yde.entities.guild

import io.github.ydwk.yde.entities.PermissionEntity
import io.github.ydwk.yde.entities.guild.role.RoleTag
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake
import java.awt.Color

/** This class is used to represent a discord guild role entity. */
interface Role : SnowFlake, GenericEntity, NameAbleEntity, PermissionEntity {
    /**
     * The color of this role.
     *
     * @return The color of this role.
     */
    var color: Color

    /**
     * Gets weather this role is hoisted (if this role is pinned in the user listing).
     *
     * @return Weather this role is hoisted.
     */
    var isHoisted: Boolean

    /**
     * The icon hash of this role.
     *
     * @return The icon hash of this role.
     */
    var icon: String?

    /**
     * The unicode emoji of this role.
     *
     * @return The unicode emoji of this role.
     */
    var unicodeEmoji: String?

    /**
     * The position of this role.
     *
     * @return The position of this role.
     */
    var position: Int

    /**
     * The managed status of this role.
     *
     * @return The managed status of this role.
     */
    var isManaged: Boolean

    /**
     * Gets whether this role is mentionable.
     *
     * @return Whether this role is mentionable.
     */
    var isMentionable: Boolean

    /**
     * The tags of this role.
     *
     * @return The tags of this role.
     */
    var tags: RoleTag?

    /**
     * The raw permissions of this role.
     *
     * @return The raw permissions of this role.
     */
    var rawPermissions: Long
}
