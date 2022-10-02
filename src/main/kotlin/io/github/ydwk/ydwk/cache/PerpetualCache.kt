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

import io.github.realyusufismail.ydwk.cache.exception.CacheException
import io.github.realyusufismail.ydwk.entities.*
import io.github.realyusufismail.ydwk.entities.application.PartialApplication
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.entities.guild.Role

/**
 * This is the implementation of the [Cache] interface that uses a [Map] to store and retrieve data.
 */
open class PerpetualCache : Cache {
    private val cache = HashMap<String, Any>()

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Any, cacheType: CacheType) {
        cache[key + cacheType.toString()] = value
    }

    override fun get(key: String, cacheType: CacheType): Any? {
        return if (cache.containsKey(key + cacheType.toString())) {
            cache[key + cacheType.toString()]
        } else {
            throw CacheException("No value found for key $key and type $cacheType")
        }
    }

    override fun remove(key: String, cacheType: CacheType): Any? {
        return if (cache.containsKey(key + cacheType.toString())) {
            cache.remove(key + cacheType.toString())
        } else {
            throw CacheException("Cache does not contain value for key $key")
        }
    }

    override fun contains(key: String): Boolean {
        return cache.containsKey(key)
    }

    override fun contains(key: String, cacheType: CacheType): Boolean {
        return cache.containsKey(key + cacheType.toString())
    }

    override fun clear() {
        cache.clear()
    }

    override fun values(cacheType: CacheType): List<Any> {
        return cache.values.filter {
            (it is Guild) ||
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
