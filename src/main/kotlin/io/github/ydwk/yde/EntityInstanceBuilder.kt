/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.*
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.entities.audit.AuditLogChange
import io.github.ydwk.yde.entities.audit.AuditLogEntry
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.entities.channel.guild.forum.DefaultReactionEmoji
import io.github.ydwk.yde.entities.channel.guild.forum.ForumTag
import io.github.ydwk.yde.entities.channel.guild.forum.GuildForumChannel
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.yde.entities.channel.guild.message.news.GuildNewsChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.GuildTextChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.entities.channel.guild.vc.GuildStageChannel
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.entities.guild.*
import io.github.ydwk.yde.entities.guild.role.RoleTag
import io.github.ydwk.yde.entities.guild.ws.WelcomeChannel
import io.github.ydwk.yde.entities.message.*
import io.github.ydwk.yde.entities.message.embed.*
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.entities.user.Avatar

interface EntityInstanceBuilder {

    /**
     * Used to build an instance of [User]
     *
     * @param json the json
     * @return [User] the user
     */
    fun buildUser(json: JsonNode): User

    /**
     * Used to build an instance of [Bot]
     *
     * @param json the json
     * @return [Bot] the bot
     */
    fun buildBot(json: JsonNode): Bot

    /**
     * Used to build an instance of [Guild]
     *
     * @param json the json
     * @return [Guild] the guild
     */
    fun buildGuild(json: JsonNode): Guild

    /**
     * Used to build an instance of [Message]
     *
     * @param json the json
     * @return [Message] the message
     */
    fun buildMessage(json: JsonNode): Message

    /**
     * Used to build an instance of [Sticker]
     *
     * @param json the json
     * @return [Sticker] the sticker
     */
    fun buildSticker(json: JsonNode): Sticker

    /**
     * Used to build an instance of [UnavailableGuild]
     *
     * @param json the json
     * @return [UnavailableGuild] the unavailable guild
     */
    fun buildUnavailableGuild(json: JsonNode): UnavailableGuild

    /**
     * Used to build an instance of [VoiceState]
     *
     * @param json the json
     * @param backUpGuild the back up guild
     * @return [VoiceState] the voice state
     */
    fun buildVoiceState(json: JsonNode, backUpGuild: Guild? = null): VoiceState

    /**
     * Used to build an instance of [VoiceState]
     *
     * @param json the json
     * @param backUpGuild the back up guild json
     * @return [VoiceState] the voice state
     */
    fun buildVoiceState(json: JsonNode, backUpGuild: JsonNode? = null): VoiceState {
        return buildVoiceState(json, backUpGuild?.let { buildGuild(it) })
    }

    /**
     * Used to build an instance of [AuditLog]
     *
     * @param json the json
     * @return [AuditLog] the audit log
     */
    fun buildAuditLog(json: JsonNode): AuditLog

    /**
     * Used to build an instance of [Application]
     *
     * @param json the json
     * @return [Application] the application
     */
    fun buildApplication(json: JsonNode): Application

    // Guild entities

    /**
     * Used to build an instance of [Ban]
     *
     * @param json the json
     * @return [Ban] the ban
     */
    fun buildBan(json: JsonNode): Ban

    /**
     * Used to build an instance of [GuildScheduledEvent]
     *
     * @param json the json
     * @return [GuildScheduledEvent] the guild scheduled event
     */
    fun buildGuildScheduledEvent(json: JsonNode): GuildScheduledEvent

    /**
     * Used to build an instance of [Invite]
     *
     * @param json the json
     * @return [Invite] the invite
     */
    fun buildInvite(json: JsonNode): Invite

    /**
     * Used to build an instance of [Member]
     *
     * @param json the json
     * @param guild the guild
     * @param backUpUser the back up user
     * @return [Member] the member
     */
    fun buildMember(json: JsonNode, guild: Guild, backUpUser: User? = null): Member

