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
import io.github.ydwk.ydwk.entities.application.PartialApplication
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role

/**
 * This is the implementation of the [Cache] interface that uses a [Map] to store and retrieve data.
 */
open class PerpetualCache(
    private val allowedCache: Set<CacheType>,
    private val disallowedCache: Set<CacheType>
) : Cache {
    private val cache = HashMap<String, Any>()

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Any, cacheType: CacheIds) {
        if (allowedCache.contains(cacheType.getCacheType()) &&
            !disallowedCache.contains(cacheType.getCacheType())) {
            cache[key + cacheType.toString()] = value
        }
    }

    override fun get(key: String, cacheType: CacheIds): Any? {
        if (allowedCache.contains(cacheType.getCacheType()) &&
            !disallowedCache.contains(cacheType.getCacheType())) {
            return if (cache.containsKey(key + cacheType.toString())) {
                cache[key + cacheType.toString()]
            } else {
                throw CacheException("No value found for key $key and type $cacheType")
            }
        }
        return null
    }

    override fun remove(key: String, cacheType: CacheIds): Any? {
        if (allowedCache.contains(cacheType.getCacheType()) &&
            !disallowedCache.contains(cacheType.getCacheType())) {
            return if (cache.containsKey(key + cacheType.toString())) {
                cache.remove(key + cacheType.toString())
            } else {
                throw CacheException("Cache does not contain value for key $key")
            }
        }
        return null
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
        return cache.values.filter {
            (it is Guild) ||
                (it is Channel) ||
                (it is User) ||
                (it is Role) ||
                (it is Emoji) ||
                (it is Message) ||
                (it is Sticker) ||
                (it is Member) ||
                (it is PartialApplication)
        }
    }
}
