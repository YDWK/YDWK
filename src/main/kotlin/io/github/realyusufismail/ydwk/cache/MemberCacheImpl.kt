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

import io.github.realyusufismail.ydwk.entities.guild.Member

/**
 * Discord's Member do not have a unique ID, so we need to use a combination of the guild ID and the
 * user ID
 */
class MemberCacheImpl : MemberCache, PerpetualCache() {
    override fun set(userId: String, guildId: String, value: Member) {
        val memberId: String = userId + guildId
        this[memberId, value] = CacheType.MEMBER
    }

    override fun get(userId: String, guildId: String): Any? {
        val memberId: String = userId + guildId
        return this[memberId, CacheType.MEMBER]
    }

    override fun remove(userId: String, guildId: String) {
        val memberId: String = userId + guildId
        this.remove(memberId, CacheType.MEMBER)
    }
}
