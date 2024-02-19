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
package io.github.ydwk.yde.entities.builder.guild

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.builder.GenericEntityBuilder
import io.github.ydwk.yde.entities.builder.guild.channel.MessageChannelBuilder
import io.github.ydwk.yde.entities.builder.guild.channel.VoiceChannelBuilder
import io.github.ydwk.yde.entities.guild.enums.ExplicitContentFilterLevel
import io.github.ydwk.yde.entities.guild.enums.MessageNotificationLevel
import io.github.ydwk.yde.entities.guild.enums.SystemChannelFlag
import io.github.ydwk.yde.entities.guild.enums.VerificationLevel

/**
 * Creates a guild.
 *
 * Note :
 * - When using the roles parameter, the first member of the array is used to change properties of
 *   the guild's @everyone role. If you are trying to bootstrap a guild with additional roles, keep
 *   this in mind.
 * - When using the roles parameter, the required id field within each role object is an integer
 *   placeholder, and will be replaced by the API upon consumption. Its purpose is to allow you to
 *   overwrite a role's permissions in a channel when also passing in channels with the channels
 *   array.
 * - When using the channels parameter, the position field is ignored, and none of the default
 *   channels are created.
 * - When using the channels parameter, the id field within each channel object may be set to an
 *   integer placeholder, and will be replaced by the API upon consumption. Its purpose is to allow
 *   you to create GUILD_CATEGORY channels by setting the parent_id field on any children to the
 *   category's id field. Category channels must be listed before any children.
 */
interface GuildBuilder : GenericEntityBuilder<Guild> {
    /**
     * Sets the icon of the guild.
     *
     * @param icon the icon of the guild
     * @return this builder
     */
    fun setIcon(icon: String): GuildBuilder

    /**
     * Sets the verification level of the guild.
     *
     * @param verificationLevel the verification level of the guild
     * @return this builder
     */
    fun setVerificationLevel(verificationLevel: VerificationLevel): GuildBuilder

    /**
     * Sets the default message notification level of the guild.
     *
     * @param defaultMessageNotifications the default message notification level of the guild
     * @return this builder
     */
    fun setDefaultMessageNotifications(
        defaultMessageNotifications: MessageNotificationLevel,
    ): GuildBuilder

    /**
     * Sets the explicit content filter level of the guild.
     *
     * @param explicitContentFilter the explicit content filter level of the guild
     * @return this builder
     */
    fun setExplicitContentFilter(explicitContentFilter: ExplicitContentFilterLevel): GuildBuilder

    /**
     * Sets the roles of the guild.
     *
     * @param roles the roles of the guild
     * @return this builder
     */
    fun setRoles(roles: List<RoleBuilder>): GuildBuilder

    /**
     * Sets the message channels of the guild.
     *
     * @param channels the message channels of the guild
     * @return this builder
     */
    fun setMessageChannels(channels: List<MessageChannelBuilder>): GuildBuilder

    /**
     * Sets the voice channels of the guild.
     *
     * @param channels the voice channels of the guild
     * @return this builder
     */
    fun setVoiceChannels(channels: List<VoiceChannelBuilder>): GuildBuilder

    /**
     * Sets the afk channel id of the guild.
     *
     * @param afkChannelId the afk channel id of the guild
     * @return this builder
     */
    fun setAfkChannelId(afkChannelId: String): GuildBuilder

    /**
     * Sets the afk timeout of the guild.
     *
     * @param afkTimeout the afk timeout of the guild
     * @return this builder
     */
    fun setAfkTimeout(afkTimeout: Int): GuildBuilder

    /**
     * Sets the system channel id of the guild.
     *
     * @param systemChannelId the system channel id of the guild
     * @return this builder
     */
    fun setSystemChannelId(systemChannelId: String): GuildBuilder

    /**
     * Sets the system channel flags of the guild.
     *
     * @param systemChannelFlags the system channel flags of the guild
     * @return this builder
     */
    fun setSystemChannelFlags(systemChannelFlags: SystemChannelFlag): GuildBuilder
}
