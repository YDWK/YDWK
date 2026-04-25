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
 * Configuration DSL for [io.github.ydwk.ydwk.bot.ModerationSystem].
 *
 * Pass a lambda to [io.github.ydwk.ydwk.bot.useModerationSystem] to fill in these values.
 *
 * ```kotlin
 * ydwk.useModerationSystem {
 *     database      = MyExposedDatabase()
 *     autoBanAt     = 3
 *     warnCommandName = "warn"   // rename any command if needed
 * }
 * ```
 */
class ModerationConfig {

  /**
   * The database back-end used to store warn records.
   *
   * When `null` the warn/warns/clearwarns commands reply with an error; kick and ban still work.
   */
  var database: ModerationDatabase? = null

  /**
   * Auto-ban threshold: how many active warnings trigger an automatic ban and clear.
   *
   * Defaults to `3`. Set to `0` or negative to disable auto-ban.
   */
  var autoBanAt: Int = 3

  // ── Command names ─────────────────────────────────────────────────────────

  /** Name of the warn slash command. Defaults to `"warn"`. */
  var warnCommandName: String = "warn"

  /** Name of the kick slash command. Defaults to `"kick"`. */
  var kickCommandName: String = "kick"

  /** Name of the ban slash command. Defaults to `"ban"`. */
  var banCommandName: String = "ban"

  /** Name of the unban slash command. Defaults to `"unban"`. */
  var unbanCommandName: String = "unban"

  /** Name of the warns (list) slash command. Defaults to `"warns"`. */
  var warnsCommandName: String = "warns"

  /** Name of the clear-warns slash command. Defaults to `"clearwarns"`. */
  var clearWarnsCommandName: String = "clearwarns"
}
