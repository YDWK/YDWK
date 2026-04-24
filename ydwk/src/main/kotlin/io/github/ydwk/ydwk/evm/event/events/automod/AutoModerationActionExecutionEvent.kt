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
package io.github.ydwk.ydwk.evm.event.events.automod

import io.github.ydwk.yde.entities.guild.automod.AutoModerationAction
import io.github.ydwk.yde.entities.guild.automod.AutoModerationTriggerType
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.event.Event

/**
 * Fired when an auto moderation rule is triggered and an action is executed.
 *
 * @param ydwk The [YDWK] instance.
 * @param guildId The guild where this action was executed.
 * @param action The action that was executed.
 * @param ruleId The ID of the rule that was triggered.
 * @param ruleTriggerType The trigger type of the rule.
 * @param userId The ID of the user that triggered the rule.
 * @param channelId The channel where the rule was triggered (null if not applicable).
 * @param messageId The ID of the triggering message (null if not applicable).
 * @param alertSystemMessageId The ID of the alert system message (null if not applicable).
 * @param content The content that triggered the rule.
 * @param matchedKeyword The keyword that was matched (null if not applicable).
 * @param matchedContent The content that matched the keyword (null if not applicable).
 */
data class AutoModerationActionExecutionEvent(
  override val ydwk: YDWK,
  val guildId: GetterSnowFlake,
  val action: AutoModerationAction,
  val ruleId: GetterSnowFlake,
  val ruleTriggerType: AutoModerationTriggerType,
  val userId: GetterSnowFlake,
  val channelId: GetterSnowFlake?,
  val messageId: GetterSnowFlake?,
  val alertSystemMessageId: GetterSnowFlake?,
  val content: String,
  val matchedKeyword: String?,
  val matchedContent: String?,
) : Event(ydwk)
