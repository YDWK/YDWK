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
package io.github.realyusufismail.ydwk.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.entities.User
import java.awt.Color

open class UserImpl(private val json: JsonNode, private val id: Long, override val ydwk: YDWK) :
    User {
    override val name: String
        get() = json["name"].asText()

    override val discriminator: String
        get() = json["discriminator"].asText()

    override val avatar: String?
        get() = json.get("avatar").asText()

    override val bot: Boolean
        get() = json.get("bot").asBoolean()

    override val system: Boolean
        get() = json.get("system").asBoolean()

    override val mfaEnabled: Boolean
        get() = json.get("mfa_enabled").asBoolean()

    override val banner: String?
        get() = json.get("banner").asText()

    override val accentColor: Color?
        get() = json.get("accent_color").asInt().let { Color(it) }

    override val locale: String?
        get() = json.get("locale").asText()

    override val verified: Boolean?
        get() = json.get("verified").asBoolean()

    override val email: String?
        get() = json.get("email").asText()

    override val flags: Int?
        get() = json.get("flags").asInt()

    override val premiumType: Int?
        get() = json.get("premium_type").asInt()

    override val publicFlags: Int?
        get() = json.get("public_flags").asInt()

    override fun getIdLong(): Long {
        return id
    }
}
