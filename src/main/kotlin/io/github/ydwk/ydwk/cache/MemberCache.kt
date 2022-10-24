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
package io.github.ydwk.ydwk.cache

import io.github.ydwk.ydwk.entities.guild.Member

interface MemberCache : Cache {
    /**
     * Adds a new item to the cache
     *
     * @param userId The user id of the member = * @param value The value of the item
     */
    operator fun set(userId: String, value: Member)

    /**
     * Gets an item from the cache
     *
     * @param userId The user id of the member
     * @return The value of the item
     */
    operator fun get(userId: String): Any?

    /**
     * Removes an item from the cache
     *
     * @param userId The user id of the member
     */
    fun remove(userId: String)
}
