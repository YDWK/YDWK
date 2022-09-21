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
package io.github.realyusufismail.ws.io.github.realyusufismail.cache

import io.github.realyusufismail.ydwk.cache.Cache

class DummyCache : Cache {
    private val dummyMap = HashMap<Long, Any>()

    override val size: Int
        get() = dummyMap.size

    override fun set(key: Long, value: Any) {
        dummyMap[key] = value
    }

    override fun get(key: Long): Any? {
        return dummyMap[key]
    }

    override fun remove(key: Long): Any? {
        return dummyMap.remove(key)
    }

    override fun contains(key: Long): Boolean {
        return dummyMap.containsKey(key)
    }

    override fun clear() {
        dummyMap.clear()
    }
}
