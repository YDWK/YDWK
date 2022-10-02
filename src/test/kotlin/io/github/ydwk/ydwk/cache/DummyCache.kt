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
            CacheType.STICKER -> {
                if (value is Sticker) {
                    dummyMap[key + "sticker"] = value
                } else {
                    throw CacheException("Cache type is Sticker but value is not a Sticker")
                }
            }
            CacheType.APPLICATION -> {
                TODO()
            }
            else -> {
                throw CacheException("Cache type is not supported")
            }
        }
    }

    override fun get(key: String, cacheType: CacheType): Any? {
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
            CacheType.STICKER -> {
                return dummyMap[key + "sticker"]
            }
            CacheType.APPLICATION -> {
                return dummyMap[key + "application"]
            }
            else -> {
                throw CacheException("Cache type is not supported")
            }
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
                CacheType.STICKER -> {
                    return dummyMap.remove(key + "sticker")
                }
                CacheType.APPLICATION -> {
                    return dummyMap.remove(key + "application")
                }
                else -> {
                    throw CacheException("Cache type is not supported")
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
            CacheType.STICKER -> {
                return dummyMap.containsKey(key + "sticker")
            }
            CacheType.APPLICATION -> {
                return dummyMap.containsKey(key + "application")
            }
            else -> {
                throw CacheException("Cache type is not supported")
            }
        }
    }

    override fun clear() {
        dummyMap.clear()
    }

    override fun values(cacheType: CacheType): List<Any> {
        val list = ArrayList<Any>()
        when (cacheType) {
            CacheType.GUILD -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("guild")) {
                        list.add(value)
                    }
                }
            }
            CacheType.USER -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("user")) {
                        list.add(value)
                    }
                }
            }
            CacheType.ROLE -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("role")) {
                        list.add(value)
                    }
                }
            }
            CacheType.MEMBER -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("member")) {
                        list.add(value)
                    }
                }
            }
            CacheType.EMOJI -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("emoji")) {
                        list.add(value)
                    }
                }
            }
            CacheType.CHANNEL -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("channel")) {
                        list.add(value)
                    }
                }
            }
            CacheType.MESSAGE -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("message")) {
                        list.add(value)
                    }
                }
            }
            CacheType.VOICE_STATE -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("voiceState")) {
                        list.add(value)
                    }
                }
            }
            CacheType.STICKER -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("sticker")) {
                        list.add(value)
                    }
                }
            }
            CacheType.APPLICATION -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("application")) {
                        list.add(value)
                    }
                }
            }
            else -> {
                throw CacheException("Cache type is not supported")
            }
        }
        return list
    }
}
