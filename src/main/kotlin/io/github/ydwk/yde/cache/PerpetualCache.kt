/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.cache

import io.github.ydwk.yde.cache.exception.CacheException
import io.github.ydwk.yde.entities.*
import io.github.ydwk.yde.impl.YDEImpl
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap
import org.slf4j.LoggerFactory

/**
 * This is the implementation of the [Cache] interface that uses a [Map] to store and retrieve data.
 */
open class PerpetualCache(private val allowedCache: Set<CacheIds>, private val yde: YDEImpl) :
    Cache {
    private val cache = HashMap<String, Any>()
    private val logger = LoggerFactory.getLogger(PerpetualCache::class.java)

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Any, cacheId: CacheIds) {
        if (cacheId in allowedCache) {
            // logger.debug("Adding to cache: $key")

            if (cache.containsKey(key) && (value !is Guild || value !is UnavailableGuild)) {
                logger.debug("Cache already contains key: $key")
            } else if (value !is Guild || value !is UnavailableGuild) {
                cache[key + cacheId.getValue()] = value
            } else {
                cache.remove(key + cacheId.getValue())
                cache[key + cacheId.getValue()] = value
            }
        }
    }

    override fun get(key: String, cacheId: CacheIds): Any? {
        return if (cacheId !in allowedCache) {
            null
        } else {
            cache[key + cacheId.getValue()]
        }
    }

    override fun getOrPut(key: String, value: Any, cacheId: CacheIds): Any {
        return if (cacheId !in allowedCache) {
            value
        } else {
            cache.getOrPut(key + cacheId.getValue()) { value }
        }
    }

    override fun remove(key: String, cacheId: CacheIds): Any? {
        if (cacheId !in allowedCache) {
            throw CacheException("The caching of type $cacheId has been disabled")
        } else {
            logger.debug("Removing from cache: $key")
            return cache.remove(key + cacheId.getValue())
        }
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
        if (cacheId !in allowedCache) {
            throw CacheException("The caching of type $cacheId has been disabled")
        } else {
            logger.debug("Clearing cache of type $cacheId")
            cache.filter { it.key.endsWith(cacheId.getValue()) }.keys.forEach { cache.remove(it) }
        }
    }

    override fun values(cacheId: CacheIds): List<Any> {
        return cache.filter { it.key.endsWith(cacheId.getValue()) }.values.toList()
    }

    override fun triggerCacheTypeClear(cacheId: CacheIds, duration: Duration, repeat: Boolean) {
        // if repeat is true, then the cache will be cleared every duration else it will be cleared
        // once for the duration
        if (cacheId !in allowedCache) {
            throw CacheException("The caching of type $cacheId has been disabled")
        } else {
            logger.debug(
                "Triggering cache clear of type $cacheId for duration $duration and the repeat status is $repeat")
            yde.threadFactory
                .createThreadExecutor("CacheClearerFor${cacheId.getValue()}") {
                    if (repeat) {
                        Timer()
                            .schedule(
                                object : TimerTask() {
                                    override fun run() {
                                        clear(cacheId)
                                    }
                                },
                                duration.toMillis(),
                                duration.toMillis())
                    } else {
                        Timer()
                            .schedule(
                                object : TimerTask() {
                                    override fun run() {
                                        clear(cacheId)
                                    }
                                },
                                duration.toMillis())
                    }
                }
                .get()
        }
    }

    override fun triggerCacheTypesClear(
        cacheIds: List<CacheIds>,
        duration: Duration,
        repeat: Boolean
    ) {
        cacheIds.forEach { triggerCacheTypeClear(it, duration, repeat) }
    }

    override fun triggerCacheClear(duration: Duration, repeat: Boolean) {
        yde.threadFactory
            .createThreadExecutor("CacheClearerForAll") {
                if (repeat) {
                    Timer()
                        .schedule(
                            object : TimerTask() {
                                override fun run() {
                                    clear()
                                }
                            },
                            duration.toMillis(),
                            duration.toMillis())
                } else {
                    Timer()
                        .schedule(
                            object : TimerTask() {
                                override fun run() {
                                    clear()
                                }
                            },
                            duration.toMillis())
                }
            }
            .get()
    }
}
