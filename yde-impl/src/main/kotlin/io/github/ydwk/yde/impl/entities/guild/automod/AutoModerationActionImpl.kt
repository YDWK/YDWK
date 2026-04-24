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
package io.github.ydwk.yde.impl.entities.guild.automod

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.guild.automod.AutoModerationAction
import io.github.ydwk.yde.entities.guild.automod.AutoModerationActionType
import io.github.ydwk.yde.util.GetterSnowFlake

class AutoModerationActionImpl(private val json: JsonNode) : AutoModerationAction {
    override val type: AutoModerationActionType =
        AutoModerationActionType.fromValue(json["type"].asInt())

    private val metadata: JsonNode? = json["metadata"]

    override val channelId: GetterSnowFlake? =
        metadata?.let {
            if (it.has("channel_id")) GetterSnowFlake.of(it["channel_id"].asLong()) else null
        }

    override val durationSeconds: Int? =
        metadata?.let { if (it.has("duration_seconds")) it["duration_seconds"].asInt() else null }

    override val customMessage: String? =
        metadata?.let { if (it.has("custom_message")) it["custom_message"].asText() else null }
}
