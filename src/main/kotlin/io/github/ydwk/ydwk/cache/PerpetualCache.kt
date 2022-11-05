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

import io.github.ydwk.ydwk.cache.exception.CacheException
import io.github.ydwk.ydwk.entities.*

/**
 * This is the implementation of the [Cache] interface that uses a [Map] to store and retrieve data.
 */
open class PerpetualCache(
    private val allowedCache: Set<CacheIds>,
) : Cache {
    private val cache = HashMap<String, Any>()

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Any, cacheType: CacheIds) {
        if (cacheType in allowedCache) {
            cache[key + cacheType.toString()] = value
        }
    }

    override fun get(key: String, cacheType: CacheIds): Any? {
        return if (cacheType !in allowedCache) {
            null
        } else {
            cache[key + cacheType.toString()]
        }
    }

    override fun remove(key: String, cacheType: CacheIds): Any? {
        if (cacheType !in allowedCache) {
            throw CacheException("The caching of type $cacheType has been disabled")
        } else {
            return cache.remove(key + cacheType.toString())
        }
    }

    override fun contains(key: String): Boolean {
        return cache.containsKey(key)
    }

    override fun contains(key: String, cacheType: CacheIds): Boolean {
        return cache.containsKey(key + cacheType.toString())
    }

    override fun clear() {
        cache.clear()
    }

    override fun values(cacheType: CacheIds): List<Any> {
        return cache.filter { it.key.endsWith(cacheType.toString()) }.values.toList()
    }

    override fun update(key: String, cacheType: CacheIds, value: Any) {
        if (cacheType !in allowedCache) {
            // do nothing
        } else {
            // check if the cache exists
            if (contains(key, cacheType)) {
                // delete the old cache
                remove(key, cacheType)
                // add the new cache
                set(key, value, cacheType)
            } else {
                // add the cache
                set(key, value, cacheType)
            }
        }
    }
}
