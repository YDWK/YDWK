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

enum class NSFWLeveL(private val level: Int) {
    /** No NSFW content is allowed. */
    DEFAULT(0),

    /** NSFW content is allowed, but must be marked as such. */
    EXPLICIT(1),

    /** NSFW content is allowed without any special marking. */
    SAFE(2),

    /** NSFW content is not allowed. */
    AGE_RESTRICTED(3),

    /** An unknown NSFW level. */
    UNKNOWN(-1);

    companion object {
        /**
         * The [NSFWLeveL] for the provided [level].
         *
         * @param level The level to get the [NSFWLeveL] for.
         * @return The [NSFWLeveL] for the provided [level].
         */
        fun fromInt(level: Int): NSFWLeveL {
            return when (level) {
                0 -> DEFAULT
                1 -> EXPLICIT
                2 -> SAFE
                3 -> AGE_RESTRICTED
                else -> UNKNOWN
            }
        }
    }

    /**
     * The level of the [NSFWLeveL].
     *
     * @return The level of the [NSFWLeveL].
     */
    fun getLevel(): Int {
        return level
    }
}
