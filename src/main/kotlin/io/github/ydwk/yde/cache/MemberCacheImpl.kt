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

import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl

/** Discord's Member do not have a unique ID, so we need to use a combination of the user id */
class MemberCacheImpl(allowedCache: Set<CacheIds>, yde: YDEImpl) :
    MemberCache, PerpetualCache(allowedCache, yde) {
    override fun set(guildId: String, userId: String, value: Member) {
        super.set(guildId + userId, value, CacheIds.MEMBER)
    }

    override fun get(guildId: String, userId: String): Member? {
        return super.get(guildId + userId, CacheIds.MEMBER) as Member?
    }

    override fun getOrPut(value: Member): Member {
        return super.getOrPut(value.id, value, CacheIds.MEMBER) as Member
    }

    override fun updateVoiceState(member: Member, voiceState: VoiceState, add: Boolean) {
        val memberImpl = member as MemberImpl
        if (add) {
            memberImpl.voiceState = voiceState
        } else {
            memberImpl.voiceState = null
        }
    }

    override fun remove(guildId: String, userId: String) {
        super.remove(guildId + userId, CacheIds.MEMBER)
    }

    override fun contains(guildId: String, userId: String): Boolean {
        return super.contains(guildId + userId, CacheIds.MEMBER)
    }

    override fun values(): List<Member> {
        return super.values(CacheIds.MEMBER).map { it as Member }
    }
}
