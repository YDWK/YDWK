/*
 * Copyright 2024-2026 YDWK inc.
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

enum class GuildFeature(private val feature: String) {
  /** Guild has access to set an animated guild banner image. */
  ANIMATED_BANNER("ANIMATED_BANNER"),

  /** Guild has access to set an animated guild icon. */
  ANIMATED_ICON("ANIMATED_ICON"),

  /**
   * Guild has application command permissions v2 enabled, disabling the old behavior of granting
   * default permissions on all commands.
   */
  APPLICATION_COMMAND_PERMISSIONS_V2("APPLICATION_COMMAND_PERMISSIONS_V2"),

  /** Guild has set up auto moderation rules. */
  AUTO_MODERATION("AUTO_MODERATION"),

  /** Guild has access to set a guild banner image. */
  BANNER("BANNER"),

  /**
   * Guild can enable welcome screen, Membership Screening, stage channels and discovery, and
   * receives community updates.
   */
  COMMUNITY("COMMUNITY"),

  /** Guild has enabled monetization and is provisionally monetizable. */
  CREATOR_MONETIZABLE_PROVISIONAL("CREATOR_MONETIZABLE_PROVISIONAL"),

  /** Guild has a creator store page. */
  CREATOR_STORE_PAGE("CREATOR_STORE_PAGE"),

  /** Guild is a developer support server. */
  DEVELOPER_SUPPORT_SERVER("DEVELOPER_SUPPORT_SERVER"),

  /** Guild is able to be discovered in the directory. */
  DISCOVERABLE("DISCOVERABLE"),

  /** Guild has enhanced role colors. */
  ENHANCED_ROLE_COLORS("ENHANCED_ROLE_COLORS"),

  /** Guild is able to be featured in the directory. */
  FEATURABLE("FEATURABLE"),

  /** Guild has guests enabled (temporary access via voice channels). */
  GUESTS_ENABLED("GUESTS_ENABLED"),

  /** Guild has guild tags. */
  GUILD_TAGS("GUILD_TAGS"),

  /** Guild has paused invites, preventing new users from joining. */
  INVITES_DISABLED("INVITES_DISABLED"),

  /** Guild has access to set an invitation splash background. */
  INVITE_SPLASH("INVITE_SPLASH"),

  /** Guild has enabled Membership Screening. */
  MEMBER_VERIFICATION_GATE_ENABLED("MEMBER_VERIFICATION_GATE_ENABLED"),

  /** Guild has enabled monetization. */
  MONETIZATION_ENABLED("MONETIZATION_ENABLED"),

  /** Guild has increased custom soundboard slots. */
  MORE_SOUNDBOARD("MORE_SOUNDBOARD"),

  /** Guild has increased custom sticker slots. */
  MORE_STICKERS("MORE_STICKERS"),

  /** Guild has access to create announcement channels. */
  NEWS("NEWS"),

  /** Guild is partnered. */
  PARTNERED("PARTNERED"),

  /** Guild can be previewed before joining via Membership Screening or the directory. */
  PREVIEW_ENABLED("PREVIEW_ENABLED"),

  /** Guild has access to create private threads. */
  PRIVATE_THREADS("PRIVATE_THREADS"),

  /** Guild has raid alerts disabled. */
  RAID_ALERTS_DISABLED("RAID_ALERTS_DISABLED"),

  /** Guild is able to set role icons. */
  ROLE_ICONS("ROLE_ICONS"),

  /** Guild has role subscriptions available for purchase. */
  ROLE_SUBSCRIPTIONS_AVAILABLE_FOR_PURCHASE("ROLE_SUBSCRIPTIONS_AVAILABLE_FOR_PURCHASE"),

  /** Guild has role subscriptions enabled. */
  ROLE_SUBSCRIPTIONS_ENABLED("ROLE_SUBSCRIPTIONS_ENABLED"),

  /** Guild has the soundboard feature enabled. */
  SOUNDBOARD("SOUNDBOARD"),

  /** Guild has enabled the ticketed events feature. */
  TICKETED_EVENTS_ENABLED("TICKETED_EVENTS_ENABLED"),

  /** Guild has access to set a vanity URL. */
  VANITY_URL("VANITY_URL"),

  /** Guild is verified. */
  VERIFIED("VERIFIED"),

  /** Guild has access to set 384kbps bitrate in voice (previously VIP voice servers). */
  VIP_REGIONS("VIP_REGIONS"),

  /** Guild has enabled the welcome screen. */
  WELCOME_SCREEN_ENABLED("WELCOME_SCREEN_ENABLED"),

  /** An unknown guild feature. */
  UNKNOWN("UNKNOWN");

  companion object {
    /**
     * The [GuildFeature] for the provided [feature].
     *
     * @param feature The feature to get the [GuildFeature] for.
     * @return The [GuildFeature] for the provided [feature].
     */
    fun getValue(feature: String): GuildFeature {
      return entries.firstOrNull { it.feature == feature } ?: UNKNOWN
    }
  }

  /**
   * The feature of the [GuildFeature].
   *
   * @return The feature of the [GuildFeature].
   */
  override fun toString(): String {
    return feature
  }
}
