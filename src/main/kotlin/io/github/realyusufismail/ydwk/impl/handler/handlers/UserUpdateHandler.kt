/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.realyusufismail.ydwk.impl.handler.handlers

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.impl.entities.UserImpl
import io.github.realyusufismail.ydwk.impl.handler.Handler
import java.awt.Color
import java.util.*

class UserUpdateHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {

    override fun start() {
        val userJson: JsonNode = json

        val userCache = ydwk.cache[userJson.get("id").asLong()]

        if (userCache == null) {
            ydwk.logger.warn(
                "UserUpdateHandler: User with id ${userJson.get("id").asLong()} not found in cache, will add it")
            val user = UserImpl(json, json.get("id").asLong(), ydwk)
            ydwk.cache[user.getIdLong()] = user
            return
        }

        val user: User = userCache as User

        val oldName = user.name
        val newName = userJson.get("username").asText()
        if (!Objects.deepEquals(oldName, newName)) {
            user.name = newName
        }

        val oldDiscriminator = user.discriminator
        val newDiscriminator = userJson.get("discriminator").asText()
        if (!Objects.deepEquals(oldDiscriminator, newDiscriminator)) {
            user.discriminator = newDiscriminator
        }

        val oldAvatar = user.avatar
        val newAvatar = userJson.get("avatar").asText()
        if (!Objects.deepEquals(oldAvatar, newAvatar)) {
            user.avatar = newAvatar
        }

        val oldSystem = user.system
        val newSystem = userJson.get("system").asBoolean()
        if (oldSystem != newSystem) {
            user.system = newSystem
        }

        val oldMfaEnabled = user.mfaEnabled
        val newMfaEnabled = userJson.get("mfa_enabled").asBoolean()
        if (oldMfaEnabled != newMfaEnabled) {
            user.mfaEnabled = newMfaEnabled
        }

        val oldBanner = user.banner
        val newBanner = userJson.get("banner").asText()

        if (!Objects.deepEquals(oldBanner, newBanner)) {
            user.banner = newBanner
        }

        val oldAccentColor = user.accentColor
        val newAccentColor = Color(userJson.get("accent_color").asInt())
        if (!Objects.deepEquals(oldAccentColor, newAccentColor)) {
            user.accentColor = newAccentColor
        }

        val oldLocale = user.banner
        val newLocale = userJson.get("locale").asText()
        if (!Objects.deepEquals(oldLocale, newLocale)) {
            user.locale = newLocale
        }

        val oldVerified = user.verified
        val newVerified = userJson.get("verified").asBoolean()
        if (oldVerified != newVerified) {
            user.verified = newVerified
        }

        val oldFlags = user.flags
        val newFlags = userJson.get("flags").asInt()
        if (oldFlags != newFlags) {
            user.flags = newFlags
        }

        val oldPremiumType = user.premiumType
        val newPremiumType = userJson.get("premium_type").asInt()
        if (oldPremiumType != newPremiumType) {
            user.premiumType = newPremiumType
        }
    }
}
