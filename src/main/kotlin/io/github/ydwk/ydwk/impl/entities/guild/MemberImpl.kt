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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.entities.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.entities.Guild
import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.impl.entities.UserImpl
import io.github.realyusufismail.ydwk.util.formatZonedDateTime

class MemberImpl(override val ydwk: YDWK, override val json: JsonNode, override val guild: Guild) :
    Member {

    override var user: User? =
        if (json.has("user")) UserImpl(json["user"], json["user"]["id"].asLong(), ydwk) else null

    override var nick: String? = if (json.has("nick")) json["nick"].asText() else null

    override var avatar: String? = if (json.has("avatar")) json["avatar"].asText() else null

    override var joinedAt: String? =
        if (json.has("joined_at")) formatZonedDateTime(json["joined_at"].asText()) else null

    override var premiumSince: String? =
        if (json.has("premium_since")) formatZonedDateTime(json["premium_since"].asText()) else null

    override var deaf: Boolean = json["deaf"].asBoolean()

    override var mute: Boolean = json["mute"].asBoolean()

    override var pending: Boolean = json["pending"].asBoolean()

    override var permissions: String? =
        if (json.has("permissions")) json["permissions"].asText() else null

    override var timedOutUntil: String? =
        if (json.has("communication_disabled_until"))
            formatZonedDateTime(json["communication_disabled_until"].asText())
        else null

    override var name: String = if (nick != null) nick!! else if (user != null) user!!.name else ""
}
