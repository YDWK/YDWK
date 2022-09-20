/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.cache

interface MemberCache : Cache {
    /**
     * Used to add a new item to the cache
     *
     * @param userId The user id of the member
     * @param guildId The guild id of the member
     * @param value The value of the item
     */
    operator fun set(userId: Long, guildId: Long, value: Any)

    /**
     * Used to get an item from the cache
     *
     * @param userId The user id of the member
     * @param guildId The guild id of the member
     * @return The value of the item
     */
    operator fun get(userId: Long, guildId: Long): Any?

    /**
     * Used to remove an item from the cache
     *
     * @param userId The user id of the member
     * @param guildId The guild id of the member
     */
    fun remove(userId: Long, guildId: Long)
}
