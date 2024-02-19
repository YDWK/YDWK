/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import java.awt.Color

open class UserImpl(
    final override val json: JsonNode,
    override val idAsLong: Long,
    override val yde: YDE,
    override var globalName: String,
    override var avatarHash: String?,
    override val avatar: Avatar,
    override val hasDefaultAvatar: Boolean,
    override val bot: Boolean?,
    override var system: Boolean?,
    override var mfaEnabled: Boolean?,
    override var banner: String?,
    override var accentColor: Color?,
    override var locale: String?,
    override var verified: Boolean?,
    override var flags: Int?,
    override var publicFlags: Int?,
    override var name: String,
) : User {

    constructor(
        user: User
    ) : this(
        user.json,
        user.idAsLong,
        user.yde,
        user.globalName,
        user.avatarHash,
        user.avatar,
        user.hasDefaultAvatar,
        user.bot,
        user.system,
        user.mfaEnabled,
        user.banner,
        user.accentColor,
        user.locale,
        user.verified,
        user.flags,
        user.publicFlags,
        user.name)

    override fun guildAvatarHash(guildId: Long): String? {
        val guild = (yde as YDEImpl).getGuildById(guildId)
        return if (guild != null) {
            val member = guild.getMemberById(idAsLong)
            if (member != null) {
                member.guildAvatarHash
            } else {
                avatarHash
            }
        } else {
            avatarHash
        }
    }

    override fun guildAvatar(guildId: Long): Avatar? {
        val ydeImpl = yde as YDEImpl
        val guild = ydeImpl.getGuildById(guildId)
        return if (guild != null) {
            val member = guild.getMemberById(idAsLong)
            if (member != null) {
                member.guildAvatar
            } else {
                avatar
            }
        } else {
            avatar
        }
    }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
