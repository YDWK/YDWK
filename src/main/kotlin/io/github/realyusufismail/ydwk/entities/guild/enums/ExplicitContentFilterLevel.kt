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

enum class ExplicitContentFilterLevel(val value: Int) {
    /** Media content will not be scanned */
    DISABLED(0),
    /** Media content sent by members without roles will be scanned */
    MEMBERS_WITHOUT_ROLES(1),
    /** Media content sent by all members will be scanned */
    ALL_MEMBERS(2),
    /** An unknown explicit content filter level */
    UNKNOWN(-1);

    companion object {
        /** Gets the [ExplicitContentFilterLevel] for the given [value]. */
        fun fromValue(value: Int): ExplicitContentFilterLevel {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
