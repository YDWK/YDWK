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
package io.github.ydwk.ydwk.evm.handler.handlers.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.evm.event.events.message.MessageReactionAddEvent
import io.github.ydwk.ydwk.evm.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl

class MessageReactionAddHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
  override suspend fun start() {
    val userId = GetterSnowFlake.of(json.get("user_id").asLong())
    val channelId = GetterSnowFlake.of(json.get("channel_id").asLong())
    val messageId = GetterSnowFlake.of(json.get("message_id").asLong())
    val guildId =
      if (json.has("guild_id")) GetterSnowFlake.of(json.get("guild_id").asLong()) else null
    val emoji = ydwk.entityInstanceBuilder.buildEmoji(json.get("emoji"))
    val member =
      if (json.has("member") && guildId != null)
        ydwk.entityInstanceBuilder.buildMember(json.get("member"), guildId)
      else null
    val message = ydwk.cache[messageId.asString, CacheIds.MESSAGE] as? Message
    val user = ydwk.getUserById(userId.asLong)
    ydwk.emitEvent(
      MessageReactionAddEvent(
        ydwk,
        userId,
        channelId,
        messageId,
        guildId,
        member,
        emoji,
        message,
        user,
      )
    )
  }
}
