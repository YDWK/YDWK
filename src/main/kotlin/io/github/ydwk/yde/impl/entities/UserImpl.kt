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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.impl.entities.user.AvatarImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import java.awt.Color
import java.net.MalformedURLException
import java.net.URL

open class UserImpl(
    final override val json: JsonNode,
    override val idAsLong: Long,
    override val yde: YDE,
) : User {

    @Deprecated("Discord has removed discriminator in favour of the new @mention system.")
    override var discriminator: String = json["discriminator"].asText()

    override var globalName: String = json["global_name"].asText()

    override var avatarHash: String? =
        if (json.hasNonNull("avatar")) json["avatar"].asText() else null

    override fun guildAvatarHash(guildId: Long): String? {
        val ydeImpl = yde as YDEImpl
        val guild = ydeImpl.getGuildById(guildId)
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

    override val avatar: Avatar
        get() = getAvatar(yde, discriminator, avatarHash, null)

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

    override val hasDefaultAvatar: Boolean
        get() = avatarHash == null

    public fun getAvatar(yde: YDE, discriminator: String, avatarHash: String?, size: Int?): Avatar {
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
            return AvatarImpl(yde, URL(url.toString()))
        } catch (urlError: MalformedURLException) {
            throw RuntimeException(
                "An issue occurred while creating the avatar URL, either update to the latest version of the library or report this issue to the developer.",
                urlError)
        }
    }

    override val bot: Boolean
        get() = json.get("bot").asBoolean()

    override var system: Boolean? = if (json.has("system")) json["system"].asBoolean() else null

    override var mfaEnabled: Boolean? =
        if (json.has("mfa_enabled")) json["mfa_enabled"].asBoolean() else null

    // if null return null else return the string
    override var banner: String? =
        if (json.hasNonNull("banner")) json.get("banner").asText() else null

    override var accentColor: Color? =
        if (json.hasNonNull("accent_color")) Color(json.get("accent_color").asInt()) else null

    override var locale: String? =
        if (json.hasNonNull("locale")) json.get("locale").asText() else null

    override var verified: Boolean? =
        if (json.hasNonNull("verified")) json.get("verified").asBoolean() else null

    override var flags: Int? = if (json.hasNonNull("flags")) json.get("flags").asInt() else null

    override var publicFlags: Int? =
        if (json.hasNonNull("public_flags")) json.get("public_flags").asInt() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }

    override var name: String = json["username"].asText()
}
