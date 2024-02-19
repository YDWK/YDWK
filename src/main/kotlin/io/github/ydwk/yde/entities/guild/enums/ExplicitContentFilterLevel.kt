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

enum class ExplicitContentFilterLevel(private val value: Int) {
    /** Media content will not be scanned. */
    DISABLED(0),

    /** Media content sent by members without roles will be scanned. */
    MEMBERS_WITHOUT_ROLES(1),

    /** Media content sent by all members will be scanned. */
    ALL_MEMBERS(2),

    /** An unknown explicit content filter level. */
    UNKNOWN(-1);

    companion object {
        /**
         * The [ExplicitContentFilterLevel] for the given [value].
         *
         * @param value The value to get the [ExplicitContentFilterLevel] for.
         * @return The [ExplicitContentFilterLevel] for the given [value].
         */
        fun fromInt(value: Int): ExplicitContentFilterLevel {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * The value of the [ExplicitContentFilterLevel].
     *
     * @return The value of the [ExplicitContentFilterLevel].
     */
    fun getValue(): Int {
        return value
    }
}
