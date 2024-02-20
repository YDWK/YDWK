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
import io.github.ydwk.yde.entities.*
import io.github.ydwk.yde.entities.guild.*
import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import io.github.ydwk.yde.impl.entities.BotImpl
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl
import io.github.ydwk.yde.util.*
import java.awt.Color

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
            getAvatar(yde, discriminator, avatarHash, null, id),
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

    override fun buildGuild(json: JsonNode): Guild {
        TODO("Not yet implemented")
    }

    override fun buildMessage(json: JsonNode): Message {
        TODO("Not yet implemented")
    }

    override fun buildSticker(json: JsonNode): Sticker {
        TODO("Not yet implemented")
    }

    override fun buildUnavailableGuild(json: JsonNode): UnavailableGuild {
        TODO("Not yet implemented")
    }

    override fun buildVoiceState(json: JsonNode): VoiceState {
        TODO("Not yet implemented")
    }

    override fun buildAuditLog(json: JsonNode): AuditLog {
        TODO("Not yet implemented")
    }

    override fun buildApplication(json: JsonNode): Application {
        TODO("Not yet implemented")
    }

    override fun buildBan(json: JsonNode): Ban {
        TODO("Not yet implemented")
    }

    override fun buildGuildScheduledEvent(json: JsonNode): GuildScheduledEvent {
        TODO("Not yet implemented")
    }

    override fun buildInvite(json: JsonNode): Invite {
        TODO("Not yet implemented")
    }

    override fun buildMember(json: JsonNode, guild: Guild, backUpUser: User?): Member {
        val roleIds = json["roles"].map { GetterSnowFlake.of(it.asLong()) }

        val roles =
            roleIds.map {
                if (guild.getRoleById(it.asLong) != null) guild.getRoleById(it.asLong) else null
            }

        val user = if (json.has("user")) buildUser(json["user"]) else backUpUser

        val isOwner = guild.ownerId.asString == user?.id

        val isTimedOut =
            if (json.has("communication_disabled_until"))
                json["communication_disabled_until"].asBoolean()
            else null

        val guildAvatarHash = if (json.has("avatar")) json["avatar"].asText() else null

        val nickName = if (json.has("nick")) json["nick"].asText() else null

        return MemberImpl(
            yde,
            json,
            guild,
            backUpUser,
            GuildPermission.fromLongs(getPermissions(guild, isOwner, roles, isTimedOut ?: false)),
            user ?: throw IllegalStateException("User is null"),
            nickName,
            guildAvatarHash,
            getGuildAvatar(1024, guildAvatarHash, guild, user),
            roleIds,
            roles,
            if (json.has("joined_at")) formatZonedDateTime(json["joined_at"].asText()) else null,
            if (json.has("premium_since")) formatZonedDateTime(json["premium_since"].asText())
            else null,
            if (json.has("deaf")) json["deaf"].asBoolean() else false,
            if (json.has("mute")) json["mute"].asBoolean() else false,
            json["pending"].asBoolean(),
            if (json.has("communication_disabled_until"))
                formatZonedDateTime(json["communication_disabled_until"].asText())
            else null,
            isOwner,
            null,
            nickName ?: user.name)
    }

    override fun buildRole(json: JsonNode): Role {
        TODO("Not yet implemented")
    }

    override fun buildWelcomeScreen(json: JsonNode): WelcomeScreen {
        TODO("Not yet implemented")
    }
}
