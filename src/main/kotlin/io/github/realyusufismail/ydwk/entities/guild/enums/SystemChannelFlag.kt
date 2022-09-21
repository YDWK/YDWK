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
package io.github.realyusufismail.ydwk.entities.guild.enums

enum class SystemChannelFlag(val value: Int) {
    /** Suppress member join notifications. */
    SUPPRESS_JOIN_NOTIFICATIONS(1 shl 0),
    /** Suppress server boost notifications. */
    SUPPRESS_PREMIUM_SUBSCRIPTIONS(1 shl 1),
    /** Suppress server setup tips. */
    SUPPRESS_GUILD_REMINDER_NOTIFICATIONS(1 shl 2),
    /** Hide member join sticker reply buttons. */
    SUPPRESS_JOIN_NOTIFICATION_REPLIES(1 shl 3),
    /** An unknown system channel flag. */
    UNKNOWN(-1);

    companion object {
        /** Gets the [SystemChannelFlag] by its [value]. */
        fun fromValue(value: Int): SystemChannelFlag {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
