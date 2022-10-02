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

/** This used to store and retrieve data from cache */
interface Cache {
    /** Used to get the size of the cache */
    val size: Int

    /**
     * Used to add a new item to the cache
     *
     * @param key The key of the item
     * @param value The value of the item
     */
    operator fun set(key: String, value: Any, cacheType: CacheType)

    /**
     * Used to get an item from the cache
     *
     * @param key The key of the item
     * @return The value of the item
     */
    operator fun get(key: String, cacheType: CacheType): Any?

    /**
     * Used to remove an item from the cache
     *
     * @param key The key of the item
     * @return The value of the item
     */
    fun remove(key: String, cacheType: CacheType): Any?

    /**
     * Used to check if this properties exists in the cache
     *
     * @param key The key of the item
     * @return True if the item exists, false otherwise
     */
    fun contains(key: String): Boolean

    /**
     * Used to check if this properties exists in the cache and value exists
     *
     * @param key The key of the item
     * @param cacheType The type of the item
     * @return True if the item exists, false otherwise
     */
    fun contains(key: String, cacheType: CacheType): Boolean

    /** Used to clear the cache */
    fun clear()

    /** Used to get a list of objects in the cache */
    fun values(cacheType: CacheType): List<Any>
}
