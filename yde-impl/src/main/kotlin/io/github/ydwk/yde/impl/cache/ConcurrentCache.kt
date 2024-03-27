/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.impl.cache

import io.github.ydwk.yde.cache.Cache
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.cache.exception.CacheException
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.*

open class ConcurrentCache(private val allowedCache: Set<CacheIds>, private val yde: YDEImpl) :
    Cache, CoroutineScope {
    private val cache = ConcurrentHashMap<String, Any>()
    private val lock = ReentrantLock()
    private val job = Job()
    override val coroutineContext: CoroutineContext =
        job + Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Any, cacheId: CacheIds) {
        if (cacheId !in allowedCache) return
        lock.withLock { cache[key + cacheId.getValue()] = value }
    }

    override fun get(key: String, cacheId: CacheIds): Any? {
        if (cacheId !in allowedCache) return null
        return lock.withLock { cache[key + cacheId.getValue()] }
    }

    override fun getOrPut(key: String, value: Any, cacheId: CacheIds): Any {
        if (cacheId !in allowedCache) return value
        return lock.withLock { cache.getOrPut(key + cacheId.getValue()) { value } }
    }

    override fun remove(key: String, cacheId: CacheIds): Any? {
        if (cacheId !in allowedCache)
            throw CacheException("The caching of type $cacheId has been disabled")
        return lock.withLock { cache.remove(key + cacheId.getValue()) }
    }

    override fun contains(key: String): Boolean {
        return cache.containsKey(key)
    }

    override fun contains(key: String, cacheId: CacheIds): Boolean {
        return cache.containsKey(key + cacheId.getValue())
    }

    override fun clear() {
        cache.clear()
    }

    override fun clear(cacheId: CacheIds) {
        if (cacheId !in allowedCache)
            throw CacheException("The caching of type $cacheId has been disabled")
        cache.entries.removeIf { it.key.endsWith(cacheId.getValue()) }
    }

    override fun values(cacheId: CacheIds): List<Any> {
        return cache.filterKeys { it.endsWith(cacheId.getValue()) }.values.toList()
    }

    override fun triggerCacheTypeClear(cacheId: CacheIds, duration: Duration, repeat: Boolean) {
        if (cacheId !in allowedCache)
            throw CacheException("The caching of type $cacheId has been disabled")
        launch {
            while (isActive) {
                lock.withLock { clear(cacheId) }
                delay(duration.toMillis())
            }
        }
    }

    override fun triggerCacheTypesClear(
        cacheIds: Set<CacheIds>,
        duration: Duration,
        repeat: Boolean
    ) {
        cacheIds.forEach { triggerCacheTypeClear(it, duration, repeat) }
    }

    override fun triggerCacheClear(duration: Duration, repeat: Boolean) {
        launch {
            while (isActive) {
                lock.withLock { clear() }
                delay(duration.toMillis())
            }
        }
    }
}
