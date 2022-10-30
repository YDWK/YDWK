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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk

enum class GateWayIntent(var value: Int, var privileged: Boolean? = false) {
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
        fun from(value: Int): GateWayIntent {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }

        /**
         * Calculates the intent value
         *
         * @param intents The intents to calculate
         */
        fun calculateBitmask(intents: List<GateWayIntent>): Int {
            var bitmask = 0
            for (intentValue in intents) {
                bitmask += 1 shl intentValue.value
            }
            return bitmask
        }

        /** Gets the default intents for the gateway. */
        fun getDefaultIntents(): List<GateWayIntent> {
            return values().filter { it.privileged == false }
        }
    }

    override fun toString(): String {
        return value.toString()
    }
}
