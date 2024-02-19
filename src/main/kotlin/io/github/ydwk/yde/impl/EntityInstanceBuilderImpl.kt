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
package io.github.ydwk.yde.impl

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.EntityInstanceBuilder
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Bot
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.entities.BotImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.user.AvatarImpl
import java.awt.Color
import java.net.MalformedURLException
import java.net.URL

/** Used to build entites */
class EntityInstanceBuilderImpl(val yde: YDEImpl) : EntityInstanceBuilder {

    override fun buildUser(json: JsonNode): User {
        val id = json["id"].asLong()
        val avatarHash = if (json.hasNonNull("avatar")) json["avatar"].asText() else null

        // Deprecated
        val discriminator = json["discriminator"].asText()

        return UserImpl(
            json,
            id,
            yde,
            json["global_name"].asText(),
            avatarHash,
            getAvatar(yde, discriminator, avatarHash, null, id.toLong()),
            json["avatar"].isNull,
            json["bot"].asBoolean(),
            if (json.hasNonNull("system")) json["system"].asBoolean() else null,
            if (json.hasNonNull("mfa_enabled")) json["mfa_enabled"].asBoolean() else null,
            if (json.hasNonNull("banner")) json["banner"].asText() else null,
            if (json.hasNonNull("accent_color")) Color(json["accent_color"].asInt()) else null,
            if (json.hasNonNull("locale")) json["locale"].asText() else null,
            if (json.hasNonNull("verified")) json["verified"].asBoolean() else null,
            if (json.hasNonNull("flags")) json["flags"].asInt() else null,
            if (json.hasNonNull("public_flags")) json["public_flags"].asInt() else null,
            json["username"].asText())
    }

    override fun buildBot(json: JsonNode): Bot {
        return BotImpl(buildUser(json), json["email"].asText())
    }

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
            return AvatarImpl(yde, URL(url.toString()))
        } catch (urlError: MalformedURLException) {
            throw RuntimeException(
                "An issue occurred while creating the avatar URL, either update to the latest version of the library or report this issue to the developer.",
                urlError)
        }
    }
}
