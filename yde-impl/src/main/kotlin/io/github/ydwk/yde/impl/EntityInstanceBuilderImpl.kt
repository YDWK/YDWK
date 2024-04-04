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
package io.github.ydwk.yde.impl

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.EntityInstanceBuilder
import io.github.ydwk.yde.entities.*
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.entities.audit.AuditLogChange
import io.github.ydwk.yde.entities.audit.AuditLogEntry
import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.entities.channel.guild.forum.*
import io.github.ydwk.yde.entities.channel.guild.message.GuildMessageChannel
import io.github.ydwk.yde.entities.channel.guild.message.news.GuildNewsChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.GuildTextChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.entities.channel.guild.vc.GuildStageChannel
import io.github.ydwk.yde.entities.channel.guild.vc.GuildVoiceChannel
import io.github.ydwk.yde.entities.guild.*
import io.github.ydwk.yde.entities.guild.enums.*
import io.github.ydwk.yde.entities.guild.invite.InviteCreator
import io.github.ydwk.yde.entities.guild.invite.TargetType
import io.github.ydwk.yde.entities.guild.role.RoleTag
import io.github.ydwk.yde.entities.guild.schedule.EntityMetadata
import io.github.ydwk.yde.entities.guild.schedule.EntityType
import io.github.ydwk.yde.entities.guild.schedule.PrivacyLevel
import io.github.ydwk.yde.entities.guild.schedule.ScheduledEventStatus
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
import io.github.ydwk.yde.entities.message.activity.MessageActivityType
import io.github.ydwk.yde.entities.message.embed.*
import io.github.ydwk.yde.entities.sticker.StickerFormatType
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.entities.sticker.StickerType
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.entities.*
import io.github.ydwk.yde.impl.entities.application.PartialApplicationImpl
import io.github.ydwk.yde.impl.entities.audit.AuditLogChangeImpl
import io.github.ydwk.yde.impl.entities.audit.AuditLogEntryImpl
import io.github.ydwk.yde.impl.entities.channel.DmChannelImpl
import io.github.ydwk.yde.impl.entities.channel.getter.ChannelGetterImpl
import io.github.ydwk.yde.impl.entities.channel.getter.guild.GuildChannelGetterImpl
import io.github.ydwk.yde.impl.entities.channel.getter.guild.GuildMessageChannelGetterImpl
import io.github.ydwk.yde.impl.entities.channel.guild.*
import io.github.ydwk.yde.impl.entities.channel.guild.GuildCategoryImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildForumChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.GuildMessageChannelImpl
import io.github.ydwk.yde.impl.entities.channel.guild.forum.DefaultReactionEmojiImpl
import io.github.ydwk.yde.impl.entities.channel.guild.forum.ForumTagImpl
import io.github.ydwk.yde.impl.entities.guild.*
import io.github.ydwk.yde.impl.entities.guild.role.RoleTagImpl
import io.github.ydwk.yde.impl.entities.guild.schedule.EntityMetadataImpl
import io.github.ydwk.yde.impl.entities.guild.ws.WelcomeChannelImpl
import io.github.ydwk.yde.impl.entities.interaction.ComponentImpl
import io.github.ydwk.yde.impl.entities.interaction.actionrow.ActionRowImpl
import io.github.ydwk.yde.impl.entities.interaction.selectmenu.SelectMenuDefaultValuesImpl
import io.github.ydwk.yde.impl.entities.interaction.selectmenu.SelectMenuOptionImpl
import io.github.ydwk.yde.impl.entities.interaction.textinput.TextInputImpl
import io.github.ydwk.yde.impl.entities.message.*
import io.github.ydwk.yde.impl.entities.message.AttachmentImpl
import io.github.ydwk.yde.impl.entities.message.EmbedImpl
import io.github.ydwk.yde.impl.entities.message.MessageInteractionImpl
import io.github.ydwk.yde.impl.entities.message.MessageReferenceImpl
import io.github.ydwk.yde.impl.entities.message.ReactionImpl
import io.github.ydwk.yde.impl.entities.message.embed.*
import io.github.ydwk.yde.impl.entities.message.embed.AuthorImpl
import io.github.ydwk.yde.impl.entities.message.embed.FieldImpl
import io.github.ydwk.yde.impl.entities.message.embed.FooterImpl
import io.github.ydwk.yde.impl.entities.message.embed.ImageImpl
import io.github.ydwk.yde.impl.entities.message.embed.ProviderImpl
import io.github.ydwk.yde.impl.entities.sticker.StickerItemImpl
import io.github.ydwk.yde.impl.entities.user.AvatarImpl
import io.github.ydwk.yde.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.yde.impl.interaction.InteractionImpl
import io.github.ydwk.yde.impl.interaction.application.type.MessageCommandImpl
import io.github.ydwk.yde.impl.interaction.application.type.SlashCommandImpl
import io.github.ydwk.yde.impl.interaction.application.type.UserCommandImpl
import io.github.ydwk.yde.impl.interaction.message.ComponentInteractionDataImpl
import io.github.ydwk.yde.impl.interaction.message.actionrow.ActionRowInteractionImpl
import io.github.ydwk.yde.impl.util.*
import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.Interaction
import io.github.ydwk.yde.interaction.application.ApplicationCommand
import io.github.ydwk.yde.interaction.application.ApplicationCommandOption
import io.github.ydwk.yde.interaction.application.type.MessageCommand
import io.github.ydwk.yde.interaction.application.type.SlashCommand
import io.github.ydwk.yde.interaction.application.type.UserCommand
import io.github.ydwk.yde.interaction.message.ComponentInteractionData
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.message.actionrow.ActionRowInteraction
import io.github.ydwk.yde.interaction.message.button.ButtonInteraction
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction
import io.github.ydwk.yde.interaction.message.selectmenu.interaction.type.*
import io.github.ydwk.yde.interaction.message.textinput.TextInputInteraction
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.rest.error.RestAPIException
import io.github.ydwk.yde.util.*
import java.awt.Color
import java.net.URL

