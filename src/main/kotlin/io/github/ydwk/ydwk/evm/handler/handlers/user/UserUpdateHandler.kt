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
package io.github.ydwk.ydwk.evm.handler.handlers.user

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.impl.entities.UserImpl
import io.github.ydwk.ydwk.evm.event.events.user.*
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import java.awt.Color
import java.util.*

class UserUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {

    override suspend fun start() {
        val userJson: JsonNode = json

        val userCache = ydwk.cache[userJson.get("id").asText(), CacheIds.GUILD]

        if (userCache == null) {
            ydwk.logger.debug(
                "UserUpdateHandler: User with id ${userJson.get("id").asLong()} not found in cache, will add it")
            val user = UserImpl(json, json.get("id").asLong(), ydwk)
            ydwk.cache[user.id, user] = CacheIds.USER
            return
        }

        val user: User = userCache as User

        val oldName = user.name
        val newName = userJson.get("username").asText()
        if (!Objects.deepEquals(oldName, newName)) {
            user.name = newName
            ydwk.emitEvent(UserNameUpdateEvent(ydwk, user, oldName, newName))
        }

        val oldDiscriminator = user.discriminator
        val newDiscriminator = userJson.get("discriminator").asText()
        if (!Objects.deepEquals(oldDiscriminator, newDiscriminator)) {
            user.discriminator = newDiscriminator
            ydwk.emitEvent(
                UserDiscriminatorUpdateEvent(ydwk, user, oldDiscriminator, newDiscriminator))
        }

        val oldAvatarHash = user.avatarHash
        val oldAvatar = user.avatar
        val newAvatarHash = if (json.hasNonNull("avatar")) json["avatar"].asText() else null
        if (!Objects.deepEquals(oldAvatarHash, newAvatarHash)) {
            user.avatarHash = newAvatarHash
            val newAvatar =
                (user as UserImpl).getAvatar(ydwk, user.discriminator, newAvatarHash, null)
            ydwk.emitEvent(UserAvatarUpdateEvent(ydwk, user, oldAvatar, newAvatar))
        }

        val oldSystem = user.system
        val newSystem = userJson.has("system") && userJson.get("system").asBoolean()
        if (!Objects.deepEquals(oldSystem, newSystem)) {
            user.system = newSystem
            ydwk.emitEvent(UserSystemUpdateEvent(ydwk, user, oldSystem.let { false }, newSystem))
        }

        val oldMfaEnabled = user.mfaEnabled
        val newMfaEnabled = userJson.has("mfa_enabled") && userJson.get("mfa_enabled").asBoolean()
        if (!Objects.deepEquals(oldMfaEnabled, newMfaEnabled)) {
            user.mfaEnabled = newMfaEnabled
            ydwk.emitEvent(
                UserMfaEnabledUpdateEvent(ydwk, user, oldMfaEnabled.let { false }, newMfaEnabled))
        }

        val oldBanner = user.banner
        val newBanner: String? =
            if (userJson.has("banner")) userJson.get("banner").asText() else null
        if (!Objects.deepEquals(oldBanner, newBanner)) {
            user.banner = newBanner
            ydwk.emitEvent(UserBannerUpdateEvent(ydwk, user, oldBanner, newBanner))
        }

        val oldAccentColor = user.accentColor
        val newAccentColor: Color? =
            if (userJson.has("accent_color")) Color(userJson.get("accent_color").asInt()) else null
        if (!Objects.deepEquals(oldAccentColor, newAccentColor)) {
            user.accentColor = newAccentColor
            ydwk.emitEvent(UserAccentColorUpdateEvent(ydwk, user, oldAccentColor, newAccentColor))
        }

        val oldLocale = user.locale
        val newLocale: String =
            if (userJson.has("locale")) userJson.get("locale").asText() else "en-US"
        if (!Objects.deepEquals(oldLocale, newLocale)) {
            user.locale = newLocale
            ydwk.emitEvent(UserLocaleUpdateEvent(ydwk, user, oldLocale, newLocale))
        }

        val oldVerified = user.verified
        val newVerified = userJson.has("verified") && userJson.get("verified").asBoolean()
        if (!Objects.deepEquals(oldVerified, newVerified)) {
            user.verified = newVerified
            ydwk.emitEvent(
                UserVerifiedUpdateEvent(ydwk, user, oldVerified.let { false }, newVerified))
        }

        val oldFlags = user.flags
        val newFlags: Int? = if (userJson.has("flags")) userJson.get("flags").asInt() else null
        if (!Objects.deepEquals(oldFlags, newFlags)) {
            user.flags = newFlags
            ydwk.emitEvent(UserFlagsUpdateEvent(ydwk, user, oldFlags, newFlags))
        }
    }
}
