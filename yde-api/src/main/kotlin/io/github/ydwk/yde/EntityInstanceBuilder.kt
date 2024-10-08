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
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.interaction.actionrow.ActionRow
import io.github.ydwk.yde.entities.interaction.button.Button
import io.github.ydwk.yde.entities.interaction.button.PartialEmoji
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenu
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuDefaultValues
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuOption
import io.github.ydwk.yde.entities.interaction.textinput.TextInput
import io.github.ydwk.yde.entities.message.*
import io.github.ydwk.yde.entities.message.embed.*
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommand
import io.github.ydwk.yde.interaction.application.ApplicationCommandOption
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.application.type.MessageCommand
import io.github.ydwk.yde.interaction.application.type.SlashCommand
import io.github.ydwk.yde.interaction.application.type.UserCommand
import io.github.ydwk.yde.interaction.message.ComponentInteractionData
import io.github.ydwk.yde.interaction.message.actionrow.ActionRowInteraction
import io.github.ydwk.yde.interaction.message.button.ButtonInteraction
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction
import io.github.ydwk.yde.interaction.message.selectmenu.interaction.type.*
import io.github.ydwk.yde.interaction.message.textinput.TextInputInteraction
import io.github.ydwk.yde.util.GetterSnowFlake
import java.net.URL

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
     * Used to build an instance of [PartialGuild]
     *
     * @param json the json
     * @return [PartialGuild] the partial guild
     */
    fun buildPartialGuild(json: JsonNode): PartialGuild

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
     * Used to build an instance of [MentionedChannel]
     *
     * @param json the json
     * @return [MentionedChannel] the mentioned channel
     */
    fun buildMentionedChannel(json: JsonNode): MentionedChannel

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
    suspend fun buildGuildScheduledEvent(json: JsonNode): GuildScheduledEvent

    /**
     * Used to build an instance of [Invite]
     *
     * @param json the json
     * @return [Invite] the invite
     */
    suspend fun buildInvite(json: JsonNode): Invite

    /**
     * Used to build an instance of [Member]
     *
     * @param json the json
     * @param guildId the guild id
     * @param backUpUser the back up user
     * @return [Member] the member
     */
    fun buildMember(json: JsonNode, guildId: GetterSnowFlake, backUpUser: User? = null): Member

    /**
     * Used to build an instance of [Member]
     *
     * @param json the json
     * @param guildId the guild id
     * @param backUpUser the back up user json
     * @return [Member] the member
     */
    fun buildMember(json: JsonNode, guildId: GetterSnowFlake, backUpUser: JsonNode): Member {
        return buildMember(json, guildId, buildUser(backUpUser))
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
     * @param isGuildChannel weather the channel is a guild channel
     * @param isDmChannel weather the channel is a dm channel
     * @return [Channel] the channel
     */
    fun buildChannel(json: JsonNode, isGuildChannel: Boolean, isDmChannel: Boolean): Channel

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
     * @param guildId the guild id
     * @return [EntityMetadata] the entity metadata
     */
    fun buildEntityMetadata(json: JsonNode, guildId: GetterSnowFlake): EntityMetadata

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
     * @param url the url
     * @return [Avatar] the avatar
     */
    fun buildAvatar(url: URL): Avatar

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

    /**
     * Used to build an instance of [PartialEmoji]
     *
     * @param json the json
     * @return [PartialEmoji] the partial emoji
     */
    fun buildPartialEmoji(json: JsonNode): PartialEmoji

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
     * @param interactionId the interaction id
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
    fun buildSelectMenu(json: JsonNode): SelectMenu

    /**
     * Used to build an instance of [Button]
     *
     * @param json the json
     * @param interactionId the interaction id
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

    /**
     * Used to build an instance of [SelectMenuOption]
     *
     * @param json the json
     * @return [SelectMenuOption] the string select menu option
     */
    fun buildSelectMenuOption(json: JsonNode): SelectMenuOption

    /**
     * Used to build an instance of [SelectMenuDefaultValues]
     *
     * @param json the json
     * @return [SelectMenuDefaultValues] the select menu default values
     */
    fun buildSelectMenuDefaultValues(json: JsonNode): SelectMenuDefaultValues

    // Interactions

    /**
     * Used to build an instance of [ActionRowInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [ActionRowInteraction] the action row interaction
     */
    fun buildActionRowInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ActionRowInteraction

    /**
     * Used to build an instance of [SelectMenuInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [SelectMenuInteraction] the select menu interaction
     */
    fun buildSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): SelectMenuInteraction

    /**
     * Used to build an instance of [ButtonInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [ButtonInteraction] the button interaction
     */
    fun buildButtonInteraction(json: JsonNode, interactionId: GetterSnowFlake): ButtonInteraction

    // Select Menu Interactions

    /**
     * Used to build an instance of [UserSelectMenuInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [UserSelectMenuInteraction] the user select menu interaction
     */
    fun buildUserSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): UserSelectMenuInteraction

    /**
     * Used to build an instance of [StringSelectMenuInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [StringSelectMenuInteraction] the string select menu interaction
     */
    fun buildStringSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): StringSelectMenuInteraction

    /**
     * Used to build an instance of [RoleSelectMenuInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [RoleSelectMenuInteraction] the role select menu interaction
     */
    fun buildRoleSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): RoleSelectMenuInteraction

    /**
     * Used to build an instance of [MemberSelectMenuInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [MemberSelectMenuInteraction] the member select menu interaction
     */
    fun buildMemberSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): MemberSelectMenuInteraction

    /**
     * Used to build an instance of [ChannelSelectMenuInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [ChannelSelectMenuInteraction] the channel select menu interaction
     */
    fun buildChannelSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ChannelSelectMenuInteraction

    /**
     * Used to build an instance of [TextInputInteraction]
     *
     * @param json the json
     * @param interactionId the interaction id
     * @return [TextInputInteraction] the text input interaction
     */
    fun buildTextInputInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): TextInputInteraction

    // Application Command entities

    /**
     * Used to build an instance of [ApplicationCommand]
     *
     * @param json the json
     * @param type the application command type
     * @param interaction the interaction
     * @return [ApplicationCommand] the application command
     */
    fun buildApplicationCommand(
        json: JsonNode,
        type: ApplicationCommandType,
        interaction: Interaction
    ): ApplicationCommand

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
