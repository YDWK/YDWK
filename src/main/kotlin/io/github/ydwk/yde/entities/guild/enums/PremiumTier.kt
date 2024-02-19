/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.entities.guild.enums

enum class PremiumTier(private val value: Int) {
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
        /**
         * The [PremiumTier] by its [value].
         *
         * @param value The value to get the [PremiumTier] by.
         * @return The [PremiumTier] by the given [value].
         */
        fun fromInt(value: Int): PremiumTier {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * The value of the [PremiumTier].
     *
     * @return The value of the [PremiumTier].
     */
    fun getValue(): Int {
        return value
    }
}
