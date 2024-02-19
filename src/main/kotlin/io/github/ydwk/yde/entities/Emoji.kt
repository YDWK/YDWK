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

import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.NameAbleEntity

interface Emoji : NameAbleEntity, GenericEntity {
    /**
     * The id of the emoji.
     *
     * @return The id of the emoji.
     */
    val idLong: Long?

    /**
     * The id of the emoji as String.
     *
     * @return The id of the emoji as String.
     */
    val id: String?
        get() = if (idLong == 0L) null else idLong.toString()

    /**
     * The roles that are allowed to use this emoji.
     *
     * @return The roles that are allowed to use this emoji.
     */
    var roles: List<Role>

    /**
     * The user that created this emoji.
     *
     * @return The user that created this emoji.
     */
    var user: User?

    /**
     * Gets whether this emoji must be wrapped in colons.
     *
     * @return Whether this emoji must be wrapped in colons
     */
    var requireColons: Boolean

    /**
     * Gets whether this emoji is managed by an external service.
     *
     * @return Whether this emoji is managed by an external service.
     */
    var isManaged: Boolean

    /**
     * Gets whether this emoji is animated.
     *
     * @return Whether this emoji is animated.
     */
    var isAnimated: Boolean

    /**
     * Gets whether this emoji is available.
     *
     * @return Whether this emoji is available.
     */
    var isAvailable: Boolean
}
