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
package io.github.ydwk.yde.impl.entities.guild.automod

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.guild.automod.AutoModerationAction
import io.github.ydwk.yde.entities.guild.automod.AutoModerationEventType
import io.github.ydwk.yde.entities.guild.automod.AutoModerationRule
import io.github.ydwk.yde.entities.guild.automod.AutoModerationTriggerType
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.util.GetterSnowFlake

internal class AutoModerationRuleImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val guildId: GetterSnowFlake,
    override val creatorId: GetterSnowFlake,
    override var name: String,
    override val eventType: AutoModerationEventType,
    override val triggerType: AutoModerationTriggerType,
    override val actions: List<AutoModerationAction>,
    override val enabled: Boolean,
    override val exemptRoles: List<GetterSnowFlake>,
    override val exemptChannels: List<GetterSnowFlake>,
    override val keywordFilter: List<String>,
    override val regexPatterns: List<String>,
    override val presets: List<Int>,
    override val allowList: List<String>,
    override val mentionTotalLimit: Int?,
    override val mentionRaidProtectionEnabled: Boolean,
) : AutoModerationRule, ToStringEntityImpl<AutoModerationRule>(yde, AutoModerationRule::class.java)
