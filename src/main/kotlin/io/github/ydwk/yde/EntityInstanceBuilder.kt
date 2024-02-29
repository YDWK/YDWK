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
import io.github.ydwk.yde.entities.guild.schedule.EntityMetadata
import io.github.ydwk.yde.entities.guild.ws.WelcomeChannel
import io.github.ydwk.yde.entities.message.*
import io.github.ydwk.yde.entities.message.embed.*
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommand
import io.github.ydwk.yde.interaction.application.ApplicationCommandOption
import io.github.ydwk.yde.interaction.application.type.MessageCommand
import io.github.ydwk.yde.interaction.application.type.SlashCommand
import io.github.ydwk.yde.interaction.application.type.UserCommand
import io.github.ydwk.yde.interaction.message.ActionRow
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.interaction.message.ComponentInteractionData
import io.github.ydwk.yde.interaction.message.button.Button
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.yde.interaction.message.selectmenu.types.*
import io.github.ydwk.yde.interaction.message.selectmenu.types.string.StringSelectMenuOption
import io.github.ydwk.yde.interaction.message.textinput.TextInput
import io.github.ydwk.yde.util.GetterSnowFlake

/** The [EntityInstanceBuilder] is used to build instances of entities. */
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
     * Used to build an instance of [VoiceState.VoiceRegion]
     *
     * @param json the json
     * @return [VoiceState.VoiceRegion] the voice region
     */
    fun buildVoiceRegion(json: JsonNode): VoiceState.VoiceRegion

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
    fun buildMember(json: JsonNode, guild: Guild, backUpUser: JsonNode): Member {
        return buildMember(json, guild, buildUser(backUpUser))
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

    /**
     * Used to build an instance of [EntityMetadata]
     *
     * @param json the json
     * @return [EntityMetadata] the entity metadata
     */
    fun buildEntityMetadata(json: JsonNode): EntityMetadata

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

    /**
     * Used to build an instance of [Emoji]
     *
     * @param json the json
     * @return [Emoji] the emoji
     */
    fun buildEmoji(json: JsonNode): Emoji

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

    // Interaction entities

    /**
     * Used to build an instance of [Interaction]
     *
     * @param json the json
     * @return [Interaction] the interaction
     */
    fun buildInteraction(json: JsonNode): Interaction

    /**
     * Used to build an instance of [ComponentInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [ComponentInteraction] the component interaction
     */
    fun buildComponentInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ComponentInteraction

    // Message Interaction entities

    /**
     * Used to build an instance of [Component]
     *
     * @param json the json
     * @return [Component] the message component
     */
    fun buildComponent(json: JsonNode): Component

    /**
     * Used to build an instance of [ComponentInteractionData]
     *
     * @param json the json
     * @return [ComponentInteractionData] the component interaction data
     */
    fun buildComponentInteractionData(json: JsonNode): ComponentInteractionData

    /**
     * Used to build an instance of [ComponentInteractionData.SelectOptionValue]
     *
     * @param json the json
     * @return [ComponentInteractionData.SelectOptionValue] the select option value
     */
    fun buildSelectOptionValue(json: JsonNode): ComponentInteractionData.SelectOptionValue

    /**
     * Used to build an instance of [TextInput]
     *
     * @param json the json
     * @return [TextInput] the text input
     */
    fun buildTextInput(json: JsonNode): TextInput

    /**
     * Used to build an instance of [SelectMenu]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [SelectMenu] the select menu
     */
    fun buildSelectMenu(json: JsonNode, interactionId: GetterSnowFlake): SelectMenu

    /**
     * Used to build an instance of [Button]
     *
     * @param json the json
     * @return [Button] the button
     */
    fun buildButton(json: JsonNode): Button

    /**
     * Used to build an instance of [ActionRow]
     *
     * @param json the json
     * @return [ActionRow] the action row
     */
    fun buildActionRow(json: JsonNode): ActionRow

    // SelectMenu entities

    /**
     * Used to build an instance of [UserSelectMenu]
     *
     * @param json the json
     * @return [UserSelectMenu] the user select menu
     */
    fun buildUserSelectMenu(json: JsonNode): UserSelectMenu

    /**
     * Used to build an instance of [StringSelectMenu]
     *
     * @param json the json
     * @return [StringSelectMenu] the string select menu
     */
    fun buildStringSelectMenu(json: JsonNode): StringSelectMenu

    /**
     * Used to build an instance of [RoleSelectMenu]
     *
     * @param json the json
     * @return [RoleSelectMenu] the role select menu
     */
    fun buildRoleSelectMenu(json: JsonNode): RoleSelectMenu

    /**
     * Used to build an instance of [MemberSelectMenu]
     *
     * @param json the json
     * @return [MemberSelectMenu] the member select menu
     */
    fun buildMemberSelectMenu(json: JsonNode): MemberSelectMenu

    /**
     * Used to build an instance of [ChannelSelectMenu]
     *
     * @param json the json
     * @return [ChannelSelectMenu] the channel select menu
     */
    fun buildChannelSelectMenu(json: JsonNode): ChannelSelectMenu

    /**
     * Used to build an instance of [StringSelectMenuOption]
     *
     * @param json the json
     * @return [StringSelectMenuOption] the string select menu option
     */
    fun buildStringSelectMenuOption(json: JsonNode): StringSelectMenuOption

    // Application Command entities

    /**
     * Used to build an instance of [ApplicationCommand]
     *
     * @param json the json
     * @return [ApplicationCommand] the application command
     */
    fun buildApplicationCommand(json: JsonNode): ApplicationCommand

    /**
     * Used to build an instance of [ApplicationCommandOption]
     *
     * @param json the json
     * @return [ApplicationCommandOption] the application command option
     */
    fun buildApplicationCommandOption(json: JsonNode): ApplicationCommandOption

    // Application Command Types

    /**
     * Used to build an instance of [UserCommand]
     *
     * @param json the json
     * @param interaction the interaction
     * @return [UserCommand] the user command
     */
    fun buildUserCommand(json: JsonNode, interaction: Interaction? = null): UserCommand

    /**
     * Used to build an instance of [SlashCommand]
     *
     * @param json the json
     * @param interaction the interaction
     * @return [SlashCommand] the slash command
     */
    fun buildSlashCommand(json: JsonNode, interaction: Interaction? = null): SlashCommand

    /**
     * Used to build an instance of [MessageCommand]
     *
     * @param json the json
     * @param interaction the interaction
     * @return [MessageCommand] the message command
     */
    fun buildMessageCommand(json: JsonNode, interaction: Interaction? = null): MessageCommand
}
