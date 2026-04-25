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
package io.github.ydwk.ydwk.bot.ticket

import io.github.ydwk.ydwk.YDWK

/**
 * Configuration DSL for [TicketSystem].
 *
 * Pass a lambda to [io.github.ydwk.ydwk.bot.useTicketSystem] to fill in these values.
 *
 * ```kotlin
 * ydwk.useTicketSystem {
 *     database         = MyTicketDatabase()
 *     maxOpenPerUser   = 1
 *
 *     // Called when a user clicks the "Open Ticket" button.
 *     // Create the channel however you like and return its snowflake ID,
 *     // or return null to abort.
 *     onOpen = { ydwk, guildId, userId ->
 *         val guild = ydwk.getGuildById(guildId) ?: return@onOpen null
 *         // create channel via REST, return its id
 *         "1234567890"
 *     }
 *
 *     // Optional: called when /ticket close is used; delete or archive the channel here.
 *     onClose = { ydwk, ticket ->
 *         // e.g. schedule channel deletion
 *     }
 * }
 * ```
 */
class TicketConfig {

  /**
   * The database back-end used to track ticket state.
   *
   * When `null`, open/close DB operations are silently skipped; the callbacks still fire.
   */
  var database: TicketDatabase? = null

  /**
   * `customId` on the button component that should open a new ticket.
   *
   * Defaults to `"ydwk_open_ticket"`. Set this to the `customId` you used when building your panel
   * embed's button.
   */
  var openButtonCustomId: String = "ydwk_open_ticket"

  /**
   * Name of the top-level slash command whose `close` sub-command closes a ticket.
   *
   * Defaults to `"ticket"` (i.e. `/ticket close`).
   */
  var ticketCommandName: String = "ticket"

  /**
   * Maximum number of open tickets a single user may have at one time.
   *
   * Defaults to `1`. Set to `0` to allow unlimited tickets.
   */
  var maxOpenPerUser: Int = 1

  /**
   * Called when a user clicks the open-ticket button.
   *
   * Receives the [YDWK] instance, the guild snowflake string, and the user snowflake string.
   * Should create the ticket channel (via REST or any other mechanism) and return its snowflake
   * string, or `null` if channel creation failed.
   *
   * The system records the new ticket in [database] after this callback returns a non-null ID.
   */
  var onOpen: (suspend (ydwk: YDWK, guildId: String, userId: String) -> String?)? = null

  /**
   * Optional callback invoked after a ticket is marked closed in [database].
   *
   * Use this to delete / archive the channel, send a transcript, etc.
   *
   * Receives the [YDWK] instance and the [TicketRecord] that was just closed.
   */
  var onClose: (suspend (ydwk: YDWK, ticket: TicketRecord) -> Unit)? = null
}