// TODO: Where it says yde.getGuildById remove this and just give the id. The user should be able to
// get the guild by id themselves
// TODO: Check every entity whith the discord documentation to see if there are any missing fields
// or any corrections that need to be made
// TODO: Rewrite the hall interaction system
// TODO: For YDWK rewrite the voice system with the new voice system
// TODO: Add support for locale
// TODO: When having a list of enums make sure to use EnumSet instead of a list

/** Used to build entities */
class EntityInstanceBuilderImpl(val yde: YDEImpl) : EntityInstanceBuilder {

    override fun buildUser(json: JsonNode): User {
        val id = json["id"].asLong()
        val avatarHash = if (json.hasNonNull("avatar")) json["avatar"].asText() else null

        // Deprecated
        val discriminator = json["discriminator"].asText()

        return UserImpl(
            json,
            id,
            yde,
            json["global_name"].asText(),
            avatarHash,
            getAvatar(yde, discriminator, avatarHash, null, id),
            json["avatar"].isNull,
            json["bot"].asBoolean(),
            if (json.hasNonNull("system")) json["system"].asBoolean() else null,
            if (json.hasNonNull("mfa_enabled")) json["mfa_enabled"].asBoolean() else null,
            if (json.hasNonNull("banner")) json["banner"].asText() else null,
            if (json.hasNonNull("accent_color")) Color(json["accent_color"].asInt()) else null,
            if (json.hasNonNull("locale")) json["locale"].asText() else null,
            if (json.hasNonNull("verified")) json["verified"].asBoolean() else null,
            if (json.hasNonNull("flags")) json["flags"].asInt() else null,
            if (json.hasNonNull("public_flags")) json["public_flags"].asInt() else null,
            json["username"].asText())
    }

    override fun buildBot(json: JsonNode): Bot {
        return BotImpl(buildUser(json), json["email"].asText())
    }

    override fun buildGuild(json: JsonNode): Guild {
        val voiceStates =
            if (json.hasNonNull("voice_states")) json["voice_states"].map { buildVoiceState(it) }
            else emptyList()

        if (voiceStates.isNotEmpty()) {
            for (vc in voiceStates) {
                val member = vc.member
                if (member != null) {
                    member.voiceState = vc
                }
            }
        }

        val id = json["id"].asLong()

        return GuildImpl(
            yde,
            json,
            id,
            if (json.has("icon")) json["icon"].asText() else null,
            if (json.has("splash")) json["splash"].asText() else null,
            if (json.has("discovery_splash")) json["discovery_splash"].asText() else null,
            if (json.hasNonNull("owner")) json["owner"].asBoolean() else null,
            GetterSnowFlake.of(json["owner_id"].asLong()),
            if (json.has("permissions")) json["permissions"].asText() else null,
            if (json.has("afk_channel_id")) GetterSnowFlake.of(json["afk_channel_id"].asLong())
            else null,
            json["afk_timeout"].asInt(),
            if (json.has("widget_enabled")) json["widget_enabled"].asBoolean() else null,
            if (json.has("widget_channel_id"))
                GetterSnowFlake.of(json["widget_channel_id"].asLong())
            else null,
            VerificationLevel.getValue(json["verification_level"].asInt()),
            MessageNotificationLevel.getValue(json["default_message_notifications"].asInt()),
            ExplicitContentFilterLevel.getValue(json["explicit_content_filter"].asInt()),
            json["roles"].map { buildRole(it) },
            json["emojis"].map { buildEmoji(it) },
            json["features"].map { GuildFeature.getValue(it.asText()) }.toSet(),
            MFALevel.getValue(json["mfa_level"].asInt()),
            if (json.has("application_id")) GetterSnowFlake.of(json["application_id"].asLong())
            else null,
            if (json.has("system_channel_id"))
                GetterSnowFlake.of(json["system_channel_id"].asLong())
            else null,
            SystemChannelFlag.getValue(json["system_channel_flags"].asInt()),
            if (json.has("rules_channel_id")) GetterSnowFlake.of(json["rules_channel_id"].asLong())
            else null,
            if (json.has("max_presences")) json["max_presences"].asInt() else null,
            json["max_members"].asInt(),
            if (json.has("vanity_url_code")) json["vanity_url_code"].asText() else null,
            if (json.has("description")) json["description"].asText() else null,
            if (json.has("banner")) json["banner"].asText() else null,
            PremiumTier.getValue(json["premium_tier"].asInt()),
            json["premium_subscription_count"].asInt(),
            json["preferred_locale"].asText(),
            if (json.has("public_updates_channel_id"))
                GetterSnowFlake.of(json["public_updates_channel_id"].asLong())
            else null,
            if (json.has("max_video_channel_users")) json["max_video_channel_users"].asInt()
            else null,
            if (json.has("approximate_member_count")) json["approximate_member_count"].asInt()
            else null,
            if (json.has("approximate_presence_count")) json["approximate_presence_count"].asInt()
            else null,
            if (json.has("welcome_screen")) buildWelcomeScreen(json["welcome_screen"]) else null,
            NSFWLeveL.getValue(json["nsfw_level"].asInt()),
            json["stickers"].map { buildSticker(it) },
            json["premium_progress_bar_enabled"].asBoolean(),
            voiceStates,
            yde.getGuildChannels().filter { it.idAsLong == id },
            json["name"].asText())
    }

