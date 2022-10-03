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

enum class CacheType(private val ids: CacheIds) {
    GUILD(CacheIds.GUILD),
    CHANNEL(CacheIds.CHANNEL),
    USER(CacheIds.USER),
    MEMBER(CacheIds.MEMBER),
    ROLE(CacheIds.ROLE),
    MESSAGE(CacheIds.MESSAGE),
    EMOJI(CacheIds.EMOJI),
    VOICE_STATE(CacheIds.VOICE_STATE),
    STICKER(CacheIds.STICKER),
    APPLICATION_COMMAND(CacheIds.APPLICATION_COMMAND),
    APPLICATION(CacheIds.APPLICATION),
    UNKNOWN(CacheIds.UNKNOWN);

    companion object {
        fun fromCacheIds(ids: CacheIds): CacheType {
            return values().firstOrNull { it.ids == ids } ?: UNKNOWN
        }

        fun getDefaultCache(): Set<CacheType> {
            return setOf(
                GUILD,
                CHANNEL,
                USER,
                MEMBER,
                ROLE,
                MESSAGE,
                EMOJI,
                VOICE_STATE,
                STICKER,
                APPLICATION_COMMAND,
                APPLICATION)
        }
    }

    fun getCacheIds(): CacheIds {
        return ids
    }
}