    /**
     * Used to build an instance of [Member]
     *
     * @param json the json
     * @param guild the guild
     * @param backUpUser the back up user json
     * @return [Member] the member
     */
    fun buildMember(json: JsonNode, guild: Guild, backUpUser: JsonNode? = null): Member {
        return buildMember(json, guild, backUpUser?.let { buildUser(it) })
    }

    /**
     * Used to build an instance of [Role]
     *
     * @param json the json
     * @return [Role] the role
     */
    fun buildRole(json: JsonNode): Role

    /**
     * Used to build an instance of [WelcomeScreen]
     *
     * @param json the json
     * @return [WelcomeScreen] the welcome screen
     */
    fun buildWelcomeScreen(json: JsonNode): WelcomeScreen

    // WelcomeScreen entities

    /**
     * Used to build an instance of [WelcomeChannel]
     *
     * @param json the json
     * @return [WelcomeChannel] the welcome screen channel
     */
    fun buildWelcomeScreenChannel(json: JsonNode): WelcomeChannel

    // Role entities

    /**
     * Used to build an instance of [RoleTag]
     *
     * @param json the json
     * @return [RoleTag] the role tag
     */
    fun buildRoleTag(json: JsonNode): RoleTag

    // Channel entities

    /**
     * Used to build an instance of [Channel]
     *
     * @param json the json
     * @return [Channel] the channel
     */
    fun buildChannel(json: JsonNode): Channel

    /**
     * Used to build an instance of [DmChannel]
     *
     * @param json the json
     * @return [DmChannel] the dm channel
     */
    fun buildDMChannel(json: JsonNode): DmChannel

    // Guild channel entities

    /**
     * Used to build an instance of [GuildChannel]
     *
     * @param json the json
     * @return [GuildChannel] the guild channel
     */
    fun buildGuildChannel(json: JsonNode): GuildChannel

    /**
     * Used to build an instance of [GuildCategory]
     *
     * @param json the json
     * @return [GuildCategory] the guild category
     */
    fun buildGuildCategory(json: JsonNode): GuildCategory

    /**
     * Used to build an instance of [GuildForumChannel]
     *
     * @param json the json
     * @return [GuildForumChannel] the guild forum channel
     */
    fun buildGuildForumChannel(json: JsonNode): GuildForumChannel

    /**
     * Used to build an instance of [GuildMessageChannel]
     *
     * @param json the json
     * @return [GuildMessageChannel] the guild message channel
     */
    fun buildGuildMessageChannel(json: JsonNode): GuildMessageChannel

    /**
     * Used to build an instance of [GuildNewsChannel]
     *
     * @param json the json
     * @return [GuildNewsChannel] the guild news channel
     */
    fun buildGuildNewsChannel(json: JsonNode): GuildNewsChannel

    /**
     * Used to build an instance of [GuildStageChannel]
     *
     * @param json the json
     * @return [GuildStageChannel] the guild stage channel
     */
    fun buildGuildStageChannel(json: JsonNode): GuildStageChannel

    /**
     * Used to build an instance of [GuildTextChannel]
     *
     * @param json the json
     * @return [GuildTextChannel] the guild text channel
     */
    fun buildGuildTextChannel(json: JsonNode): GuildTextChannel

    /**
     * Used to build an instance of [GuildVoiceChannel]
     *
     * @param json the json
     * @return [GuildVoiceChannel] the guild voice channel
     */
    fun buildGuildVoiceChannel(json: JsonNode): GuildVoiceChannel

    /**
     * Used to build an instance of [PermissionOverwrite]
     *
     * @param json the json
     * @return [PermissionOverwrite] the permission overwrite
     */
    fun buildPermissionOverwrite(json: JsonNode): PermissionOverwrite

    // Forum channel entities

    /**
     * Used to build an instance of [ForumTag]
     *
     * @param json the json
     * @return [ForumTag] the forum tag
     */
    fun buildForumTag(json: JsonNode): ForumTag

