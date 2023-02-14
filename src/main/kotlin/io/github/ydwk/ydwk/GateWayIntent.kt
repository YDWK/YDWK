/*
 * Copyright 2022 YDWK inc.
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
    GUILD_MEMBERS(1),
    GUILD_BANS(2),
    GUILD_WEBHOOKS(5),
    GUILD_INVITES(6),
    GUILD_VOICE_STATES(7),
    GUILD_PRESENCES(8, true),
    GUILD_MESSAGES(9),
    GUILD_MESSAGE_REACTIONS(10),
    GUILD_MESSAGE_TYPING(11),
    DIRECT_MESSAGES(12),
    DIRECT_MESSAGE_REACTIONS(13),
    DIRECT_MESSAGE_TYPING(14),
    MESSAGE_CONTENT(15, true),
    AUTO_MODERATION_CONFIGURATION(20),
    AUTO_MODERATION_EXECUTION(21),
    UNKNOWN(-1);

    companion object {
        /**
         * Get the [GateWayIntent] from the given [value].
         *
         * @param value The value to get the [GateWayIntent] from.
         * @return The [GateWayIntent] from the given [value].
         */
        fun fromInt(value: Int): GateWayIntent {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
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
                GUILD_MEMBERS,
                GUILD_BANS,
                GUILD_WEBHOOKS,
                GUILD_INVITES,
                GUILD_VOICE_STATES,
                GUILD_MESSAGES,
                GUILD_MESSAGE_REACTIONS,
                GUILD_MESSAGE_TYPING,
                DIRECT_MESSAGES,
                DIRECT_MESSAGE_REACTIONS,
                AUTO_MODERATION_CONFIGURATION,
                AUTO_MODERATION_EXECUTION)
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
