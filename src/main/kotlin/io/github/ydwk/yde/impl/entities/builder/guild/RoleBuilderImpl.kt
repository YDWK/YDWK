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
package io.github.ydwk.yde.impl.entities.builder.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.builder.guild.RoleBuilder
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.guild.enums.GuildPermission
import io.github.ydwk.yde.impl.entities.guild.RoleImpl
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.util.Checks
import java.awt.Color
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import okhttp3.RequestBody.Companion.toRequestBody

class RoleBuilderImpl(val yde: YDE, val guildId: String?, val name: String) : RoleBuilder {
    private var color: Color? = null
    private var pinned: Boolean? = null
    private var iconHash: String? = null
    private var position: Int? = null
    private var permissions: MutableSet<GuildPermission> = mutableSetOf()
    private var managed: Boolean? = null
    private var mentionable: Boolean? = null

    override fun setColor(color: Color): RoleBuilder {
        this.color = color
        return this
    }

    override fun setPinned(pinned: Boolean): RoleBuilder {
        this.pinned = pinned
        return this
    }

    override fun setIconHash(iconHash: String): RoleBuilder {
        this.iconHash = iconHash
        return this
    }

    override fun setPosition(position: Int): RoleBuilder {
        this.position = position
        return this
    }

    override fun setPermissions(permissions: EnumSet<GuildPermission>): RoleBuilder {
        this.permissions.addAll(permissions)
        return this
    }

    override fun setManaged(managed: Boolean): RoleBuilder {
        this.managed = managed
        return this
    }

    override fun setMentionable(mentionable: Boolean): RoleBuilder {
        this.mentionable = mentionable
        return this
    }

    override val json: JsonNode
        get() {
            val json = yde.objectMapper.createObjectNode()
            Checks.checkLength(name, 100, "name")
            json.put("name", name)
            if (color != null) {
                json.put("color", color!!.rgb)
            }
            if (pinned != null) {
                json.put("pinned", pinned!!)
            }
            if (iconHash != null) {
                json.put("icon", iconHash)
            }
            if (position != null) {
                json.put("position", position)
            }
            if (permissions.isNotEmpty()) {
                json.put("permissions", permissions.sumOf { it.getValue() })
            }
            if (managed != null) {
                json.put("managed", managed!!)
            }
            if (mentionable != null) {
                json.put("mentionable", mentionable!!)
            }
            return json
        }

    override fun create(): CompletableDeferred<Role> {
        if (guildId == null) {
            throw IllegalStateException("Guild id is not set")
        }

        return yde.restApiManager
            .post(json.toString().toRequestBody(), EndPoint.GuildEndpoint.CREATE_ROLE, guildId)
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    RoleImpl(yde, jsonBody, jsonBody["id"].asLong())
                }
            }
    }
}