    /**
     * Used to build an instance of [DefaultReactionEmoji]
     *
     * @param json the json
     * @return [DefaultReactionEmoji] the default reaction emoji
     */
    fun buildDefaultReactionEmoji(json: JsonNode): DefaultReactionEmoji

    // AuditLog entities

    /**
     * Used to build an instance of [AuditLogEntry]
     *
     * @param json the json
     * @return [AuditLogEntry] the audit log entry
     */
    fun buildAuditLogEntry(json: JsonNode): AuditLogEntry

    /**
     * Used to build an instance of [AuditLogChange]
     *
     * @param json the json
     * @return [AuditLogChange] the audit log change
     */
    fun buildAuditLogChange(json: JsonNode): AuditLogChange

    // Application entities

    /**
     * Used to build an instance of [PartialApplication]
     *
     * @param json the json
     * @return [PartialApplication] the application command
     */
    fun buildPartialApplication(json: JsonNode): PartialApplication

    // Sticker entities

    /**
     * Used to build an instance of [StickerItem]
     *
     * @param json the json
     * @return [StickerItem] the sticker item
     */
    fun buildStickerItem(json: JsonNode): StickerItem

    // User entities

    /**
     * Used to build an instance of [Avatar]
     *
     * @param json the json
     * @return [Avatar] the avatar
     */
    fun buildAvatar(json: JsonNode): Avatar

    // Message entities

    /**
     * Used to build an instance of [MessageReference]
     *
     * @param json the json
     * @return [MessageReference] the message reference
     */
    fun buildMessageReference(json: JsonNode): MessageReference

    /**
     * Used to build an instance of [Reaction]
     *
     * @param json the json
     * @return [Reaction] the reaction
     */
    fun buildReaction(json: JsonNode): Reaction

    /**
     * Used to build an instance of [MessageInteraction]
     *
     * @param json the json
     * @return [MessageInteraction] the message interaction
     */
    fun buildMessageInteraction(json: JsonNode): MessageInteraction

    /**
     * Used to build an instance of [MessageActivity]
     *
     * @param json the json
     * @return [MessageActivity] the message activity
     */
    fun buildMessageActivity(json: JsonNode): MessageActivity

    /**
     * Used to build an instance of [Embed]
     *
     * @param json the json
     * @return [Embed] the embed
     */
    fun buildEmbed(json: JsonNode): Embed

    /**
     * Used to build an instance of [Attachment]
     *
     * @param json the json
     * @return [Attachment] the attachment
     */
    fun buildAttachment(json: JsonNode): Attachment

    // Embed entities

    /**
     * Used to build an instance of [Author]
     *
     * @param json the json
     * @return [Author] the author in the embed
     */
    fun buildAuthor(json: JsonNode): Author

    /**
     * Used to build an instance of [Field]
     *
     * @param json the json
     * @return [Field] the embed field
     */
    fun buildField(json: JsonNode): Field

    /**
     * Used to build an instance of [Footer]
     *
     * @param json the json
     * @return [Footer] the footer in the embed
     */
    fun buildFooter(json: JsonNode): Footer

    /**
     * Used to build an instance of [Image]
     *
     * @param json the json
     * @return [Image] the image in the embed
     */
    fun buildImage(json: JsonNode): Image

    /**
     * Used to build an instance of [Provider]
     *
     * @param json the json
     * @return [Provider] the provider in the embed
     */
    fun buildProvider(json: JsonNode): Provider

    /**
     * Used to build an instance of [Thumbnail]
     *
     * @param json the json
     * @return [Thumbnail] the thumbnail in the embed
     */
    fun buildThumbnail(json: JsonNode): Thumbnail

    /**
     * Used to build an instance of [Video]
     *
     * @param json the json
     * @return [Video] the video in the embed
     */
    fun buildVideo(json: JsonNode): Video
}
