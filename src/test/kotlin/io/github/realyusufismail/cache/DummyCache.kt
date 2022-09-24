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

import io.github.realyusufismail.ws.io.github.realyusufismail.cache.user.DummyUser
import io.github.realyusufismail.ydwk.cache.Cache
import io.github.realyusufismail.ydwk.cache.CacheType
import io.github.realyusufismail.ydwk.cache.exception.CacheException
import io.github.realyusufismail.ydwk.entities.Emoji
import io.github.realyusufismail.ydwk.entities.Guild
import io.github.realyusufismail.ydwk.entities.Sticker
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.entities.guild.Role

class DummyCache : Cache {
    private val dummyMap = HashMap<String, Any>()

    override val size: Int
        get() = dummyMap.size

    override fun set(key: String, value: Any, cacheType: CacheType) {
        when (cacheType) {
            CacheType.GUILD -> {
                if (value is Guild) {
                    dummyMap[key + "guild"] = value
                } else {
                    throw CacheException("Cache type is Guild but value is not a Guild")
                }
            }
            CacheType.USER -> {
                if (value is DummyUser) {
                    dummyMap[key + "user"] = value
                } else {
                    throw CacheException("Cache type is User but value is not a User")
                }
            }
            CacheType.ROLE -> {
                if (value is Role) {
                    dummyMap[key + "role"] = value
                } else {
                    throw CacheException("Cache type is Role but value is not a Role")
                }
            }
            CacheType.MEMBER -> {
                if (value is Member) {
                    dummyMap[key] = value
                } else {
                    throw CacheException("Cache type is Member but value is not a Member")
                }
            }
            CacheType.EMOJI -> {
                if (value is Emoji) {
                    dummyMap[key + "emoji"] = value
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
                    dummyMap[key + "sticker"] = value
                } else {
                    throw CacheException("Cache type is Sticker but value is not a Sticker")
                }
            }
        }
    }

    override fun get(key: String, cacheType: CacheType): Any? {
        if (dummyMap.containsKey(key)) {
            when (cacheType) {
                CacheType.GUILD -> {
                    return dummyMap[key + "guild"]
                }
                CacheType.USER -> {
                    return dummyMap[key + "user"]
                }
                CacheType.ROLE -> {
                    return dummyMap[key + "role"]
                }
                CacheType.MEMBER -> {
                    return dummyMap[key]
                }
                CacheType.EMOJI -> {
                    return dummyMap[key + "emoji"]
                }
                CacheType.CHANNEL -> {
                    return dummyMap[key + "channel"]
                }
                CacheType.MESSAGE -> {
                    return dummyMap[key + "message"]
                }
                CacheType.VOICE_STATE -> {
                    return dummyMap[key + "voiceState"]
                }
                CacheType.PRESENCE -> {
                    return dummyMap[key + "presence"]
                }
                CacheType.STICKER -> {
                    return dummyMap[key + "sticker"]
                }
            }
        } else {
            throw CacheException("Cache does not contain key: $key")
        }
    }

    override fun remove(key: String, cacheType: CacheType): Any? {
        if (dummyMap.containsKey(key)) {
            when (cacheType) {
                CacheType.GUILD -> {
                    return dummyMap.remove(key + "guild")
                }
                CacheType.USER -> {
                    return dummyMap.remove(key + "user")
                }
                CacheType.ROLE -> {
                    return dummyMap.remove(key + "role")
                }
                CacheType.MEMBER -> {
                    return dummyMap.remove(key)
                }
                CacheType.EMOJI -> {
                    return dummyMap.remove(key + "emoji")
                }
                CacheType.CHANNEL -> {
                    return dummyMap.remove(key + "channel")
                }
                CacheType.MESSAGE -> {
                    return dummyMap.remove(key + "message")
                }
                CacheType.VOICE_STATE -> {
                    return dummyMap.remove(key + "voiceState")
                }
                CacheType.PRESENCE -> {
                    return dummyMap.remove(key + "presence")
                }
                CacheType.STICKER -> {
                    return dummyMap.remove(key + "sticker")
                }
            }
        } else {
            throw CacheException("Cache does not contain value for key $key")
        }
    }

    override fun contains(key: String): Boolean {
        return dummyMap.containsKey(key)
    }

    override fun contains(key: String, cacheType: CacheType): Boolean {
        when (cacheType) {
            CacheType.GUILD -> {
                return dummyMap.containsKey(key + "guild")
            }
            CacheType.USER -> {
                return dummyMap.containsKey(key + "user")
            }
            CacheType.ROLE -> {
                return dummyMap.containsKey(key + "role")
            }
            CacheType.MEMBER -> {
                return dummyMap.containsKey(key)
            }
            CacheType.EMOJI -> {
                return dummyMap.containsKey(key + "emoji")
            }
            CacheType.CHANNEL -> {
                return dummyMap.containsKey(key + "channel")
            }
            CacheType.MESSAGE -> {
                return dummyMap.containsKey(key + "message")
            }
            CacheType.VOICE_STATE -> {
                return dummyMap.containsKey(key + "voiceState")
            }
            CacheType.PRESENCE -> {
                return dummyMap.containsKey(key + "presence")
            }
            CacheType.STICKER -> {
                return dummyMap.containsKey(key + "sticker")
            }
        }
    }

    override fun clear() {
        dummyMap.clear()
    }

    override fun values(): MutableCollection<Any> {
        return dummyMap.values
    }
}
