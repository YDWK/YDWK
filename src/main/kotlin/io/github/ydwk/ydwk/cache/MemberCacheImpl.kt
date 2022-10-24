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

import io.github.ydwk.ydwk.entities.guild.Member

/** Discord's Member do not have a unique ID, so we need to use a combination of the user id */
class MemberCacheImpl(allowedCache: Set<CacheIds>) : MemberCache, PerpetualCache(allowedCache) {
    override fun set(userId: String, value: Member) {
        super.set(userId, value, CacheIds.MEMBER)
    }

    override fun get(userId: String): Any? {
        return super.get(userId, CacheIds.MEMBER)
    }

    override fun remove(userId: String) {
        super.remove(userId, CacheIds.MEMBER)
    }
}
