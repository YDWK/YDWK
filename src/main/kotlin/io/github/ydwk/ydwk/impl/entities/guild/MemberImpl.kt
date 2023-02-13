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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.VoiceState
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.guild.enums.GuildPermission
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.UserImpl
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.rest.result.NoResult
import io.github.ydwk.ydwk.util.EntityToStringBuilder
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.formatZonedDateTime
import java.util.*
import java.util.concurrent.CompletableFuture

class MemberImpl(
    override val ydwk: YDWKImpl,
    override val json: JsonNode,
    override val guild: Guild,
    backupUser: User? = null
) : Member {

    override var user: User =
        if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), ydwk)
        else backupUser ?: throw IllegalStateException("Member must have a user")

    override var nick: String? = if (json.has("nick")) json["nick"].asText() else null

    override var avatar: String? = if (json.has("avatar")) json["avatar"].asText() else null
    override val roleIds: List<GetterSnowFlake>
        get() = json["roles"].map { GetterSnowFlake.of(it.asLong()) }

    override val roles: List<Role?>
        get() =
            roleIds.map {
                if (guild.getRoleById(it.asLong) != null) guild.getRoleById(it.asLong) else null
            }

    override var joinedAt: String? =
        if (json.has("joined_at")) formatZonedDateTime(json["joined_at"].asText()) else null

    override var premiumSince: String? =
        if (json.has("premium_since")) formatZonedDateTime(json["premium_since"].asText()) else null

    override var deaf: Boolean = json.has("deaf") && json["deaf"].asBoolean()

    override var mute: Boolean = json.has("mute") && json["mute"].asBoolean()

    override var pending: Boolean = json["pending"].asBoolean()

    override var timedOutUntil: String? =
        if (json.has("communication_disabled_until"))
            formatZonedDateTime(json["communication_disabled_until"].asText())
        else null

    override val isOwner: Boolean
        get() = guild.ownerId.asString == user.id

    override var voiceState: VoiceState? = null

    override fun addRole(role: Role): CompletableFuture<NoResult> {
        return ydwk.restApiManager
            .put(null, EndPoint.GuildEndpoint.ADD_ROLE, guild.id, user.id, role.id)
            .executeWithNoResult()
    }

    override fun addRoles(roles: List<Role>): List<CompletableFuture<NoResult>> {
        for (role in roles) {
            addRole(role)
        }

        return roles.map { addRole(it) }
    }

    override fun removeRole(role: Role): CompletableFuture<NoResult> {
        return ydwk.restApiManager
            .delete(null, EndPoint.GuildEndpoint.REMOVE_ROLE, guild.id, user.id, role.id)
            .executeWithNoResult()
    }

    override fun removeRoles(roles: List<Role>): List<CompletableFuture<NoResult>> {
        for (role in roles) {
            removeRole(role)
        }

        return roles.map { removeRole(it) }
    }

    override val permissions: EnumSet<GuildPermission>
        get() = GuildPermission.fromLongs(getPermissions(this))

    private fun getPermissions(member: Member): Long {
        if (member.isOwner) return GuildPermission.ALL_PERMS

        var perms: Long = guild.everyoneRole.rawPermissions
        for (role in member.roles) {
            if (role != null) {
                perms = perms or role.rawPermissions

                if (perms and GuildPermission.ADMINISTRATOR.getValue() ==
                    GuildPermission.ADMINISTRATOR.getValue()) {
                    return GuildPermission.ALL_PERMS
                }
            } else {
                perms = perms or GuildPermission.NONE.getValue()
            }
        }

        if (member.isTimedOut) {
            perms =
                perms and
                    (GuildPermission.VIEW_CHANNEL.getValue() or
                        GuildPermission.READ_MESSAGE_HISTORY.getValue())
        }

        return perms
    }

    override fun hasPermission(vararg permission: GuildPermission): Boolean {
        return permissions.containsAll(permission.toList())
    }

    override fun hasPermission(permission: Collection<GuildPermission>): Boolean {
        return permissions.containsAll(permission)
    }

    override var name: String = if (nick != null) nick!! else user.name

    override val idAsLong: Long
        get() = guild.idAsLong + user.idAsLong

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).name(this.name).toString()
    }
}
