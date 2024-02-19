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

import java.time.Duration

/** This is used to store and retrieve data from cache */
interface Cache {
    /** The size of the cache */
    val size: Int

    /**
     * Adds a new item to the cache
     *
     * @param key The key of the item
     * @param value The value of the item
     * @param cacheId The id of the cache
     */
    operator fun set(key: String, value: Any, cacheId: CacheIds)

    /**
     * Gets an item from the cache
     *
     * @param key The key of the item
     * @param cacheId The id of the cache
     * @return The value of the item
     */
    operator fun get(key: String, cacheId: CacheIds): Any?

    /**
     * Gets an item from the cache but adds it if it doesn't exist
     *
     * @param key The key of the item
     * @param value The value of the item
     * @param cacheId The id of the cache
     * @return The value of the item
     */
    fun getOrPut(key: String, value: Any, cacheId: CacheIds): Any

    /**
     * Removes an item from the cache
     *
     * @param key The key of the item
     * @param cacheId The id of the cache
     * @return The value of the item
     */
    fun remove(key: String, cacheId: CacheIds): Any?

    /**
     * Check's if this properties exists in the cache
     *
     * @param key The key of the item
     * @return True if the item exists, false otherwise
     */
    fun contains(key: String): Boolean

    /**
     * Check's if this properties exists in the cache and value exists
     *
     * @param key The key of the item
     * @param cacheId The id of the cache
     * @return True if the item exists, false otherwise
     */
    fun contains(key: String, cacheId: CacheIds): Boolean

    /** Clears the cache */
    fun clear()

    /**
     * Clears the cache of a certain cache Id.
     *
     * @param cacheId The id of the cache
     */
    fun clear(cacheId: CacheIds)

    /**
     * Gets a list of objects in the cache
     *
     * @param cacheId The id of the cache
     * @return A list of objects in the cache
     */
    fun values(cacheId: CacheIds): List<Any>

    /**
     * Triggers a thread to clear a certain cache type after a certain amount of time
     *
     * @param cacheId The id of the cache
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    fun triggerCacheTypeClear(cacheId: CacheIds, duration: Duration, repeat: Boolean = true)

    /**
     * Triggers a thread to clear a list of cache types after a certain amount of time
     *
     * @param cacheIds The ids of the cache
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    fun triggerCacheTypesClear(cacheIds: List<CacheIds>, duration: Duration, repeat: Boolean = true)

    /**
     * Triggers a thread to clear the entire cache after a certain amount of time
     *
     * @param duration The duration to wait before clearing the cache
     * @param repeat whether to repeat the clearing of the cache
     */
    fun triggerCacheClear(duration: Duration, repeat: Boolean = true)
}
