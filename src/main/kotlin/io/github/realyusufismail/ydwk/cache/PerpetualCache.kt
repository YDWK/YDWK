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

/**
 * This is the implementation of the [Cache] interface that uses a [Map] to store and retrieve data.
 */
class PerpetualCache : Cache {
    private val cache = HashMap<Long, Any>()

    override val size: Int
        get() = cache.size

    override fun set(key: Long, value: Any) {
        cache[key] = value
    }

    override fun get(key: Long): Any? {
        return cache[key]
    }

    override fun remove(key: Long): Any? {
        return cache.remove(key)
    }

    override fun clear() {
        cache.clear()
    }
}
