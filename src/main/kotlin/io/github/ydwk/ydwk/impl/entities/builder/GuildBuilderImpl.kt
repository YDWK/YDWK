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
package io.github.ydwk.ydwk.impl.entities.builder

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.builder.GuildBuilder
import io.github.ydwk.ydwk.entities.builder.guild.ChannelBuilder
import io.github.ydwk.ydwk.entities.builder.guild.RoleBuilder
import io.github.ydwk.ydwk.entities.guild.enums.ExplicitContentFilterLevel
import io.github.ydwk.ydwk.entities.guild.enums.MessageNotificationLevel
import io.github.ydwk.ydwk.entities.guild.enums.SystemChannelFlag
import io.github.ydwk.ydwk.entities.guild.enums.VerificationLevel
import io.github.ydwk.ydwk.impl.entities.GuildImpl
import io.github.ydwk.ydwk.rest.EndPoint
import java.util.concurrent.CompletableFuture
import okhttp3.RequestBody.Companion.toRequestBody

class GuildBuilderImpl(val ydwk: YDWK, val name: String) : GuildBuilder {
    private var icon: String? = null
    private var verificationLevel: VerificationLevel? = null
    private var defaultMessageNotifications: MessageNotificationLevel? = null
    private var explicitContentFilter: ExplicitContentFilterLevel? = null
    private var roles: List<RoleBuilder>? = null
    private var channels: List<ChannelBuilder>? = null
    private var afkChannelId: String? = null
    private var afkTimeout: Int? = null
    private var systemChannelId: String? = null
    private var systemChannelFlags: SystemChannelFlag? = null

    override fun setIcon(icon: String): GuildBuilder {
        this.icon = icon
        return this
    }

    override fun setVerificationLevel(verificationLevel: VerificationLevel): GuildBuilder {
        this.verificationLevel = verificationLevel
        return this
    }

    override fun setDefaultMessageNotifications(
        defaultMessageNotifications: MessageNotificationLevel
    ): GuildBuilder {
        this.defaultMessageNotifications = defaultMessageNotifications
        return this
    }

    override fun setExplicitContentFilter(
        explicitContentFilter: ExplicitContentFilterLevel
    ): GuildBuilder {
        this.explicitContentFilter = explicitContentFilter
        return this
    }

    override fun setRoles(roles: List<RoleBuilder>): GuildBuilder {
        this.roles = roles
        return this
    }

    override fun setChannels(channels: List<ChannelBuilder>): GuildBuilder {
        this.channels = channels
        return this
    }

    override fun setAfkChannelId(afkChannelId: String): GuildBuilder {
        this.afkChannelId = afkChannelId
        return this
    }

    override fun setAfkTimeout(afkTimeout: Int): GuildBuilder {
        this.afkTimeout = afkTimeout
        return this
    }

    override fun setSystemChannelId(systemChannelId: String): GuildBuilder {
        this.systemChannelId = systemChannelId
        return this
    }

    override fun setSystemChannelFlags(systemChannelFlags: SystemChannelFlag): GuildBuilder {
        this.systemChannelFlags = systemChannelFlags
        return this
    }

    override fun create(): CompletableFuture<Guild> {
        val jsonBuilder = ydwk.objectMapper.createObjectNode()
        jsonBuilder.put("name", name)
        if (icon != null) {
            jsonBuilder.put("icon", icon)
        }
        if (verificationLevel != null) {
            jsonBuilder.put("verification_level", verificationLevel!!.getLevel())
        }
        if (defaultMessageNotifications != null) {
            jsonBuilder.put(
                "default_message_notifications", defaultMessageNotifications!!.getValue())
        }
        if (explicitContentFilter != null) {
            jsonBuilder.put("explicit_content_filter", explicitContentFilter!!.getValue())
        }
        if (roles != null) {
            val rolesArray = jsonBuilder.putArray("roles")
            for (role in roles!!) {
                // rolesArray.add(role.build())
            }
        }
        if (channels != null) {
            val channelsArray = jsonBuilder.putArray("channels")
            for (channel in channels!!) {
                // channelsArray.add(channel.build())
            }
        }
        if (afkChannelId != null) {
            jsonBuilder.put("afk_channel_id", afkChannelId)
        }
        if (afkTimeout != null) {
            jsonBuilder.put("afk_timeout", afkTimeout)
        }
        if (systemChannelId != null) {
            jsonBuilder.put("system_channel_id", systemChannelId)
        }
        if (systemChannelFlags != null) {
            jsonBuilder.put("system_channel_flags", systemChannelFlags!!.getValue())
        }
        return ydwk.restApiManager
            .post(jsonBuilder.toString().toRequestBody(), EndPoint.GuildEndpoint.CREATE_GUILD)
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    GuildImpl(ydwk, jsonBody, jsonBody["id"].asLong())
                }
            }
    }
}
