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
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
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
import io.github.ydwk.yde.entities.guild.enums.*
import io.github.ydwk.yde.entities.guild.role.RoleTag
import io.github.ydwk.yde.entities.guild.ws.WelcomeChannel
import io.github.ydwk.yde.entities.message.*
import io.github.ydwk.yde.entities.message.embed.*
import io.github.ydwk.yde.entities.sticker.StickerFormatType
import io.github.ydwk.yde.entities.sticker.StickerItem
import io.github.ydwk.yde.entities.sticker.StickerType
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.impl.entities.*
import io.github.ydwk.yde.impl.entities.application.PartialApplicationImpl
import io.github.ydwk.yde.impl.entities.guild.BanImpl
import io.github.ydwk.yde.impl.entities.guild.MemberImpl
import io.github.ydwk.yde.impl.entities.guild.RoleImpl
import io.github.ydwk.yde.impl.entities.guild.WelcomeScreenImpl
import io.github.ydwk.yde.impl.entities.guild.role.RoleTagImpl
import io.github.ydwk.yde.impl.entities.guild.ws.WelcomeChannelImpl
import io.github.ydwk.yde.impl.interaction.message.ComponentImpl
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
import io.github.ydwk.yde.util.*
import java.awt.Color

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
            val channelType = ChannelType.fromInt(it["type"].asInt())
            if (ChannelType.isGuildChannel(channelType)) {
                channels.add(buildGuildChannel(it))
            } else {
                channels.add(buildDMChannel(it))
            }
        }

        val thread: Channel? =
            if (json.has("thread")) {
                val newThreadJson = json.get("thread")
                val channelType = ChannelType.fromInt(newThreadJson["type"].asInt())
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
            json["components"].map { ComponentImpl(yde, it) },
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
        return object :
            UnavailableGuildImpl(yde, json, json["id"].asLong(), json["unavailable"].asBoolean()) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).toString()
            }
        }
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

        return object :
            VoiceStateImpl(
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
                if (json.has("request_to_speak_timestamp"))
                    json["request_to_speak_timestamp"].asText()
                else null) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).add("sessionId", sessionId).toString()
            }
        }
    }

    override fun buildVoiceRegion(json: JsonNode): VoiceState.VoiceRegion {
        TODO("Not yet implemented")
    }

    override fun buildAuditLog(json: JsonNode): AuditLog {
        return object :
            AuditLogImpl(yde, json, emptyList(), json["entries"].map { buildAuditLogEntry(it) }) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).name("AuditLog").toString()
            }
        }
    }

    override fun buildApplication(json: JsonNode): Application {
        TODO("Not yet implemented")
    }

    override fun buildBan(json: JsonNode): Ban {
        return object :
            BanImpl(
                yde,
                json,
                if (json.has("reason")) json["reason"].asText() else null,
                buildUser(json["user"])) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).toString()
            }
        }
    }

    override fun buildGuildScheduledEvent(json: JsonNode): GuildScheduledEvent {
        TODO("Not yet implemented")
    }

    override fun buildInvite(json: JsonNode): Invite {
        TODO("Not yet implemented")
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
            backUpUser,
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
        return object :
            WelcomeScreenImpl(
                yde,
                json,
                if (json.has("description")) json["description"].asText() else null,
                json["welcome_channels"].map { buildWelcomeScreenChannel(it) }) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).toString()
            }
        }
    }

    override fun buildWelcomeScreenChannel(json: JsonNode): WelcomeChannel {
        return object :
            WelcomeChannelImpl(
                yde,
                json,
                GetterSnowFlake.of(json["channel_id"].asLong()),
                json["description"].asText(),
                if (json.has("emoji_id")) GetterSnowFlake.of(json["emoji_id"].asLong()) else null,
                if (json.has("emoji_name")) json["emoji_name"].asText() else null) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).toString()
            }
        }
    }

    override fun buildRoleTag(json: JsonNode): RoleTag {
        return object :
            RoleTagImpl(
                yde,
                json,
                if (json.has("bot_id")) GetterSnowFlake.of(json["bot_id"].asLong()) else null,
                if (json.has("integration_id")) GetterSnowFlake.of(json["integration_id"].asLong())
                else null) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).toString()
            }
        }
    }

    override fun buildChannel(json: JsonNode): Channel {
        TODO("Not yet implemented")
    }

    override fun buildDMChannel(json: JsonNode): DmChannel {
        TODO("Not yet implemented")
    }

    override fun buildGuildChannel(json: JsonNode): GuildChannel {
        TODO("Not yet implemented")
    }

    override fun buildGuildCategory(json: JsonNode): GuildCategory {
        TODO("Not yet implemented")
    }

    override fun buildGuildForumChannel(json: JsonNode): GuildForumChannel {
        TODO("Not yet implemented")
    }

    override fun buildGuildMessageChannel(json: JsonNode): GuildMessageChannel {
        TODO("Not yet implemented")
    }

    override fun buildGuildNewsChannel(json: JsonNode): GuildNewsChannel {
        TODO("Not yet implemented")
    }

    override fun buildGuildStageChannel(json: JsonNode): GuildStageChannel {
        TODO("Not yet implemented")
    }

    override fun buildGuildTextChannel(json: JsonNode): GuildTextChannel {
        TODO("Not yet implemented")
    }

    override fun buildGuildVoiceChannel(json: JsonNode): GuildVoiceChannel {
        TODO("Not yet implemented")
    }

    override fun buildPermissionOverwrite(json: JsonNode): PermissionOverwrite {
        TODO("Not yet implemented")
    }

    override fun buildForumTag(json: JsonNode): ForumTag {
        TODO("Not yet implemented")
    }

    override fun buildDefaultReactionEmoji(json: JsonNode): DefaultReactionEmoji {
        TODO("Not yet implemented")
    }

    override fun buildAuditLogEntry(json: JsonNode): AuditLogEntry {
        TODO("Not yet implemented")
    }

    override fun buildAuditLogChange(json: JsonNode): AuditLogChange {
        TODO("Not yet implemented")
    }

    override fun buildPartialApplication(json: JsonNode): PartialApplication {
        return object :
            PartialApplicationImpl(json, json["id"].asLong(), yde, json["flags"].asInt()) {
            override fun toString(): String {
                return EntityToStringBuilder(yde, this).toString()
            }
        }
    }

    override fun buildStickerItem(json: JsonNode): StickerItem {
        TODO("Not yet implemented")
    }

    override fun buildAvatar(json: JsonNode): Avatar {
        TODO("Not yet implemented")
    }

    override fun buildMessageReference(json: JsonNode): MessageReference {
        TODO("Not yet implemented")
    }

    override fun buildReaction(json: JsonNode): Reaction {
        TODO("Not yet implemented")
    }

    override fun buildMessageInteraction(json: JsonNode): MessageInteraction {
        TODO("Not yet implemented")
    }

    override fun buildMessageActivity(json: JsonNode): MessageActivity {
        TODO("Not yet implemented")
    }

    override fun buildEmbed(json: JsonNode): Embed {
        TODO("Not yet implemented")
    }

    override fun buildAttachment(json: JsonNode): Attachment {
        TODO("Not yet implemented")
    }

    override fun buildEmoji(json: JsonNode): Emoji {
        TODO("Not yet implemented")
    }

    override fun buildAuthor(json: JsonNode): Author {
        TODO("Not yet implemented")
    }

    override fun buildField(json: JsonNode): Field {
        TODO("Not yet implemented")
    }

    override fun buildFooter(json: JsonNode): Footer {
        TODO("Not yet implemented")
    }

    override fun buildImage(json: JsonNode): Image {
        TODO("Not yet implemented")
    }

    override fun buildProvider(json: JsonNode): Provider {
        TODO("Not yet implemented")
    }

    override fun buildThumbnail(json: JsonNode): Thumbnail {
        TODO("Not yet implemented")
    }

    override fun buildVideo(json: JsonNode): Video {
        TODO("Not yet implemented")
    }

    override fun buildInteraction(json: JsonNode): Interaction {
        TODO("Not yet implemented")
    }

    override fun buildComponentInteraction(
        json: JsonNode,
        interactionId: GetterSnowFlake
    ): ComponentInteraction {
        TODO("Not yet implemented")
    }

    override fun buildComponent(json: JsonNode): Component {
        TODO("Not yet implemented")
    }

    override fun buildComponentInteractionData(json: JsonNode): ComponentInteractionData {
        TODO("Not yet implemented")
    }

    override fun buildSelectOptionValue(
        json: JsonNode
    ): ComponentInteractionData.SelectOptionValue {
        TODO("Not yet implemented")
    }

    override fun buildTextInput(json: JsonNode): TextInput {
        TODO("Not yet implemented")
    }

    override fun buildSelectMenu(json: JsonNode, interactionId: GetterSnowFlake): SelectMenu {
        TODO("Not yet implemented")
    }

    override fun buildButton(json: JsonNode): Button {
        TODO("Not yet implemented")
    }

    override fun buildActionRow(json: JsonNode): ActionRow {
        TODO("Not yet implemented")
    }

    override fun buildUserSelectMenu(json: JsonNode): UserSelectMenu {
        TODO("Not yet implemented")
    }

    override fun buildStringSelectMenu(json: JsonNode): StringSelectMenu {
        TODO("Not yet implemented")
    }

    override fun buildRoleSelectMenu(json: JsonNode): RoleSelectMenu {
        TODO("Not yet implemented")
    }

    override fun buildMemberSelectMenu(json: JsonNode): MemberSelectMenu {
        TODO("Not yet implemented")
    }

    override fun buildChannelSelectMenu(json: JsonNode): ChannelSelectMenu {
        TODO("Not yet implemented")
    }

    override fun buildStringSelectMenuOption(json: JsonNode): StringSelectMenuOption {
        TODO("Not yet implemented")
    }

    override fun buildApplicationCommand(json: JsonNode): ApplicationCommand {
        TODO("Not yet implemented")
    }

    override fun buildApplicationCommandOption(json: JsonNode): ApplicationCommandOption {
        TODO("Not yet implemented")
    }

    override fun buildUserCommand(json: JsonNode, interaction: Interaction?): UserCommand {
        TODO("Not yet implemented")
    }

    override fun buildSlashCommand(json: JsonNode, interaction: Interaction?): SlashCommand {
        TODO("Not yet implemented")
    }

    override fun buildMessageCommand(json: JsonNode, interaction: Interaction?): MessageCommand {
        TODO("Not yet implemented")
    }
}
