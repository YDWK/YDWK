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
package io.github.ydwk.yde.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.user.AvatarImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.formatZonedDateTime
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MemberImpl(
    override val yde: YDEImpl,
    override val json: JsonNode,
    override val guild: Guild,
    backupUser: User? = null,
) : Member {

    override var user: User =
        if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), yde)
        else backupUser ?: throw IllegalStateException("Member must have a user")

    override var nick: String? = if (json.has("nick")) json["nick"].asText() else null

    override var guildAvatarHash: String? =
        if (json.has("avatar")) json["avatar"].asText() else null

    override val guildAvatar: Avatar?
        get() = getGuildAvatar(1024)

    public fun getGuildAvatar(size: Int): Avatar? {
        if (guildAvatarHash != null) {
            val url = StringBuilder(("https://" + "cdn.discordapp.com") + "/")
            url.append("guilds/")
                .append(guild.id)
                .append("/")
                .append("users/")
                .append(user.id)
                .append("/")
                .append("avatars/")
                .append(guildAvatarHash)
                .append(if (guildAvatarHash!!.startsWith("a_")) ".gif" else ".png")
                .append("?size=")
                .append(size)

            try {
                return AvatarImpl(yde, URL(url.toString()))
            } catch (urlError: MalformedURLException) {
                throw RuntimeException(
                    "An issue occurred while creating the avatar URL, either update to the latest version of the library or report this issue to the developer.",
                    urlError)
            }
        } else {
            return null
        }
    }

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
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
