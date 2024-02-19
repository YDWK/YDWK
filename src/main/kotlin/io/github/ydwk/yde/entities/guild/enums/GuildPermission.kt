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
package io.github.ydwk.yde.entities.guild.enums

import java.util.*

enum class GuildPermission(
    private val value: Long,
    private val description: String,
    private val textChannel: Boolean = false,
    private val voiceChannel: Boolean = false,
) {
    CREATE_INSTANT_INVITE(1 shl 0, "Allows creation of instant invites", true, true),
    KICK_MEMBERS(1 shl 1, "Allows kicking members"),
    BAN_MEMBERS(1 shl 2, "Allows banning members"),
    ADMINISTRATOR(1 shl 3, "Allows all permissions and bypasses channel permission overwrites"),
    MANAGE_CHANNELS(1 shl 4, "Allows management and editing of channels", true, true),
    MANAGE_GUILD(1 shl 5, "Allows management and editing of the guild"),
    ADD_REACTIONS(1 shl 6, "Allows for the addition of reactions to messages", true, true),
    VIEW_AUDIT_LOG(1 shl 7, "Allows for viewing of audit logs"),
    PRIORITY_SPEAKER(1 shl 8, "Allows for using priority speaker in a voice channel", false, true),
    STREAM(1 shl 9, "Allows the user to go live", false, true),
    VIEW_CHANNEL(
        1 shl 10,
        "Allows guild members to view a channel, which includes reading messages in text channels",
        true,
        true),
    SEND_MESSAGES(1 shl 11, "Allows for sending messages in a channel", true, true),
    SEND_TTS_MESSAGES(1 shl 12, "Allows for sending of /tts messages", true, true),
    MANAGE_MESSAGES(1 shl 13, "Allows for deletion of other users messages", true, true),
    EMBED_LINKS(
        1 shl 14, "Links sent by users with this permission will be auto-embedded", true, true),
    ATTACH_FILES(1 shl 15, "Allows for uploading images and files", true, true),
    READ_MESSAGE_HISTORY(1 shl 16, "Allows for reading of message history", true, true),
    MENTION_EVERYONE(
        1 shl 17,
        "Allows for using the @everyone tag to notify all users in a channel, and the @here tag to notify all online users in a channel",
        true,
        true),
    USE_EXTERNAL_EMOJIS(
        1 shl 18, "Allows the usage of custom emojis from other servers", true, true),
    VIEW_GUILD_INSIGHTS(1 shl 19, "Allows for viewing guild insights"),
    CONNECT(1 shl 20, "Allows for joining of a voice channel", false, true),
    SPEAK(1 shl 21, "Allows for speaking in a voice channel", false, true),
    MUTE_MEMBERS(1 shl 22, "Allows for muting members in a voice channel", false, true),
    DEAFEN_MEMBERS(1 shl 23, "Allows for deafening of members in a voice channel", false, true),
    MOVE_MEMBERS(1 shl 24, "Allows for moving of members between voice channels", false, true),
    USE_VAD(1 shl 25, "Allows for using voice-activity-detection in a voice channel", false, true),
    CHANGE_NICKNAME(1 shl 26, "Allows for modification of own nickname"),
    MANAGE_NICKNAMES(1 shl 27, "Allows for modification of other users nicknames"),
    MANAGE_ROLES(1 shl 28, "Allows management and editing of roles", true, true),
    MANAGE_WEBHOOKS(1 shl 29, "Allows management and editing of webhooks", true, true),
    MANAGE_EMOJIS_AND_STICKERS(
        1 shl 30, "Allows management and editing of emojis and stickers", true, true),
    USE_APPLICATION_COMMANDS(
        1 shl 31,
        "Allows members to use application commands, including slash commands and context menu commands.",
        true,
        true),
    REQUEST_TO_SPEAK(
        1 shl 32,
        "Allows for requesting to speak in stage channels. (This permission is under active development and may be changed or removed.)",
        false,
        false),
    MANAGE_EVENTS(
        1 shl 33,
        "Allows for creating, deleting, and managing events and calendars. (This permission is under active development and may be changed or removed.)",
        false,
        true),
    MANAGE_THREADS(
        1 shl 34,
        "Allows for creating, archiving, and managing threads and public/private threads. (This permission is under active development and may be changed or removed.)",
        true),
    CREATE_PUBLIC_THREADS(
        1 shl 35,
        "Allows for creating public threads. (This permission is under active development and may be changed or removed.)",
        true),
    CREATE_PRIVATE_THREADS(
        1 shl 36,
        "Allows for creating private threads. (This permission is under active development and may be changed or removed.)",
        true),
    USE_EXTERNAL_STICKERS(
        1 shl 37,
        "Allows the usage of custom stickers from other servers. (This permission is under active development and may be changed or removed.)",
        true),
    SEND_MESSAGES_IN_THREADS(
        1 shl 38,
        "Allows for sending messages in threads. (This permission is under active development and may be changed or removed.)",
        true),
    USE_EMBEDDED_ACTIVITIES(
        1 shl 39,
        "Allows for using Activities (applications with the EMBEDDED flag) in a voice channel",
        false,
        true),
    MODERATE_MEMBERS(
        1 shl 40,
        "Allows for timing out users to prevent them from sending or reacting to messages in chat and threads, and from speaking in voice and stage channels"),
    UNKNOWN(-1, "Unknown permission"),
    NONE(0, "No permissions");

    companion object {
        val ALL_PERMS: Long = values().map { it.value }.reduce { acc, l -> acc or l }

        /**
         * Returns the [GuildPermission] with the given [value]. If no [GuildPermission] is found,
         * [UNKNOWN] is returned.
         *
         * @param value The value of the [GuildPermission] to find.
         * @return The [GuildPermission] with the given [value].
         */
        fun fromLong(value: Long): GuildPermission {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }

        /**
         * Returns an enum set of [GuildPermission]s with the given [value].
         *
         * @param permissions The value of the [GuildPermission]s to find.
         * @return An enum set of [GuildPermission]s with the given [value].
         */
        fun fromLongs(permissions: Long): EnumSet<GuildPermission> {
            return values()
                .filter { it.value != -1L && permissions and it.value == it.value }
                .toCollection(EnumSet.noneOf(GuildPermission::class.java))
        }
    }

    /**
     * The value of this [GuildPermission].
     *
     * @return The value of this [GuildPermission].
     */
    fun getValue(): Long = value

    /**
     * The description of this [GuildPermission].
     *
     * @return The description of this [GuildPermission].
     */
    fun getDescription(): String = description

    /**
     * Whether this [GuildPermission] is a textChannel permission.
     *
     * @return Whether this [GuildPermission] is a textChannel permission.
     */
    fun isTextChannelPermission(): Boolean = textChannel

    /**
     * Whether this [GuildPermission] is a voiceChannel permission.
     *
     * @return Whether this [GuildPermission] is a voiceChannel permission.
     */
    fun isVoiceChannelPermission(): Boolean = voiceChannel
}
