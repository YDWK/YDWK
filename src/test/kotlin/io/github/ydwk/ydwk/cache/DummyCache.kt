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

class DummyCache : Cache {
    private val dummyMap = HashMap<String, Any>()

    override val size: Int
        get() = dummyMap.size

    override fun set(key: String, value: Any, cacheType: CacheIds) {
        dummyMap[key + cacheType.toString()] = value
    }

    override fun get(key: String, cacheType: CacheIds): Any? {
        return dummyMap[key + cacheType.toString()]
            ?: throw CacheException("The key $key does not exist")
    }

    override fun remove(key: String, cacheType: CacheIds): Any? {
        return dummyMap.remove(key + cacheType.toString())
    }

    override fun contains(key: String): Boolean {
        return dummyMap.containsKey(key)
    }

    override fun contains(key: String, cacheType: CacheIds): Boolean {
        return dummyMap.containsKey(key + cacheType.toString())
    }

    override fun clear() {
        dummyMap.clear()
    }

    override fun values(cacheType: CacheIds): List<Any> {
        return dummyMap.filter { it.key.endsWith(cacheType.toString()) }.values.toList()
    }
}
