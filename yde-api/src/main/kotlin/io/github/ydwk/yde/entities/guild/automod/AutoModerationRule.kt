/*
 * Copyright 2024-2025 YDWK inc.
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
package io.github.ydwk.yde.entities.guild.automod

import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake

/** Represents a Discord auto moderation rule. */
interface AutoModerationRule : SnowFlake, GenericEntity, NameAbleEntity {
    /** The ID of the guild this rule belongs to. */
    val guildId: GetterSnowFlake

    /** The ID of the user who created this rule. */
    val creatorId: GetterSnowFlake

    /** The event type that triggers this rule. */
    val eventType: AutoModerationEventType

    /** The type of content that triggers this rule. */
    val triggerType: AutoModerationTriggerType

    /** The actions to execute when this rule is triggered. */
    val actions: List<AutoModerationAction>

    /** Whether this rule is enabled. */
    val enabled: Boolean

    /** The role IDs that are exempt from this rule. */
    val exemptRoles: List<GetterSnowFlake>

    /** The channel IDs that are exempt from this rule. */
    val exemptChannels: List<GetterSnowFlake>

    /** Keywords to filter (only for KEYWORD trigger type). */
    val keywordFilter: List<String>

    /** Regex patterns to match against (only for KEYWORD trigger type). */
    val regexPatterns: List<String>

    /** Keyword presets to use (only for KEYWORD_PRESET trigger type). */
    val presets: List<Int>

    /** Strings to allow regardless of other filters. */
    val allowList: List<String>

    /** Total number of unique role and user mentions allowed (only for MENTION_SPAM). */
    val mentionTotalLimit: Int?

    /** Whether to automatically detect mention raids (only for MENTION_SPAM). */
    val mentionRaidProtectionEnabled: Boolean
}
