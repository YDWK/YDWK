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
package io.github.ydwk.ydwk.entities

import io.github.ydwk.ydwk.entities.sticker.StickerFormatType
import io.github.ydwk.ydwk.entities.sticker.StickerType
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.NameAbleEntity
import io.github.ydwk.ydwk.util.SnowFlake

/** This class is used to represent a discord sticker object. */
interface Sticker : GenericEntity, SnowFlake, NameAbleEntity {
    /**
     * Gets the ID of the pack this sticker is from.
     *
     * @return The ID of the pack this sticker is from.
     */
    val packId: GetterSnowFlake?

    /**
     * Gets the description of this sticker.
     *
     * @return The description of this sticker.
     */
    var description: String?

    /**
     * Gets the autocomplete/suggestion tags for the sticker (max 200 characters)
     *
     * @return The autocomplete/suggestion tags for the sticker (max 200 characters)
     */
    var tags: List<String>

    /**
     * Gets the type of sticker.
     *
     * @return The type of sticker.
     */
    var type: StickerType

    /**
     * Gets the format type of sticker.
     *
     * @return The format type of sticker.
     */
    var formatType: StickerFormatType

    /**
     * Gets weather this sticker can be used, may be false due to loss of Server Boosts.
     *
     * @return weather this sticker can be used, may be false due to loss of Server Boosts.
     */
    var available: Boolean

    /**
     * Gets the ID of the guild that owns this sticker.
     *
     * @return The ID of the guild that owns this sticker.
     */
    val guildId: GetterSnowFlake?

    /**
     * Gets the user that uploaded the sticker.
     *
     * @return The user that uploaded the sticker.
     */
    var user: User?

    /**
     * Gets the standard sticker's sort order within its pack
     *
     * @return The standard sticker's sort order within its pack
     */
    var sortvarue: Int?
}
