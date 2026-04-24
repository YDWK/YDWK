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
package io.github.ydwk.yde.entities.guild.automod

import io.github.ydwk.yde.util.GetterSnowFlake

/** Represents an action to be taken when an auto moderation rule is triggered. */
interface AutoModerationAction {
  /** The type of action. */
  val type: AutoModerationActionType

  /** The channel to send an alert to (only for SEND_ALERT_MESSAGE). */
  val channelId: GetterSnowFlake?

  /** The duration to timeout the user for in seconds (only for TIMEOUT). */
  val durationSeconds: Int?

  /** Custom message to show when a message is blocked (only for BLOCK_MESSAGE). */
  val customMessage: String?
}
