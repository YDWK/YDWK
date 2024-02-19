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

enum class CacheIds(private val value: String, private val cacheType: CacheType) {
    GUILD("guild", CacheType.GUILD),
    USER("user", CacheType.USER),
    CHANNEL("channel", CacheType.CHANNEL),
    ROLE("role", CacheType.ROLE),
    EMOJI("emoji", CacheType.EMOJI),
    MESSAGE("message", CacheType.MESSAGE),
    STICKER("sticker", CacheType.STICKER),
    MEMBER("member", CacheType.MEMBER),
    ATTACHMENT("attachment", CacheType.ATTACHMENT),
    APPLICATION("application", CacheType.APPLICATION),
    UNKNOWN("unknown", CacheType.UNKNOWN);

    companion object {
        /** Get the [CacheIds] from a [String] type. */
        fun fromString(string: String): CacheIds {
            return values().firstOrNull { it.value == string } ?: UNKNOWN
        }

        fun getDefaultCache(): Set<CacheIds> {
            return setOf(GUILD, USER, CHANNEL, MESSAGE, MEMBER, ATTACHMENT, ROLE, APPLICATION)
        }

        fun getAllCache(): Set<CacheIds> {
            return values().toSet()
        }
    }

    /**
     * The value of the [CacheIds].
     *
     * @return The value of the [CacheIds].
     */
    fun getValue(): String {
        return value
    }

    /**
     * The [CacheType] of the [CacheIds].
     *
     * @return The [CacheType] of the [CacheIds].
     */
    fun getCacheType(): CacheType {
        return cacheType
    }
}
