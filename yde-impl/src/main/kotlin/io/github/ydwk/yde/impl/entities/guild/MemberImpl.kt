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
package io.github.ydwk.yde.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.PermissionEntity
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.PermissionEntityImpl
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake
import java.util.*

internal class MemberImpl(
    override val yde: YDEImpl,
    override val json: JsonNode,
    override val guildId: GetterSnowFlake,
    override var user: User,
    override var nick: String?,
    override var guildAvatarHash: String?,
    override val guildAvatar: Avatar?,
    override val roleIds: List<GetterSnowFlake>,
    override var joinedAt: String?,
    override var premiumSince: String?,
    override var deaf: Boolean,
    override var mute: Boolean,
    override var pending: Boolean,
    override var timedOutUntil: String?,
    override var voiceState: VoiceState?,
    override var name: String,
    override val idAsLong: Long = guildId.asLong + user.idAsLong
) : Member {
    override fun isOwner(guild: Guild): Boolean {
        return guild.ownerId.asString == user.id
    }

    override fun permissionEntity(guild: Guild, roles: List<Role>): PermissionEntity {
        return PermissionEntityImpl(guild, isOwner(guild), roles, isTimedOut)
    }

    override suspend fun createDmChannel(): RestResult<DmChannel> {
        return user.createDmChannel()
    }

    override suspend fun addRole(role: Role): RestResult<NoResult> {
        return yde.restAPIMethodGetters
            .getMemberRestAPIMethods()
            .addRoleToMember(guildId.asLong, idAsLong, role.idAsLong)
    }

    override suspend fun addRoles(roles: List<Role>): List<RestResult<NoResult>> {
        return roles.map { addRole(it) }
    }

    override suspend fun removeRole(role: Role): RestResult<NoResult> {
        return yde.restAPIMethodGetters
            .getMemberRestAPIMethods()
            .removeRoleFromMember(guildId.asLong, idAsLong, role.idAsLong)
    }

    override suspend fun removeRoles(roles: List<Role>): List<RestResult<NoResult>> {
        return roles.map { removeRole(it) }
    }

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
