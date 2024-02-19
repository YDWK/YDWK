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
package io.github.ydwk.yde.cache

import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.entities.guild.Member

/** Discord's Member do not have a unique ID, so we need to use a combination of the user id */
interface MemberCache {
    /**
     * Adds a new item to the cache
     *
     * @param guildId The guild id of the member
     * @param userId The user id of the member
     * @param value The value of the item
     */
    operator fun set(guildId: String, userId: String, value: Member)

    /**
     * Gets an item from the cache
     *
     * @param guildId The guild id of the member
     * @param userId The user id of the member
     * @return The value of the item
     */
    operator fun get(guildId: String, userId: String): Member?

    /**
     * Gets an item from the cache but adds it if it doesn't exist
     *
     * @param value The value of the item
     * @return The value of the item
     */
    fun getOrPut(value: Member): Member

    /**
     * updates the member's voice state
     *
     * @param member The member which voice state is to be updated
     * @param voiceState The new voice state
     * @param add If true, the voice state will be added to the member's voice states, otherwise it
     *   will be removed
     */
    fun updateVoiceState(member: Member, voiceState: VoiceState, add: Boolean)

    /**
     * Removes an item from the cache
     *
     * @param guildId The guild id of the member
     * @param userId The user id of the member
     */
    fun remove(guildId: String, userId: String)

    /**
     * Check's if this properties exists in the cache and value exists
     *
     * @param key The key of the item
     * @param cacheType The type of the item
     * @return True if the item exists, false otherwise
     */
    fun contains(guildId: String, userId: String): Boolean

    /** Clears the cache */
    fun clear()

    /**
     * Gets a list of members in the cache
     *
     * @return A list of members in the cache
     */
    fun values(): List<Member>
}
