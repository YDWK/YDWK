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
package io.github.ydwk.ydwk

enum class GateWayIntent(private var value: Int, private var privileged: Boolean? = false) {
  /** Receive guild lifecycle events (GUILD_CREATE, GUILD_UPDATE, GUILD_DELETE, etc.). */
  GUILDS(0),

  /** Receive guild member events (GUILD_MEMBER_ADD/UPDATE/REMOVE). Privileged. */
  GUILD_MEMBERS(1, true),

  /**
   * Receive guild moderation events (GUILD_BAN_ADD/REMOVE, GUILD_AUDIT_LOG_ENTRY_CREATE). Formerly
   * GUILD_BANS.
   */
  GUILD_MODERATION(2),

  /** Receive guild expression events (GUILD_EMOJIS_UPDATE, GUILD_STICKERS_UPDATE, soundboard). */
  GUILD_EXPRESSIONS(3),

  /** Receive guild integration events (GUILD_INTEGRATIONS_UPDATE, INTEGRATION_*). */
  GUILD_INTEGRATIONS(4),

  /** Receive webhook events (WEBHOOKS_UPDATE). */
  GUILD_WEBHOOKS(5),

  /** Receive invite events (INVITE_CREATE, INVITE_DELETE). */
  GUILD_INVITES(6),

  /** Receive voice state update events (VOICE_STATE_UPDATE). */
  GUILD_VOICE_STATES(7),

  /** Receive presence update events. Privileged. */
  GUILD_PRESENCES(8, true),

  /** Receive guild message events (MESSAGE_CREATE, MESSAGE_UPDATE, MESSAGE_DELETE, etc.). */
  GUILD_MESSAGES(9),

  /** Receive guild message reaction events (MESSAGE_REACTION_*). */
  GUILD_MESSAGE_REACTIONS(10),

  /** Receive TYPING_START events in guilds. */
  GUILD_MESSAGE_TYPING(11),

  /** Receive DM message events. */
  DIRECT_MESSAGES(12),

  /** Receive DM message reaction events. */
  DIRECT_MESSAGE_REACTIONS(13),

  /** Receive TYPING_START events in DMs. */
  DIRECT_MESSAGE_TYPING(14),

  /** Receive full message content. Privileged. */
  MESSAGE_CONTENT(15, true),

  /** Receive guild scheduled event events (GUILD_SCHEDULED_EVENT_*). */
  GUILD_SCHEDULED_EVENTS(16),

  /** Receive auto-moderation rule configuration events. */
  AUTO_MODERATION_CONFIGURATION(20),

  /** Receive auto-moderation action execution events. */
  AUTO_MODERATION_EXECUTION(21),

  /** Receive poll vote events in guilds (MESSAGE_POLL_VOTE_ADD/REMOVE). */
  GUILD_MESSAGE_POLLS(24),

  /** Receive poll vote events in DMs (MESSAGE_POLL_VOTE_ADD/REMOVE). */
  DIRECT_MESSAGE_POLLS(25),
  UNKNOWN(-1);

  companion object {
    /**
     * Get the [GateWayIntent] from the given [value].
     *
     * @param value The value to get the [GateWayIntent] from.
     * @return The [GateWayIntent] from the given [value].
     */
    fun getValue(value: Int): GateWayIntent {
      return entries.firstOrNull { it.value == value } ?: UNKNOWN
    }

    /**
     * Calculates the intent value
     *
     * @param intents The intents to calculate
     * @return The intent value
     */
    fun calculateBitmask(intents: List<GateWayIntent>): Int {
      var bitmask = 0
      for (intentValue in intents) {
        bitmask += 1 shl intentValue.value
      }
      return bitmask
    }

    /**
     * The default intents for the gateway.
     *
     * @return The default intents for the gateway.
     */
    fun getDefaultIntents(): List<GateWayIntent> {
      return listOf(
        GUILDS,
        GUILD_MEMBERS,
        GUILD_MODERATION,
        GUILD_EXPRESSIONS,
        GUILD_INTEGRATIONS,
        GUILD_WEBHOOKS,
        GUILD_INVITES,
        GUILD_VOICE_STATES,
        GUILD_MESSAGES,
        GUILD_MESSAGE_REACTIONS,
        GUILD_MESSAGE_TYPING,
        DIRECT_MESSAGES,
        DIRECT_MESSAGE_REACTIONS,
        GUILD_SCHEDULED_EVENTS,
        AUTO_MODERATION_CONFIGURATION,
        AUTO_MODERATION_EXECUTION,
      )
    }
  }

  /**
   * Get the value of the [GateWayIntent].
   *
   * @return The value of the [GateWayIntent].
   */
  fun getValue(): Int {
    return value
  }

  /**
   * Whether the intent is privileged.
   *
   * @return Whether the intent is privileged.
   */
  fun privileged(): Boolean? {
    return privileged
  }
}
