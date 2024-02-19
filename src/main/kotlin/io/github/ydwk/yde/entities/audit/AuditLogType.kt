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
package io.github.ydwk.yde.entities.audit

import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Sticker
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.Role
import io.github.ydwk.yde.entities.util.GenericEntity
import kotlin.reflect.KClass

enum class AuditLogType(
    private val type: Int,
    private val objectChanged: KClass<out GenericEntity>? = null,
) {
    /** The guild settings were updated. */
    GUILD_UPDATE(1, Guild::class),

    /** A channel was created. */
    CHANNEL_CREATE(10, Channel::class),

    /** A channel was updated. */
    CHANNEL_UPDATE(11, Channel::class),

    /** A channel was deleted. */
    CHANNEL_DELETE(12, Channel::class),

    /** A channel permission overwrite was created. */
    CHANNEL_OVERWRITE_CREATE(13, Channel::class),

    /** A channel permission overwrite was updated. */
    CHANNEL_OVERWRITE_UPDATE(14, Channel::class),

    /** A channel permission overwrite was deleted. */
    CHANNEL_OVERWRITE_DELETE(15, Channel::class),

    /** A member was kicked from the guild. */
    MEMBER_KICK(20),

    /** A member was banned from the guild. */
    MEMBER_BAN_ADD(21),

    /** A member was unbanned from the guild. */
    MEMBER_BAN_REMOVE(22),

    /** A member was updated. */
    MEMBER_UPDATE(23, Member::class),

    /** A member was granted a role. */
    MEMBER_ROLE_UPDATE(24, Role::class),

    /** A member was moved to a different voice channel. */
    MEMBER_MOVE(25),

    /** A member was disconnected from a voice channel. */
    MEMBER_DISCONNECT(26),

    /** A bot was added to the guild. */
    BOT_ADD(27),

    /** A role was created. */
    ROLE_CREATE(30, Role::class),

    /** A role was updated. */
    ROLE_UPDATE(31, Role::class),

    /** A role was deleted. */
    ROLE_DELETE(32, Role::class),

    /** An invite was created. */
    INVITE_CREATE(40),

    /** An invite was updated. */
    INVITE_UPDATE(41),

    /** An invite was deleted. */
    INVITE_DELETE(42),

    /** A webhook was created. */
    WEBHOOK_CREATE(50),

    /** A webhook was updated. */
    WEBHOOK_UPDATE(51),

    /** A webhook was deleted. */
    WEBHOOK_DELETE(52),

    /** An emoji was created. */
    EMOJI_CREATE(60, Emoji::class),

    /** An emoji was updated. */
    EMOJI_UPDATE(61, Emoji::class),

    /** An emoji was deleted. */
    EMOJI_DELETE(62, Emoji::class),

    /** A message was deleted. */
    MESSAGE_DELETE(72),

    /** A bulk message was deleted. */
    MESSAGE_BULK_DELETE(73),

    /** A message was pinned. */
    MESSAGE_PIN(74),

    /** A message was unpinned. */
    MESSAGE_UNPIN(75),

    /** An integration was created. */
    INTEGRATION_CREATE(80),

    /** An integration was updated. */
    INTEGRATION_UPDATE(81),

    /** An integration was deleted. */
    INTEGRATION_DELETE(82),

    /** A stage instance was created. */
    STAGE_INSTANCE_CREATE(83),

    /** A stage instance was updated. */
    STAGE_INSTANCE_UPDATE(84),

    /** A stage instance was deleted. */
    STAGE_INSTANCE_DELETE(85),

    /** A sticker was created. */
    STICKER_CREATE(90, Sticker::class),

    /** A sticker was updated. */
    STICKER_UPDATE(91, Sticker::class),

    /** A sticker was deleted. */
    STICKER_DELETE(92, Sticker::class),

    /** A guild scheduled event was created. */
    GUILD_SCHEDULED_EVENT_CREATE(100),

    /** A guild scheduled event was updated. */
    GUILD_SCHEDULED_EVENT_UPDATE(101),

    /** A guild scheduled event was deleted. */
    GUILD_SCHEDULED_EVENT_DELETE(102),

    /** A thread was created. */
    THREAD_CREATE(110),

    /** A thread was updated. */
    THREAD_UPDATE(111),

    /** A thread was deleted. */
    THREAD_DELETE(112),

    /** An application command permissions were updated. */
    APPLICATION_COMMAND_PERMISSION_UPDATE(121),

    /** An auto moderation rule was created. */
    AUTO_MODERATION_RULE_CREATE(140),

    /** An auto moderation rule was updated. */
    AUTO_MODERATION_RULE_UPDATE(141),

    /** An auto moderation rule was deleted. */
    AUTO_MODERATION_RULE_DELETE(142),

    /** A message was blocked. */
    AUTO_MODERATION_BLOCK_MESSAGE(143),

    /** A message was flagged by AutoMod. */
    AUTO_MODERATION_FLAG_MESSAGE(144),

    /** A member was timed out by AutoMod. */
    AUTO_MODERATION_USER_COMMUNICATION_DISABLED(145),

    /** An unknown audit log type. */
    UNKNOWN(-1);

    companion object {
        /**
         * The [AuditLogType] of the provided [type].
         *
         * @param type The type to get the [AuditLogType] of.
         * @return The [AuditLogType] of the provided [type].
         */
        fun fromInt(type: Int): AuditLogType {
            return values().firstOrNull { it.type == type } ?: UNKNOWN
        }
    }

    /**
     * The type of this [AuditLogType].
     *
     * @return The type of this [AuditLogType].
     */
    fun getType(): Int {
        return type
    }

    /**
     * The object changed by this [AuditLogType].
     *
     * @return The object changed by this [AuditLogType].
     */
    fun getObjectChanged(): KClass<out GenericEntity>? {
        return objectChanged
    }
}
