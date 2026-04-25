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

/**
 * A snapshot of a ticket stored in the database.
 *
 * @param id Internal auto-increment ID assigned by the database.
 * @param guildId Discord guild snowflake (String).
 * @param channelId Discord channel snowflake (String) where the ticket lives.
 * @param ownerId Discord user snowflake (String) who opened the ticket.
 * @param status `"OPEN"` or `"CLOSED"`.
 */
data class TicketRecord(
  val id: Int,
  val guildId: String,
  val channelId: String,
  val ownerId: String,
  val status: String,
)

/**
 * Plug-in interface for ticket persistence.
 *
 * Implement this with your preferred database library and pass the instance to
 * [io.github.ydwk.ydwk.bot.ticket.TicketConfig.database].
 *
 * All functions are `suspend` so they can perform non-blocking I/O inside a coroutine.
 */
interface TicketDatabase {

  /**
   * Persists a new ticket record. Called after the ticket channel has been created.
   *
   * @param guildId Snowflake string of the guild.
   * @param channelId Snowflake string of the newly-created ticket channel.
   * @param ownerId Snowflake string of the user who opened the ticket.
   * @return Auto-generated ticket ID.
   */
  suspend fun openTicket(guildId: String, channelId: String, ownerId: String): Int

  /**
   * Marks an existing ticket as closed. Does **not** delete the channel — that is handled by
   * [TicketConfig.onClose].
   *
   * @param channelId Snowflake string of the ticket channel.
   */
  suspend fun closeTicket(channelId: String)

  /**
   * Looks up a ticket by its channel ID.
   *
   * @param channelId Snowflake string of the channel.
   * @return The [TicketRecord], or `null` if the channel is not a known ticket.
   */
  suspend fun getTicketByChannel(channelId: String): TicketRecord?

  /**
   * Returns all **open** tickets owned by [userId] in [guildId].
   *
   * Used to enforce a per-user open-ticket limit.
   *
   * @param guildId Snowflake string of the guild.
   * @param userId Snowflake string of the user.
   * @return List of open [TicketRecord] objects.
   */
  suspend fun getOpenTicketsByUser(guildId: String, userId: String): List<TicketRecord>
}
