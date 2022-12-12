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
import org.slf4j.LoggerFactory

/**
 * This is the implementation of the [Cache] interface that uses a [Map] to store and retrieve data.
 */
open class PerpetualCache(
    private val allowedCache: Set<CacheIds>,
) : Cache {
    private val cache = HashMap<String, Any>()
    private val logger = LoggerFactory.getLogger(PerpetualCache::class.java)

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Any, cacheType: CacheIds) {
        if (cacheType in allowedCache) {
            // logger.debug("Adding to cache: $key")

            if (cache.containsKey(key) && (value !is Guild || value !is UnavailableGuild)) {
                logger.debug("Cache already contains key: $key")
            } else if (value !is Guild || value !is UnavailableGuild) {
                cache[key + cacheType.getValue()] = value
            } else {
                cache.remove(key + cacheType.getValue())
                cache[key + cacheType.getValue()] = value
            }
        }
    }

    override fun get(key: String, cacheType: CacheIds): Any? {
        return if (cacheType !in allowedCache) {
            null
        } else {
            cache[key + cacheType.getValue()]
        }
    }

    override fun getOrPut(key: String, value: Any, cacheType: CacheIds): Any {
        return if (cacheType !in allowedCache) {
            value
        } else {
            cache.getOrPut(key + cacheType.getValue()) { value }
        }
    }

    override fun remove(key: String, cacheType: CacheIds): Any? {
        if (cacheType !in allowedCache) {
            throw CacheException("The caching of type $cacheType has been disabled")
        } else {
            logger.debug("Removing from cache: $key")
            return cache.remove(key + cacheType.getValue())
        }
    }

    override fun contains(key: String): Boolean {
        return cache.containsKey(key)
    }

    override fun contains(key: String, cacheType: CacheIds): Boolean {
        return cache.containsKey(key + cacheType.getValue())
    }

    override fun clear() {
        cache.clear()
    }

    override fun values(cacheType: CacheIds): List<Any> {
        return cache.filter { it.key.endsWith(cacheType.getValue()) }.values.toList()
    }
}
