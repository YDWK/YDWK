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
package io.github.ydwk.yde.impl.util

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.EntityInstanceBuilderImpl
import io.github.ydwk.yde.impl.entities.user.AvatarImpl
import java.net.MalformedURLException
import java.net.URL

fun getAvatar(
    yde: YDE,
    discriminator: String,
    avatarHash: String?,
    size: Int?,
    idAsLong: Long
): Avatar {
    val url = StringBuilder(("https://" + "cdn.discordapp.com") + "/")
    val isDiscriminatorZero = discriminator == "0"

    // For users with migrated accounts, default avatar URLs will be based on the user ID
    // instead of the discriminator.
    // The URL can now be calculated using (user_id >> 22) % 6. Users on the legacy username
    // system will continue using discriminator % 5.

    if (avatarHash == null) {
        if (isDiscriminatorZero) {
            url.append("embed/avatars/").append(idAsLong % 5).append(".png")
        } else {
            url.append("embed/avatars/").append(discriminator.toInt() % 5).append(".png")
        }
    } else {
        url.append("avatars/")
            .append(idAsLong)
            .append("/")
            .append(avatarHash)
            .append(if (avatarHash.startsWith("a_")) ".gif" else ".png")
    }

    if (size != null) {
        url.append("?size=").append(size)
    }

    try {
        return AvatarImpl(yde, @Suppress("DEPRECATION") URL(url.toString()))
    } catch (urlError: MalformedURLException) {
        throw RuntimeException(
            "An issue occurred while creating the avatar URL, either update to the latest version of the library or report this issue to the developer.",
            urlError)
    }
}

fun getPermissions(guild: Guild, isOwner: Boolean, roles: List<Role?>, isTimedOut: Boolean): Long {
    if (isOwner) return GuildPermission.ALL_PERMS

    var perms: Long = guild.everyoneRole.rawPermissions
    for (role in roles) {
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

    if (isTimedOut) {
        perms =
            perms and
                (GuildPermission.VIEW_CHANNEL.getValue() or
                    GuildPermission.READ_MESSAGE_HISTORY.getValue())
    }

    return perms
}

fun EntityInstanceBuilderImpl.getGuildAvatar(
    size: Int,
    guildAvatarHash: String?,
    guildId: Long,
    user: User
): Avatar? {
    if (guildAvatarHash != null) {
        val url = StringBuilder(("https://" + "cdn.discordapp.com") + "/")
        url.append("guilds/")
            .append(guildId)
            .append("/")
            .append("users/")
            .append(user.id)
            .append("/")
            .append("avatars/")
            .append(guildAvatarHash)
            .append(if (guildAvatarHash.startsWith("a_")) ".gif" else ".png")
            .append("?size=")
            .append(size)

        try {
            return AvatarImpl(yde, @Suppress("DEPRECATION") URL(url.toString()))
        } catch (urlError: MalformedURLException) {
            throw RuntimeException(
                "An issue occurred while creating the avatar URL, either update to the latest version of the library or report this issue to the developer.",
                urlError)
        }
    } else {
        return null
    }
}

fun checkType(node: JsonNode): Any? {
    return when {
        node.isTextual -> node.asText()
        node.isInt -> node.asInt()
        node.isBoolean -> node.asBoolean()
        node.isLong -> node.asLong()
        node.isDouble or node.isFloat -> node.asDouble()
        else -> null
    }
}

fun getComponentJson(json: JsonNode, customId: String): JsonNode {
    val message = json["message"]
    val mainComponents = message["components"]
    for (mainComponent in mainComponents) {
        val components = mainComponent["components"]
        val component = components.find { it["custom_id"].asText() == customId }
        if (component != null) {
            return component
        }
    }
    throw IllegalStateException("Component not found")
}