    override fun buildMessage(json: JsonNode): Message {
        val channels = mutableListOf<Channel>()

        json["mention_channels"].map {
            val channelType = ChannelType.getValue(it["type"].asInt())
            if (ChannelType.isGuildChannel(channelType)) {
                channels.add(buildGuildChannel(it))
            } else {
                channels.add(buildDMChannel(it))
            }
        }

        val thread: Channel? =
            if (json.has("thread")) {
                val newThreadJson = json.get("thread")
                val channelType = ChannelType.getValue(newThreadJson["type"].asInt())
                if (ChannelType.isGuildChannel(channelType)) {
                    buildGuildChannel(newThreadJson)
                } else {
                    buildDMChannel(newThreadJson)
                }
            } else null

        return MessageImpl(
            yde,
            json,
            json["id"].asLong(),
            yde.getChannelById(json["channel_id"].asLong())
                ?: throw IllegalStateException("Channel is null"),
            buildUser(json["author"]),
            json["content"].asText(),
            formatZonedDateTime(json["timestamp"].asText()),
            if (json.has("edited_timestamp")) formatZonedDateTime(json["edited_timestamp"].asText())
            else null,
            json["tts"].asBoolean(),
            json["mention_everyone"].asBoolean(),
            json["mentions"].map { buildUser(it) },
            json["mention_roles"].map { buildRole(it) },
            channels,
            json["attachments"].map { buildAttachment(it) },
            json["embeds"].map { buildEmbed(it) },
            json["reactions"].map { buildReaction(it) },
            if (json.has("nonce")) json["nonce"].asText() else null,
            json["pinned"].asBoolean(),
            if (json.has("webhook_id")) GetterSnowFlake.of(json["webhook_id"].asLong()) else null,
            MessageType.getValue(json["type"].asInt()),
            if (json.has("activity")) buildMessageActivity(json["activity"]) else null,
            if (json.has("application")) buildPartialApplication(json["application"]) else null,
            if (json.has("message_reference")) buildMessageReference(json["message_reference"])
            else null,
            if (json.has("flags")) MessageFlag.getValue(json["flags"].asLong()) else null,
            if (json.has("referenced_message")) buildMessage(json["referenced_message"]) else null,
            if (json.has("interaction")) buildMessageInteraction(json["interaction"]) else null,
            thread,
            json["components"].map { buildComponent(it) },
            json["sticker_items"].map { buildStickerItem(it) },
            if (json.has("position")) json["position"].asLong() else null,
        )
    }

