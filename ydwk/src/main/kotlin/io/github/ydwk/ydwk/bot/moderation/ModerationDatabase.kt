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
package io.github.ydwk.ydwk.bot.moderation

/**
 * A single warning record returned from [ModerationDatabase.getWarns].
 *
 * @param reason Human-readable reason supplied by the moderator.
 * @param moderatorId Discord user-ID (as a String snowflake) of the moderator.
 * @param timestamp Unix epoch milliseconds when the warning was issued.
 */
data class WarnEntry(
  val reason: String,
  val moderatorId: String,
  val timestamp: Long,
)

/**
 * Plug-in interface for persistent warn storage.
 *
 * Implement this with your preferred database library (Exposed, KMongo, R2DBC, …) and pass the
 * instance to [io.github.ydwk.ydwk.bot.ModerationConfig.database].
 *
 * All functions are `suspend` so they can perform non-blocking I/O inside a coroutine.
 */
interface ModerationDatabase {

  /**
   * Persists a new warning for [userId] in [guildId].
   *
   * @param guildId Snowflake string of the Discord guild.
   * @param userId Snowflake string of the warned user.
   * @param reason Human-readable reason.
   * @param moderatorId Snowflake string of the moderator who issued the warning.
   */
  suspend fun addWarn(
    guildId: String,
    userId: String,
    reason: String,
    moderatorId: String,
  )

  /**
   * Returns the total number of active warnings for [userId] in [guildId].
   *
   * @param guildId Snowflake string of the Discord guild.
   * @param userId Snowflake string of the user.
   * @return Number of warnings.
   */
  suspend fun getWarnCount(guildId: String, userId: String): Int

  /**
   * Returns all active warning records for [userId] in [guildId], ordered oldest-first.
   *
   * @param guildId Snowflake string of the Discord guild.
   * @param userId Snowflake string of the user.
   * @return List of [WarnEntry] objects.
   */
  suspend fun getWarns(guildId: String, userId: String): List<WarnEntry>

  /**
   * Deletes all active warnings for [userId] in [guildId] (e.g. after an auto-ban).
   *
   * @param guildId Snowflake string of the Discord guild.
   * @param userId Snowflake string of the user.
   */
  suspend fun clearWarns(guildId: String, userId: String)
}
