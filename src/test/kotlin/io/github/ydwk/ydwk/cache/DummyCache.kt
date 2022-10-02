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
import io.github.ydwk.ydwk.entities.Emoji
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Sticker
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role

class DummyCache : Cache {
    private val dummyMap = HashMap<String, Any>()

    override val size: Int
        get() = dummyMap.size

    override fun set(key: String, value: Any, cacheType: CacheIds) {
        when (cacheType) {
            CacheIds.GUILD -> {
                if (value is Guild) {
                    dummyMap[key + "guild"] = value
                } else {
                    throw CacheException("Cache type is Guild but value is not a Guild")
                }
            }
            CacheIds.USER -> {
                if (value is DummyUser) {
                    dummyMap[key + "user"] = value
                } else {
                    throw CacheException("Cache type is User but value is not a User")
                }
            }
            CacheIds.ROLE -> {
                if (value is Role) {
                    dummyMap[key + "role"] = value
                } else {
                    throw CacheException("Cache type is Role but value is not a Role")
                }
            }
            CacheIds.MEMBER -> {
                if (value is Member) {
                    dummyMap[key] = value
                } else {
                    throw CacheException("Cache type is Member but value is not a Member")
                }
            }
            CacheIds.EMOJI -> {
                if (value is Emoji) {
                    dummyMap[key + "emoji"] = value
                } else {
                    throw CacheException("Cache type is Emoji but value is not a Emoji")
                }
            }
            CacheIds.CHANNEL -> {
                TODO()
            }
            CacheIds.MESSAGE -> {
                TODO()
            }
            CacheIds.VOICE_STATE -> {
                TODO()
            }
            CacheIds.STICKER -> {
                if (value is Sticker) {
                    dummyMap[key + "sticker"] = value
                } else {
                    throw CacheException("Cache type is Sticker but value is not a Sticker")
                }
            }
            CacheIds.APPLICATION -> {
                TODO()
            }
            else -> {
                throw CacheException("Cache type is not supported")
            }
        }
    }

    override fun get(key: String, cacheType: CacheIds): Any? {
        when (cacheType) {
            CacheIds.GUILD -> {
                return dummyMap[key + "guild"]
            }
            CacheIds.USER -> {
                return dummyMap[key + "user"]
            }
            CacheIds.ROLE -> {
                return dummyMap[key + "role"]
            }
            CacheIds.MEMBER -> {
                return dummyMap[key]
            }
            CacheIds.EMOJI -> {
                return dummyMap[key + "emoji"]
            }
            CacheIds.CHANNEL -> {
                return dummyMap[key + "channel"]
            }
            CacheIds.MESSAGE -> {
                return dummyMap[key + "message"]
            }
            CacheIds.VOICE_STATE -> {
                return dummyMap[key + "voiceState"]
            }
            CacheIds.STICKER -> {
                return dummyMap[key + "sticker"]
            }
            CacheIds.APPLICATION -> {
                return dummyMap[key + "application"]
            }
            else -> {
                throw CacheException("Cache type is not supported")
            }
        }
    }

    override fun remove(key: String, cacheType: CacheIds): Any? {
        if (dummyMap.containsKey(key)) {
            when (cacheType) {
                CacheIds.GUILD -> {
                    return dummyMap.remove(key + "guild")
                }
                CacheIds.USER -> {
                    return dummyMap.remove(key + "user")
                }
                CacheIds.ROLE -> {
                    return dummyMap.remove(key + "role")
                }
                CacheIds.MEMBER -> {
                    return dummyMap.remove(key)
                }
                CacheIds.EMOJI -> {
                    return dummyMap.remove(key + "emoji")
                }
                CacheIds.CHANNEL -> {
                    return dummyMap.remove(key + "channel")
                }
                CacheIds.MESSAGE -> {
                    return dummyMap.remove(key + "message")
                }
                CacheIds.VOICE_STATE -> {
                    return dummyMap.remove(key + "voiceState")
                }
                CacheIds.STICKER -> {
                    return dummyMap.remove(key + "sticker")
                }
                CacheIds.APPLICATION -> {
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

    override fun contains(key: String, cacheType: CacheIds): Boolean {
        when (cacheType) {
            CacheIds.GUILD -> {
                return dummyMap.containsKey(key + "guild")
            }
            CacheIds.USER -> {
                return dummyMap.containsKey(key + "user")
            }
            CacheIds.ROLE -> {
                return dummyMap.containsKey(key + "role")
            }
            CacheIds.MEMBER -> {
                return dummyMap.containsKey(key)
            }
            CacheIds.EMOJI -> {
                return dummyMap.containsKey(key + "emoji")
            }
            CacheIds.CHANNEL -> {
                return dummyMap.containsKey(key + "channel")
            }
            CacheIds.MESSAGE -> {
                return dummyMap.containsKey(key + "message")
            }
            CacheIds.VOICE_STATE -> {
                return dummyMap.containsKey(key + "voiceState")
            }
            CacheIds.STICKER -> {
                return dummyMap.containsKey(key + "sticker")
            }
            CacheIds.APPLICATION -> {
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

    override fun values(cacheType: CacheIds): List<Any> {
        val list = ArrayList<Any>()
        when (cacheType) {
            CacheIds.GUILD -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("guild")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.USER -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("user")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.ROLE -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("role")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.MEMBER -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("member")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.EMOJI -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("emoji")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.CHANNEL -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("channel")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.MESSAGE -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("message")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.VOICE_STATE -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("voiceState")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.STICKER -> {
                dummyMap.forEach { (key, value) ->
                    if (key.endsWith("sticker")) {
                        list.add(value)
                    }
                }
            }
            CacheIds.APPLICATION -> {
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
