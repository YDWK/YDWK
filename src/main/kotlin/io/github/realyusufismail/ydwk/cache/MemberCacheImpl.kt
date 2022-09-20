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

/**
 * Discord's Member do not have a unique ID, so we need to use a combination of the guild ID and the
 * user ID
 */
class MemberCacheImpl : MemberCache, PerpetualCache() {
    override fun set(userId: Long, guildId: Long, value: Any) {
        val memberId: Long = userId + guildId
        this[memberId] = value
    }

    override fun get(userId: Long, guildId: Long): Any? {
        val memberId: Long = userId + guildId
        return this[memberId]
    }

    override fun remove(userId: Long, guildId: Long) {
        val memberId: Long = userId + guildId
        this.remove(memberId)
    }
}
