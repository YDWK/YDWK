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
package io.github.ydwk.ydwk.impl.interaction.sub

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.entities.Message
import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.entities.guild.Member
import io.github.realyusufismail.ydwk.entities.guild.Role
import io.github.realyusufismail.ydwk.impl.entities.UserImpl
import io.github.realyusufismail.ydwk.impl.entities.guild.MemberImpl
import io.github.realyusufismail.ydwk.impl.entities.guild.RoleImpl
import io.github.realyusufismail.ydwk.interaction.sub.InteractionResolvedData
import io.github.realyusufismail.ydwk.util.GetterSnowFlake

class InteractionResolvedDataImpl(override val ydwk: YDWK, override val json: JsonNode) :
    InteractionResolvedData {
    override val users: Map<GetterSnowFlake, User> =
        json["users"].associate {
            GetterSnowFlake.of(it["id"].asLong()) to UserImpl(it, it["id"].asLong(), ydwk)
        }

    override val members: Map<GetterSnowFlake, Member> =
        json["members"].associate {
            GetterSnowFlake.of(it["user"]["id"].asLong()) to
                MemberImpl(ydwk, it, ydwk.getGuild(it["guild_id"].asLong())!!)
        }

    override val roles: Map<GetterSnowFlake, Role> =
        json["roles"].associate {
            GetterSnowFlake.of(it["id"].asLong()) to RoleImpl(ydwk, it, it["id"].asLong())
        }

    override val channels: Map<GetterSnowFlake, Any> = TODO("Not yet implemented")

    override val messages: Map<GetterSnowFlake, Message> = TODO("Not yet implemented")

    override val attachments: Map<GetterSnowFlake, Any> = TODO("Not yet implemented")
}
