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

    override fun clear() {
        dummyMap.clear()
    }
}