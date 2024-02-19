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
package io.github.ydwk.yde.entities.builder.guild

import io.github.ydwk.yde.entities.builder.GenericEntityBuilder
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import java.awt.Color
import java.util.*

interface RoleBuilder : GenericEntityBuilder<Role> {

    /**
     * Sets the color of the role.
     *
     * @param color the color of the role.
     * @return this builder.
     */
    fun setColor(color: Color): RoleBuilder

    /**
     * Weather if this role is pinned in the user listing.
     *
     * @param pinned if this role is pinned in the user listing.
     * @return this builder
     */
    fun setPinned(pinned: Boolean): RoleBuilder

    /**
     * Sets the icon hash of the role.
     *
     * @param iconHash the icon hash of the role.
     * @return this builder.
     */
    fun setIconHash(iconHash: String): RoleBuilder

    /**
     * Sets the position of the role.
     *
     * @param position the position of the role.
     * @return this builder.
     */
    fun setPosition(position: Int): RoleBuilder

    /**
     * Sets the permissions of the role.
     *
     * @param permissions the permissions of the role.
     * @return this builder.
     */
    fun setPermissions(permissions: EnumSet<GuildPermission>): RoleBuilder

    /**
     * Whether this role is managed by an integration.
     *
     * @param managed whether this role is managed by an integration.
     * @return this builder.
     */
    fun setManaged(managed: Boolean): RoleBuilder

    /**
     * Whether this role is mentionable.
     *
     * @param mentionable whether this role is mentionable.
     * @return this builder.
     */
    fun setMentionable(mentionable: Boolean): RoleBuilder
}
