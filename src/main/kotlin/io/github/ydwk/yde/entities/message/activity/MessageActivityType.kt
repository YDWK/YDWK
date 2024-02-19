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
package io.github.ydwk.yde.entities.message.activity

enum class MessageActivityType(private val value: Int) {
    JOIN(1),
    SPECTATE(2),
    LISTEN(3),
    JOIN_REQUEST(5);

    companion object {
        /**
         * The [MessageActivityType] from the provided [value].
         *
         * @param value The value of the [MessageActivityType].
         * @return The [MessageActivityType] corresponding to the provided [value].
         */
        fun fromInt(value: Int): MessageActivityType {
            return values().first { it.value == value }
        }
    }

    /**
     * The value of the [MessageActivityType].
     *
     * @return The value of the [MessageActivityType].
     */
    fun getValue(): Int {
        return value
    }
}
