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
package io.github.ydwk.yde.entities.guild.schedule

enum class ScheduledEventStatus(private val value: Int) {
    /** The scheduled event has not started yet. */
    SCHEDULED(1),

    /** The scheduled event has started. */
    ACTIVE(2),

    /** The scheduled event has ended. */
    COMPLETED(3),

    /** The scheduled event has been cancelled. */
    CANCELLED(4);

    companion object {
        /**
         * The status from the value.
         *
         * @param value the value.
         * @return the status.
         */
        fun getValue(value: Int): ScheduledEventStatus {
            return values().first { it.value == value }
        }
    }

    /**
     * The value of the status.
     *
     * @return the value of the status.
     */
    fun getValue(): Int {
        return value
    }
}
