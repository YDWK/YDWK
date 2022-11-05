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
package io.github.ydwk.ydwk.entities.guild.enums

enum class PremiumTier(val value: Int) {
    /** Guild has not unlocked any Server Boost perks. */
    NONE(0),

    /** Guild has unlocked Server Boost level 1 perks. */
    TIER_1(1),

    /** Guild has unlocked Server Boost level 2 perks. */
    TIER_2(2),

    /** Guild has unlocked Server Boost level 3 perks. */
    TIER_3(3),

    /** An unknown premium tier. */
    UNKNOWN(-1);

    companion object {
        /** Gets the [PremiumTier] by its [value]. */
        fun fromValue(value: Int): PremiumTier {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
