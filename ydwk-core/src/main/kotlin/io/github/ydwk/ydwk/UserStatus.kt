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

/** Sets the user status. */
enum class UserStatus(private val status: String) {
    ONLINE("online"),

    /** Do not disturb. */
    DND("dnd"),

    /** Afk. */
    IDLE("idle"),

    /** Invisible and shown as offline. */
    INVISIBLE("invisible"),
    OFFLINE("offline");

    companion object {
        /**
         * The UserStatus from the status string.
         *
         * @param status The status string.
         * @return The UserStatus.
         */
        fun fromString(status: String): UserStatus {
            return when (status) {
                "online" -> ONLINE
                "dnd" -> DND
                "idle" -> IDLE
                "invisible" -> INVISIBLE
                else -> OFFLINE
            }
        }
    }

    /**
     * The status string.
     *
     * @return The status string.
     */
    fun getStatus(): String {
        return status
    }
}