    override fun buildSticker(json: JsonNode): Sticker {
        return StickerImpl(
            yde,
            json,
            json["id"].asLong(),
            if (json.has("pack_id")) GetterSnowFlake.of(json["pack_id"].asLong()) else null,
            if (json.has("description")) json["description"].asText() else null,
            if (json.has("tags")) json["tags"].map { it.asText() } else emptyList(),
            StickerType.getValue(json["type"].asInt()),
            StickerFormatType.getValue(json["format_type"].asInt()),
            json["available"].asBoolean(),
            if (json.has("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null,
            if (json.has("user")) buildUser(json["user"]) else null,
            if (json.has("sort_value")) json["sort_value"].asInt() else null,
            json["name"].asText())
    }

    override fun buildUnavailableGuild(json: JsonNode): UnavailableGuild {
        return UnavailableGuildImpl(yde, json, json["id"].asLong(), json["unavailable"].asBoolean())
    }

    override fun buildVoiceState(json: JsonNode, backUpGuild: Guild?): VoiceState {

        val channel: GuildVoiceChannel? =
            if (json.hasNonNull("channel_id") || json.get("channel_id").asText() != "null") {
                if (yde.getGuildChannelGetterById(json["channel_id"].asText()) != null)
                    yde.getGuildChannelGetterById(json["channel_id"].asText())!!
                        .asGuildVoiceChannel()
                else null
            } else null
        val guild =
            if (yde.getGuildById(json["guild_id"].asLong()) != null)
                yde.getGuildById(json["guild_id"].asLong())
            else backUpGuild
        val user = yde.getUserById(json["user_id"].asLong())

        return VoiceStateImpl(
            yde,
            json,
            backUpGuild,
            guild,
            channel,
            user,
            if (json.has("member")) buildMember(json["member"], guild!!, user) else null,
            json["session_id"].asText(),
            json["deaf"].asBoolean(),
            json["mute"].asBoolean(),
            json["self_deaf"].asBoolean(),
            json["self_mute"].asBoolean(),
            json["self_stream"].asBoolean(),
            json["suppress"].asBoolean(),
            if (json.has("request_to_speak_timestamp")) json["request_to_speak_timestamp"].asText()
            else null)
    }

    override fun buildVoiceRegion(json: JsonNode): VoiceState.VoiceRegion {
        return VoiceStateImpl.VoiceRegionImpl(
            yde,
            json,
            json["id"].asLong(),
            json["optimal"].asBoolean(),
            json["deprecated"].asBoolean(),
            json["custom"].asBoolean(),
            json["name"].asText())
    }

    override fun buildAuditLog(json: JsonNode): AuditLog {
        return AuditLogImpl(yde, json, emptyList(), json["entries"].map { buildAuditLogEntry(it) })
    }

    override fun buildApplication(json: JsonNode): Application {
        return ApplicationImpl(
            json,
            json["id"].asLong(),
            yde,
            if (json.has("icon")) json["icon"].asText() else null,
            json["description"].asText(),
            if (json.has("rpc_origins")) json["rpc_origins"].asText().split(",").toTypedArray()
            else null,
            json["bot_public"].asBoolean(),
            json["bot_require_code_grant"].asBoolean(),
            if (json.has("terms_of_service_url"))
                @Suppress("DEPRECATION") URL(json["terms_of_service_url"].asText())
            else null,
            if (json.has("privacy_policy_url"))
                @Suppress("DEPRECATION") URL(json["privacy_policy_url"].asText())
            else null,
            if (json.has("owner")) buildUser(json["owner"]) else null,
            if (json.has("verify_key")) json["verify_key"].asText() else null,
            if (json.has("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null,
            if (json.has("game_sdk_id")) GetterSnowFlake.of(json["game_sdk_id"].asLong()) else null,
            if (json.has("slug")) json["slug"].asText() else null,
            if (json.has("cover_image")) json["cover_image"].asText() else null,
            if (json.has("flags")) json["flags"].asInt() else null,
            if (json.has("tags")) json["tags"].asText().split(",").toTypedArray() else null,
            json["name"].asText())
    }

    override fun buildBan(json: JsonNode): Ban {
        return BanImpl(
            yde,
            json,
            if (json.has("reason")) json["reason"].asText() else null,
            buildUser(json["user"]))
    }

    override suspend fun buildGuildScheduledEvent(json: JsonNode): GuildScheduledEvent {

        val guild =
            yde.getGuildById(json["guild_id"].asLong())
                ?: yde.requestGuild(json["guild_id"].asLong()).getOrNull()
                    ?: throw IllegalStateException("Guild is null")

        return GuildScheduledEventImpl(
            yde,
            json,
            json["id"].asLong(),
            guild,
            if (json.has("channel_id")) yde.getGuildChannelById(json["channel_id"].asLong())
            else null,
            if (json.has("creator")) buildUser(json["creator"]) else null,
            if (json.has("description")) json["description"].asText() else null,
            formatZonedDateTime(json["scheduled_start"].asText()),
            if (json.has("scheduled_end_time"))
                formatZonedDateTime(json["scheduled_end_time"].asText())
            else null,
            PrivacyLevel.getValue(json["privacy_level"].asInt()),
            ScheduledEventStatus.getValue(json["status"].asInt()),
            EntityType.getValue(json["entity_type"].asInt()),
            if (json.has("entity_id")) GetterSnowFlake.of(json["entity_id"].asLong()) else null,
            if (json.has("entity_metadata")) buildEntityMetadata(json["entity_metadata"], guild)
            else null,
            if (json.has("user_count")) json["user_count"].asInt() else 0,
            if (json.has("image")) json["image"].asText() else null,
            json["name"].asText())
    }

    override suspend fun buildInvite(json: JsonNode): Invite {
        return InviteImpl(
            yde,
            json,
            json["code"].asText(),
            yde.getGuildById(json["guild_id"].asLong())
                ?: yde.requestGuild(json["guild_id"].asLong()).getOrNull()
                    ?: throw RestAPIException("Guild is null"),
            yde.getGuildChannelById(json["channel_id"].asLong())
                ?: throw IllegalStateException("Channel is null"),
            if (json.has("inviter")) buildUser(json["inviter"]) else null,
            TargetType.getValue(json["target_type"].asInt()),
            if (json.has("target_user")) buildUser(json["target_user"]) else null,
            if (json.has("target_application")) buildApplication(json["target_application"])
            else null,
            json["approximate_presence_count"].asInt(),
            json["approximate_member_count"].asInt(),
            formatZonedDateTime(json["expires_at"].asText()),
            if (json.has("scheduled_event")) buildGuildScheduledEvent(json["scheduled_event"])
            else throw IllegalStateException("Scheduled event is null"))
    }

    override fun buildMember(json: JsonNode, guild: Guild, backUpUser: User?): Member {
        val roleIds = json["roles"].map { GetterSnowFlake.of(it.asLong()) }

        val roles =
            roleIds.map {
                if (guild.getRoleById(it.asLong) != null) guild.getRoleById(it.asLong) else null
            }

        val user = if (json.has("user")) buildUser(json["user"]) else backUpUser

        val isOwner = guild.ownerId.asString == user?.id

        val isTimedOut =
            if (json.has("communication_disabled_until"))
                json["communication_disabled_until"].asBoolean()
            else null

        val guildAvatarHash = if (json.has("avatar")) json["avatar"].asText() else null

        val nickName = if (json.has("nick")) json["nick"].asText() else null

        return MemberImpl(
            yde,
            json,
            guild,
            GuildPermission.getValues(getPermissions(guild, isOwner, roles, isTimedOut ?: false)),
            user ?: throw IllegalStateException("User is null"),
            nickName,
            guildAvatarHash,
            getGuildAvatar(1024, guildAvatarHash, guild, user),
            roleIds,
            roles,
            if (json.has("joined_at")) formatZonedDateTime(json["joined_at"].asText()) else null,
            if (json.has("premium_since")) formatZonedDateTime(json["premium_since"].asText())
            else null,
            if (json.has("deaf")) json["deaf"].asBoolean() else false,
            if (json.has("mute")) json["mute"].asBoolean() else false,
            json["pending"].asBoolean(),
            if (json.has("communication_disabled_until"))
                formatZonedDateTime(json["communication_disabled_until"].asText())
            else null,
            isOwner,
            null,
            nickName ?: user.name)
    }

    override fun buildRole(json: JsonNode): Role {
        return RoleImpl(
            yde,
            json,
            json["id"].asLong(),
            GuildPermission.getValues(json["permissions"].asLong()),
            Color(json["color"].asInt()),
            json["hoist"].asBoolean(),
            if (json.has("icon")) json["icon"].asText() else null,
            if (json.has("unicode_emoji")) json["unicode_emoji"].asText() else null,
            json["position"].asInt(),
            json["managed"].asBoolean(),
            json["mentionable"].asBoolean(),
            if (json.has("tags")) buildRoleTag(json["tags"]) else null,
            json["permissions"].asLong(),
            json["name"].asText())
    }

    override fun buildWelcomeScreen(json: JsonNode): WelcomeScreen {
        return WelcomeScreenImpl(
            yde,
            json,
            if (json.has("description")) json["description"].asText() else null,
            json["welcome_channels"].map { buildWelcomeScreenChannel(it) })
    }

    override fun buildWelcomeScreenChannel(json: JsonNode): WelcomeChannel {
        return WelcomeChannelImpl(
            yde,
            json,
            GetterSnowFlake.of(json["channel_id"].asLong()),
            json["description"].asText(),
            if (json.has("emoji_id")) GetterSnowFlake.of(json["emoji_id"].asLong()) else null,
            if (json.has("emoji_name")) json["emoji_name"].asText() else null)
    }

    override fun buildRoleTag(json: JsonNode): RoleTag {
        return RoleTagImpl(
            yde,
            json,
            if (json.has("bot_id")) GetterSnowFlake.of(json["bot_id"].asLong()) else null,
            if (json.has("integration_id")) GetterSnowFlake.of(json["integration_id"].asLong())
            else null)
    }

    override fun buildChannel(
        json: JsonNode,
        isGuildChannel: Boolean,
        isDmChannel: Boolean
    ): Channel {
        val id = json["id"].asLong()
        return ChannelImpl(
            yde,
            json,
            id,
            isGuildChannel,
            isDmChannel,
            ChannelType.getValue(json["type"].asInt()),
            ChannelGetterImpl(yde, json, id, isGuildChannel, isDmChannel))
    }

    override fun buildDMChannel(json: JsonNode): DmChannel {
        return DmChannelImpl(
            yde,
            json,
            json["id"].asLong(),
            if (json.has("last_message_id")) GetterSnowFlake.of(json["last_message_id"].asLong())
            else null,
            if (json.has("recipients")) json["recipients"].map { buildUser(it) } else emptyList(),
            json["name"].asText())
    }

    override fun buildEntityMetadata(json: JsonNode, guild: Guild): EntityMetadata {
        return EntityMetadataImpl(
            yde,
            json,
            GetterSnowFlake.of(json["scheduled_event_id"].asLong()),
            buildUser(json["user"]),
            if (json.has("member")) buildMember(json["member"], guild, buildUser(json["user"]))
            else null)
    }

    override fun buildGuildChannel(json: JsonNode): GuildChannel {
        return GuildChannelImpl(
            yde,
            json,
            json["id"].asLong(),
            yde.getGuildById(json["guild_id"].asLong())!!,
            json["position"].asInt(),
            if (json.has("parent_id"))
                yde.getGuildChannelById(json["parent_id"].asLong())
                    ?.guildChannelGetter
                    ?.asGuildCategory()
            else null,
            GuildChannelGetterImpl(yde, json),
            InviteCreator(yde, json["id"].asText()),
            json["name"].asText())
    }

    override fun buildGuildCategory(json: JsonNode): GuildCategory {
        return GuildCategoryImpl(
            yde,
            json,
            json["id"].asLong(),
            yde.getGuildChannels().filter { it.idAsLong == json["id"].asLong() },
            if (json.has("nsfw")) json["nsfw"].asBoolean() else false)
    }

    override fun buildGuildForumChannel(json: JsonNode): GuildForumChannel {
        return GuildForumChannelImpl(
            yde,
            json,
            json["id"].asLong(),
            if (json.has("topic")) json["topic"].asText() else null,
            if (json.has("template")) json["template"].asText() else null,
            if (json.has("rate_limit_per_user")) json["rate_limit_per_user"].asInt() else 0,
            if (json.has("permission_overwrites"))
                json["permission_overwrites"].map { buildPermissionOverwrite(it) }
            else emptyList(),
            json["nsfw"].asBoolean(),
            if (json.has("last_message_id")) GetterSnowFlake.of(json["last_message_id"].asLong())
            else null,
            ChannelFlag.getValue(json["flags"].asLong()),
            json["default_rate_limit_per_user"].asInt(),
            if (json.has("default_sort_order")) json["default_sort_order"].asInt() else null,
            if (json.has("default_reaction_emoji"))
                buildDefaultReactionEmoji(json["default_reaction_emoji"])
            else null,
            if (json.has("available_forum_tags"))
                json["available_forum_tags"].map { buildForumTag(it) }
            else emptyList(),
            if (json.has("available_forum_tags"))
                json["available_forum_tags"].map { GetterSnowFlake.of(it["id"].asLong()) }
            else emptyList(),
            ForumLayoutType.getValue(json["layout_type"].asInt()))
    }

    override fun buildGuildMessageChannel(json: JsonNode): GuildMessageChannel {
        return GuildMessageChannelImpl(
            yde,
            json,
            json["id"].asLong(),
            json["topic"].asText(),
            json["nsfw"].asBoolean(),
            if (json.has("default_auto_archive_duration"))
                json["default_auto_archive_duration"].asInt()
            else 0,
            json["last_message_id"].asText(),
            json["last_pin_timestamp"].asText(),
            json["permission_overwrites"].map { buildPermissionOverwrite(it) },
            GuildMessageChannelGetterImpl(yde, json, json["id"].asLong()),
            if (json["type"].asInt() == 0) ChannelType.TEXT else ChannelType.NEWS)
    }

    override fun buildGuildNewsChannel(json: JsonNode): GuildNewsChannel {
        return GuildNewsChannelImpl(yde, json, json["id"].asLong())
    }

    override fun buildGuildStageChannel(json: JsonNode): GuildStageChannel {
        return GuildStageChannelImpl(
            yde, json, json["id"].asLong(), if (json.has("topic")) json["topic"].asText() else null)
    }

    override fun buildGuildTextChannel(json: JsonNode): GuildTextChannel {
        return GuildTextChannelImpl(
            yde, json, json["id"].asLong(), json["rate_limit_per_user"].asInt())
    }

    override fun buildGuildVoiceChannel(json: JsonNode): GuildVoiceChannel {
        return GuildVoiceChannelImpl(
            yde,
            json,
            json["id"].asLong(),
            json["bitrate"].asInt(),
            json["user_limit"].asInt(),
            json["rate_limit_per_user"].asInt())
    }

    override fun buildPermissionOverwrite(json: JsonNode): PermissionOverwrite {
        return PermissionOverwriteImpl(
            yde,
            json,
            json["id"].asLong(),
            json["type"].asInt(),
            json["allow"].asText(),
            json["deny"].asText())
    }

    override fun buildForumTag(json: JsonNode): ForumTag {
        return ForumTagImpl(
            yde,
            json,
            json["id"].asLong(),
            json["moderated"].asBoolean(),
            if (json.has("emoji_id")) GetterSnowFlake.of(json["emoji_id"].asLong()) else null,
            if (json.has("emoji_name")) json["emoji_name"].asText() else null,
            json["name"].asText())
    }

    override fun buildDefaultReactionEmoji(json: JsonNode): DefaultReactionEmoji {
        return DefaultReactionEmojiImpl(
            yde,
            json,
            if (json.has("emoji_id")) GetterSnowFlake.of(json["emoji_id"].asLong()) else null,
            json["name"].asText())
    }

    override fun buildAuditLogEntry(json: JsonNode): AuditLogEntry {
        return AuditLogEntryImpl(
            yde,
            json,
            json["id"].asLong(),
            if (json.has("target_id")) json["target_id"].asText() else null,
            json["changes"].map { buildAuditLogChange(it) },
            if (json.has("user_id")) yde.getUserById(json["user_id"].asLong()) else null,
            AuditLogType.getValue(json["type"].asInt()),
            if (json.has("reason")) json["reason"].asText() else null)
    }

    override fun buildAuditLogChange(json: JsonNode): AuditLogChange {
        return AuditLogChangeImpl(
            yde,
            json,
            if (json.has("new_value")) checkType(json["new_value"]) else null,
            if (json.has("old_value")) checkType(json["old_value"]) else null,
            json["key"].asText())
    }

    override fun buildPartialApplication(json: JsonNode): PartialApplication {
        return PartialApplicationImpl(json, json["id"].asLong(), yde, json["flags"].asInt())
    }

    override fun buildStickerItem(json: JsonNode): StickerItem {
        return StickerItemImpl(
            yde,
            json,
            json["id"].asLong(),
            json["name"].asText(),
            StickerType.getValue(json["type"].asInt()))
    }

    override fun buildAvatar(url: URL): Avatar {
        return AvatarImpl(yde, url)
    }

    override fun buildMessageReference(json: JsonNode): MessageReference {
        return MessageReferenceImpl(
            yde,
            json,
            GetterSnowFlake.of(json["message_id"].asLong()),
            GetterSnowFlake.of(json["channel_id"].asLong()),
            GetterSnowFlake.of(json["guild_id"].asLong()),
            yde.getGuildById(json["guild_id"].asLong())
                ?: throw IllegalStateException("Guild is null"))
    }

    override fun buildReaction(json: JsonNode): Reaction {
        return ReactionImpl(
            yde, json, json["count"].asInt(), json["me"].asBoolean(), buildEmoji(json["emoji"]))
    }

    override fun buildMessageInteraction(json: JsonNode): MessageInteraction {
        return MessageInteractionImpl(
            yde,
            json,
            json["id"].asLong(),
            InteractionType.getValue(json["type"].asInt()),
            json["name"].asText(),
            buildUser(json["user"]),
            if (json.has("member"))
                buildMember(
                    json["member"],
                    yde.getGuildById(json["guild_id"].asLong())!!,
                    buildUser(json["user"]))
            else null)
    }

    override fun buildMessageActivity(json: JsonNode): MessageActivity {
        return MessageActivityImpl(
            yde,
            json,
            MessageActivityType.getValue(json["type"].asInt()),
            if (json.has("party_id")) json["party_id"].asText() else null)
    }

    override fun buildEmbed(json: JsonNode): Embed {
        return EmbedImpl(
            yde,
            json,
            if (json.has("title")) json["title"].asText() else null,
            if (json.has("type")) EmbedType.getValue(json["type"].asText()) else null,
            if (json.has("description")) json["description"].asText() else null,
            if (json.has("url")) @Suppress("DEPRECATION") URL(json["url"].asText()) else null,
            if (json.has("timestamp")) json["timestamp"].asText() else null,
            if (json.has("color")) Color(json["color"].asInt()) else null,
            if (json.has("footer")) buildFooter(json["footer"]) else null,
            if (json.has("image")) buildImage(json["image"]) else null,
            if (json.has("thumbnail")) buildThumbnail(json["thumbnail"]) else null,
            if (json.has("video")) buildVideo(json["video"]) else null,
            if (json.has("provider")) buildProvider(json["provider"]) else null,
            if (json.has("author")) buildAuthor(json["author"]) else null,
            if (json.has("fields")) json["fields"].map { buildField(it) } else emptyList())
    }

    override fun buildAttachment(json: JsonNode): Attachment {
        return AttachmentImpl(
            yde,
            json,
            json["id"].asLong(),
            if (json.has("description")) json["description"].asText() else null,
            if (json.has("media_type")) json["media_type"].asText() else null,
            @Suppress("DEPRECATION") URL(json["url"].asText()),
            @Suppress("DEPRECATION") URL(json["proxy_url"].asText()),
            json["size"].asInt(),
            if (json.has("height")) json["height"].asInt() else null,
            if (json.has("width")) json["width"].asInt() else null,
            json["ephemeral"].asBoolean(),
            if (json.has("duration_secs")) json["duration_secs"].floatValue() else null,
            if (json.has("waveform")) json["waveform"].asText() else null,
            if (json.has("flags")) AttachmentFlags.getValue(json["flags"].asInt()) else null,
            json["filename"].asText())
    }

    override fun buildEmoji(json: JsonNode): Emoji {
        return EmojiImpl(
            yde,
            json,
            if (json.has("id")) json["id"].asLong() else null,
            if (json.has("roles")) json["roles"].map { GetterSnowFlake.of(it.asLong()) }
            else emptyList(),
            if (json.has("user")) buildUser(json["user"]) else null,
            if (json.has("require_colons")) json["require_colons"].asBoolean() else false,
            if (json.has("managed")) json["managed"].asBoolean() else false,
            if (json.has("animated")) json["animated"].asBoolean() else false,
            if (json.has("available")) json["available"].asBoolean() else false,
            json["name"].asText())
    }

    override fun buildPartialEmoji(json: JsonNode): PartialEmoji {
        TODO("Not yet implemented")
    }

    override fun buildAuthor(json: JsonNode): Author {
        return AuthorImpl(
            yde,
            json,
            json["name"].asText(),
            if (json.has("url")) @Suppress("DEPRECATION") URL(json["url"].asText()) else null,
            if (json.has("icon_url")) json["icon_url"].asText() else null,
            if (json.has("proxy_icon_url")) json["proxy_icon_url"].asText() else null)
    }

    override fun buildField(json: JsonNode): Field {
        return FieldImpl(
            yde,
            json,
            json["name"].asText(),
            json["value"].asText(),
            if (json.has("inline")) json["inline"].asBoolean() else false)
    }

    override fun buildFooter(json: JsonNode): Footer {
        return FooterImpl(
            yde,
            json,
            json["text"].asText(),
            if (json.has("icon_url")) json["icon_url"].asText() else null,
            if (json.has("proxy_icon_url")) json["proxy_icon_url"].asText() else null)
    }

    override fun buildImage(json: JsonNode): Image {
        return ImageImpl(
            yde,
            json,
            @Suppress("DEPRECATION") URL(json["url"].asText()),
            if (json.has("proxy_url")) @Suppress("DEPRECATION") URL(json["proxy_url"].asText())
            else null,
            if (json.has("height")) json["height"].asInt() else null,
            if (json.has("width")) json["width"].asInt() else null)
    }

    override fun buildProvider(json: JsonNode): Provider {
        return ProviderImpl(
            yde,
            json,
            if (json.has("name")) json["name"].asText() else null,
            if (json.has("url")) json["url"].asText() else null)
    }

    override fun buildThumbnail(json: JsonNode): Thumbnail {
        return ThumbnailImpl(
            yde,
            json,
            @Suppress("DEPRECATION") URL(json["url"].asText()),
            if (json.has("proxy_url")) json["proxy_url"].asText() else null,
            if (json.has("height")) json["height"].asInt() else null,
            if (json.has("width")) json["width"].asInt() else null)
    }

    override fun buildVideo(json: JsonNode): Video {
        return VideoImpl(
            yde,
            json,
            @Suppress("DEPRECATION") URL(json["url"].asText()),
            if (json.has("proxy_url")) json["proxy_url"].asText() else null,
            if (json.has("height")) json["height"].asInt() else null,
            if (json.has("width")) json["width"].asInt() else null)
    }

    override fun buildInteraction(json: JsonNode): Interaction {
        return InteractionImpl(
            yde,
            json,
            json["id"].asLong(),
            GetterSnowFlake.of(json["application_id"].asLong()),
            InteractionType.getValue(json["type"].asInt()),
            if (json.has("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null,
            if (json.has("channel_id")) yde.getChannelById(json["channel_id"].asLong()) else null,
            if (json.has("member"))
                buildMember(json["member"], yde.getGuildById(json["guild_id"].asLong())!!, null)
            else null,
            if (json.has("user")) buildUser(json["user"])
            else if (json.has("member")) buildUser(json["member"]["user"])
            else throw IllegalStateException("User is null"),
            json["token"].asText(),
            json["version"].asInt(),
            if (json.has("message")) buildMessage(json["message"]) else null,
            if (json.has("permissions")) json["permissions"].asLong() else null,
            if (json.has("locale")) json["locale"].asText() else null,
            if (json.has("guild_locale")) json["guild_locale"].asText() else null,
        )
    }

    override fun buildComponentInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ComponentInteraction {
        val message = buildMessage(json["message"])
        val guild = if (json.has("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null
        return ComponentInteractionImpl(
            yde,
            json,
            interactionId,
            ComponentType.getValue(json["component_type"].asInt()),
            json["token"].asText(),
            message,
            if (json.has("member")) buildMember(json["member"], guild!!, null) else null,
            if (json.has("user")) buildUser(json["user"]) else null,
            guild,
            if (json.has("channel_id"))
                yde.getGuildChannelById(json["channel_id"].asLong())
                    ?.guildChannelGetter
                    ?.asGuildMessageChannel()
            else null,
            if (json.has("application_id")) GetterSnowFlake.of(json["application_id"].asLong())
            else null,
            message.components,
            buildComponentInteractionData(json),
        )
    }

    override fun buildComponent(json: JsonNode): Component {
        return ComponentImpl(yde, json, ComponentType.getValue(json["type"].asInt()))
    }

    override fun buildComponentInteractionData(json: JsonNode): ComponentInteractionData {
        return ComponentInteractionDataImpl(
            yde,
            json,
            json["custom_id"].asText(),
            ComponentType.getValue(json["component_type"].asInt()),
            if (json.has("values")) json["values"].map { buildSelectOptionValue(it) }
            else emptyList())
    }

    override fun buildSelectOptionValue(
        json: JsonNode
    ): ComponentInteractionData.SelectOptionValue {
        return ComponentInteractionDataImpl.SelectOptionValueImpl(
            yde,
            json,
            json["name"].asText(),
            json["value"].asText(),
            if (json.has("description")) json["description"].asText() else null,
            if (json.has("emoji")) buildEmoji(json["emoji"]) else null,
            if (json.has("default")) json["default"].asBoolean() else false)
    }

    override fun buildTextInput(json: JsonNode): TextInput {
        return TextInputImpl(
            yde,
            json,
            json["custom_id"].asText(),
            TextInput.TextInputStyle.getValue(json["style"].asInt()),
            json["label"].asText(),
            if (json.has("min_length")) json["min_length"].asInt() else null,
            if (json.has("max_length")) json["max_length"].asInt() else null,
            if (json.has("required")) json["required"].asBoolean() else false,
            if (json.has("initial_value")) json["initial_value"].asText() else null,
            if (json.has("placeholder")) json["placeholder"].asText() else null)
    }

    override fun buildSelectMenu(json: JsonNode): SelectMenu {
        TODO("Not yet implemented")
    }

    override fun buildButton(json: JsonNode): Button {
        TODO("Not yet implemented")
    }

    override fun buildActionRow(json: JsonNode): ActionRow {
        return ActionRowImpl(yde, json, json["components"].map { buildComponent(it) })
    }

    override fun buildSelectMenuOption(json: JsonNode): SelectMenuOption {
        return SelectMenuOptionImpl(
            yde,
            json,
            json["label"].asText(),
            json["value"].asText(),
            if (json.has("description")) json["description"].asText() else null,
            if (json.has("emoji")) buildEmoji(json["emoji"]) else null,
            if (json.has("default")) json["default"].asBoolean() else false)
    }

    override fun buildSelectMenuDefaultValues(json: JsonNode): SelectMenuDefaultValues {
        return SelectMenuDefaultValuesImpl(
            yde, json, SelectMenuDefaultValues.Type.getValue(json["type"].asText()))
    }

    override fun buildActionRowInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ActionRowInteraction {
        return ActionRowInteractionImpl(yde, json, interactionId)
    }

    override fun buildSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): SelectMenuInteraction {
        TODO("Not yet implemented")
    }

    override fun buildButtonInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ButtonInteraction {
        TODO("Not yet implemented")
    }

    override fun buildUserSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): UserSelectMenuInteraction {
        TODO("Not yet implemented")
    }

    override fun buildStringSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): StringSelectMenuInteraction {
        TODO("Not yet implemented")
    }

    override fun buildRoleSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): RoleSelectMenuInteraction {
        TODO("Not yet implemented")
    }

    override fun buildMemberSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): MemberSelectMenuInteraction {
        TODO("Not yet implemented")
    }

    override fun buildChannelSelectMenuInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ChannelSelectMenuInteraction {
        TODO("Not yet implemented")
    }

    override fun buildTextInputInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): TextInputInteraction {
        TODO("Not yet implemented")
    }

    override fun buildApplicationCommand(json: JsonNode): ApplicationCommand {
        TODO("Not yet implemented")
    }

    override fun buildApplicationCommandOption(json: JsonNode): ApplicationCommandOption {
        TODO("Not yet implemented")
    }

    override fun buildUserCommand(json: JsonNode, interaction: Interaction?): UserCommand {
        val i = interaction ?: buildInteraction(json)
        val user = buildUser(json["data"]["resolved"]["users"])

        return UserCommandImpl(
            yde, json, i, user, buildMember(json["data"]["resolved"]["members"], i.guild!!, user))
    }

    override fun buildSlashCommand(json: JsonNode, interaction: Interaction?): SlashCommand {
        return SlashCommandImpl(yde, json, interaction ?: buildInteraction(json))
    }

    override fun buildMessageCommand(json: JsonNode, interaction: Interaction?): MessageCommand {
        return MessageCommandImpl(
            yde,
            json,
            interaction ?: buildInteraction(json),
            buildMessage(json["data"]["resolved"]["messages"]))
    }
}
