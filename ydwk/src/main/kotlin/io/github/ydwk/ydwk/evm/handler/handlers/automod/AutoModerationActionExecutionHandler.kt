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
package io.github.ydwk.ydwk.evm.handler.handlers.automod

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.guild.automod.AutoModerationTriggerType
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.impl.entities.guild.automod.AutoModerationActionImpl
import io.github.ydwk.ydwk.evm.event.events.automod.AutoModerationActionExecutionEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class AutoModerationActionExecutionHandler(ydwk: YDWKImpl, json: JsonNode) :
    Handler(ydwk, json) {
    override suspend fun start() {
        val guildId = GetterSnowFlake.of(json["guild_id"].asLong())
        val action = AutoModerationActionImpl(json["action"])
        val ruleId = GetterSnowFlake.of(json["rule_id"].asLong())
        val ruleTriggerType =
            AutoModerationTriggerType.fromValue(json["rule_trigger_type"].asInt())
        val userId = GetterSnowFlake.of(json["user_id"].asLong())
        val channelId =
            if (json.has("channel_id")) GetterSnowFlake.of(json["channel_id"].asLong()) else null
        val messageId =
            if (json.has("message_id")) GetterSnowFlake.of(json["message_id"].asLong()) else null
        val alertSystemMessageId =
            if (json.has("alert_system_message_id"))
                GetterSnowFlake.of(json["alert_system_message_id"].asLong())
            else null
        val content = if (json.has("content")) json["content"].asText() else ""
        val matchedKeyword =
            if (json.has("matched_keyword") && !json["matched_keyword"].isNull)
                json["matched_keyword"].asText()
            else null
        val matchedContent =
            if (json.has("matched_content") && !json["matched_content"].isNull)
                json["matched_content"].asText()
            else null

        ydwk.emitEvent(
            AutoModerationActionExecutionEvent(
                ydwk,
                guildId,
                action,
                ruleId,
                ruleTriggerType,
                userId,
                channelId,
                messageId,
                alertSystemMessageId,
                content,
                matchedKeyword,
                matchedContent,
            ))
    }
}
