/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.realyusufismail.ydwk.ws.util

enum class GateWayIntent(final var value: Int, final var privileged: Boolean) {
    GUILDS(0, false),
    GUILD_MEMBERS(1, true),
    GUILD_BANS(2, false),
    GUILD_EMOJIS_AND_STICKERS(3, false),
    GUILD_INTEGRATIONS(4, false),
    GUILD_WEBHOOKS(5, false),
    GUILD_INVITES(6, false),
    GUILD_VOICE_STATES(7, false),
    GUILD_PRESENCES(8, true),
    GUILD_MESSAGES(9, false),
    GUILD_MESSAGE_REACTIONS(10, false),
    GUILD_MESSAGE_TYPING(11, false),
    DIRECT_MESSAGES(12, false),
    DIRECT_MESSAGE_REACTIONS(13, false),
    DIRECT_MESSAGE_TYPING(14, false),
    MESSAGE_CONTENT(15, true),
    GUILD_SCHEDULED_EVENTS(16, false),
    AUTO_MODERATION_CONFIGURATION(20, false),
    CHANNEL_STATS(21, false),
    UNKNOWN(-1, false);

    companion object {
        fun from(value: Int): GateWayIntent {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }

        fun calculateBitmask(intents: List<GateWayIntent>): Int {
            var bitmask = 0
            for (intentValue in intents) {
                bitmask += 1 shl intentValue.value
            }
            return bitmask
        }

        fun getAllIntents(): List<GateWayIntent> {
            return listOf(
                GUILDS,
                GUILD_BANS,
                GUILD_EMOJIS_AND_STICKERS,
                GUILD_INTEGRATIONS,
                GUILD_INVITES,
                GUILD_VOICE_STATES,
                GUILD_MESSAGES,
                GUILD_MESSAGE_REACTIONS,
                DIRECT_MESSAGES,
                DIRECT_MESSAGE_REACTIONS,
                GUILD_SCHEDULED_EVENTS,
                AUTO_MODERATION_CONFIGURATION,
                CHANNEL_STATS)
        }
    }

    override fun toString(): String {
        return value.toString()
    }
}
