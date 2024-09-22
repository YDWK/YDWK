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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.guild.enums.GuildFeature
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.SnowFlake

interface PartialGuild : GenericEntity, SnowFlake {

    /**
     * The guild's icon hash.
     *
     * @return the guild's icon hash.
     */
    var icon: String

    /**
     * The banner hash for the guild.
     *
     * @return the banner hash for the guild.
     */
    var banner: String

    /**
     * Checks if this is user is owner of the guild.
     *
     * @return true if the user is owner of the guild.
     */
    var isOwner: Boolean

    /**
     * The guild's permissions.
     *
     * @return the guild's permissions.
     */
    var permissions: String

    /**
     * The guild's features.
     *
     * @return the guild's features.
     */
    var features: Set<GuildFeature>

    /**
     * The approximate member count for the guild.
     *
     * @return the approximate member count for the guild.
     */
    var approximateMemberCount: Int

    /**
     * The approximate presence count for the guild.
     *
     * @return the approximate presence count for the guild.
     */
    var approximatePresenceCount: Int
}
