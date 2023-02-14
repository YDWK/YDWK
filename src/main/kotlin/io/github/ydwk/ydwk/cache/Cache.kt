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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.cache

/** This is used to store and retrieve data from cache */
interface Cache {
    /** The size of the cache */
    val size: Int

    /**
     * Adds a new item to the cache
     *
     * @param key The key of the item
     * @param value The value of the item
     */
    operator fun set(key: String, value: Any, cacheType: CacheIds)

    /**
     * Gets an item from the cache
     *
     * @param key The key of the item
     * @return The value of the item
     */
    operator fun get(key: String, cacheType: CacheIds): Any?

    /**
     * Gets an item from the cache but adds it if it doesn't exist
     *
     * @param key The key of the item
     * @param value The value of the item
     * @return The value of the item
     */
    fun getOrPut(key: String, value: Any, cacheType: CacheIds): Any

    /**
     * Removes an item from the cache
     *
     * @param key The key of the item
     * @return The value of the item
     */
    fun remove(key: String, cacheType: CacheIds): Any?

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
     * @param cacheType The type of the item
     * @return True if the item exists, false otherwise
     */
    fun contains(key: String, cacheType: CacheIds): Boolean

    /** Clears the cache */
    fun clear()

    /**
     * Gets a list of objects in the cache
     *
     * @param cacheType The type of the item
     * @return A list of objects in the cache
     */
    fun values(cacheType: CacheIds): List<Any>
}
