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

enum class GuildFeature(private val feature: String) {
    /** Guild has access to set an animated guild banner image. */
    ANIMATED_BANNER("ANIMATED_BANNER"),

    /** Guild has access to set an animated guild icon. */
    ANIMATED_ICON("ANIMATED_ICON"),

    /** Guild has set up auto moderation rules. */
    AUTO_MODERATION("AUTO_MODERATION"),

    /** Guild has access to set a guild banner image. */
    BANNER("BANNER"),

    /**
     * Guild can enable welcome screen, Membership Screening, stage channels and discovery, and
     * receives community updates.
     */
    COMMUNITY("COMMUNITY"),

    /** Guild is able to be discovered in the directory. */
    DISCOVERABLE("DISCOVERABLE"),

    /** Guild is able to be featured in the directory. */
    FEATURABLE("FEATURABLE"),

    /** Guild has paused invites, preventing new users from joining. */
    INVITES_DISABLED("INVITES_DISABLED"),

    /** Guild has access to set an invitation splash background. */
    INVITE_SPLASH("INVITE_SPLASH"),

    /** Guild has enabled Membership Screening. */
    MEMBER_VERIFICATION_GATE_ENABLED("MEMBER_VERIFICATION_GATE_ENABLED"),

    /** Guild has enabled monetization. */
    MONETIZATION_ENABLED("MONETIZATION_ENABLED"),

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

    /** Guild is able to set role icons. */
    ROLE_ICONS("ROLE_ICONS"),

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
        fun fromString(feature: String): GuildFeature {
            return values().firstOrNull { it.feature == feature } ?: UNKNOWN
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
