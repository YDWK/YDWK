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

import io.github.realyusufismail.ydwk.cache.exception.CacheException
import io.github.realyusufismail.ydwk.entities.Emoji
import io.github.realyusufismail.ydwk.entities.Guild
import io.github.realyusufismail.ydwk.entities.Sticker
import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.entities.guild.Role

/**
 * This is the implementation of the [Cache] interface that uses a [Map] to store and retrieve data.
 */
// Issues : Different unities with same id will be stored in the same cache
// Solution : Use a different cache for each unity
open class PerpetualCache : Cache {
    private val cache = HashMap<String, Any>()

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Any, cacheType: CacheType) {
        when (cacheType) {
            CacheType.GUILD -> {
                if (value is Guild) {
                    cache[key + "guild"] = value
                } else {
                    throw CacheException("Cache type is Guild but value is not a Guild")
                }
            }
            CacheType.USER -> {
                if (value is User) {
                    cache[key + "user"] = value
                } else {
                    throw CacheException("Cache type is User but value is not a User")
                }
            }
            CacheType.ROLE -> {
                if (value is Role) {
                    cache[key + "role"] = value
                } else {
                    throw CacheException("Cache type is Role but value is not a Role")
                }
            }
            CacheType.MEMBER -> {
                if (value is Member) {
                    cache[key] = value
                } else {
                    throw CacheException("Cache type is Member but value is not a Member")
                }
            }
            CacheType.EMOJI -> {
                if (value is Emoji) {
                    cache[key + "emoji"] = value
                } else {
                    throw CacheException("Cache type is Emoji but value is not a Emoji")
                }
            }
            CacheType.CHANNEL -> {
                TODO()
            }
            CacheType.MESSAGE -> {
                TODO()
            }
            CacheType.VOICE_STATE -> {
                TODO()
            }
            CacheType.PRESENCE -> {
                TODO()
            }
            CacheType.STICKER -> {
                if (value is Sticker) {
                    cache[key + "sticker"] = value
                } else {
                    throw CacheException("Cache type is Sticker but value is not a Sticker")
                }
            }
        }
    }

    override fun get(key: String, cacheType: CacheType): Any? {
        when (cacheType) {
            CacheType.GUILD -> {
                return cache[key + "guild"]
            }
            CacheType.USER -> {
                return cache[key + "user"]
            }
            CacheType.ROLE -> {
                return cache[key + "role"]
            }
            CacheType.MEMBER -> {
                return cache[key]
            }
            CacheType.EMOJI -> {
                return cache[key + "emoji"]
            }
            CacheType.CHANNEL -> {
                return cache[key + "channel"]
            }
            CacheType.MESSAGE -> {
                return cache[key + "message"]
            }
            CacheType.VOICE_STATE -> {
                return cache[key + "voiceState"]
            }
            CacheType.PRESENCE -> {
                return cache[key + "presence"]
            }
            CacheType.STICKER -> {
                return cache[key + "sticker"]
            }
        }
    }

    override fun remove(key: String, cacheType: CacheType): Any? {
        if (cache.containsKey(key)) {
            when (cacheType) {
                CacheType.GUILD -> {
                    return cache.remove(key + "guild")
                }
                CacheType.USER -> {
                    return cache.remove(key + "user")
                }
                CacheType.ROLE -> {
                    return cache.remove(key + "role")
                }
                CacheType.MEMBER -> {
                    return cache.remove(key)
                }
                CacheType.EMOJI -> {
                    return cache.remove(key + "emoji")
                }
                CacheType.CHANNEL -> {
                    return cache.remove(key + "channel")
                }
                CacheType.MESSAGE -> {
                    return cache.remove(key + "message")
                }
                CacheType.VOICE_STATE -> {
                    return cache.remove(key + "voiceState")
                }
                CacheType.PRESENCE -> {
                    return cache.remove(key + "presence")
                }
                CacheType.STICKER -> {
                    return cache.remove(key + "sticker")
                }
            }
        } else {
            throw CacheException("Cache does not contain value for key $key")
        }
    }

    override fun contains(key: String): Boolean {
        return cache.containsKey(key)
    }

    override fun contains(key: String, cacheType: CacheType): Boolean {
        when (cacheType) {
            CacheType.GUILD -> {
                return cache.containsKey(key + "guild")
            }
            CacheType.USER -> {
                return cache.containsKey(key + "user")
            }
            CacheType.ROLE -> {
                return cache.containsKey(key + "role")
            }
            CacheType.MEMBER -> {
                return cache.containsKey(key)
            }
            CacheType.EMOJI -> {
                return cache.containsKey(key + "emoji")
            }
            CacheType.CHANNEL -> {
                return cache.containsKey(key + "channel")
            }
            CacheType.MESSAGE -> {
                return cache.containsKey(key + "message")
            }
            CacheType.VOICE_STATE -> {
                return cache.containsKey(key + "voiceState")
            }
            CacheType.PRESENCE -> {
                return cache.containsKey(key + "presence")
            }
            CacheType.STICKER -> {
                return cache.containsKey(key + "sticker")
            }
        }
    }

    override fun clear() {
        cache.clear()
    }

    override fun values(): MutableCollection<Any> {
        return cache.values
    }
}
