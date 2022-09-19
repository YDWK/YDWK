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
import java.util.*

open class UserImpl(
    final override val json: JsonNode,
    private val id: Long,
    override val ydwk: YDWK
) : User {
    override var discriminator: String = json["discriminator"].asText()

    override var avatar: String? = json["avatar"].asText()

    override val bot: Boolean
        get() = json.get("bot").asBoolean()

    override var system: Boolean = json["system"].asBoolean()

    override var mfaEnabled: Boolean = json["mfa_enabled"].asBoolean()

    override var banner: String? = json["banner"].asText()

    override var accentColor: Color? = Color(json["accent_color"].asInt())

    override var locale: String? = json["locale"].asText()

    override var verified: Boolean? = json["verified"].asBoolean(false)

    override var flags: Int? = json["flags"].asInt()

    override var premiumType: Int? = json["premium_type"].asInt()

    override var publicFlags: Int? = json["public_flags"].asInt()

    override fun getIdLong(): Long {
        return id
    }

    override var name: String = json["username"].asText()

    override fun formatTo(formatter: Formatter?, flags: Int, width: Int, precision: Int) {
        formatter?.format("%s#%s", name, discriminator)
    }
}
