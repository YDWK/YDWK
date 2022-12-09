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
package io.github.ydwk.ydwk.interaction.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.impl.interaction.message.ActionRowImpl
import io.github.ydwk.ydwk.util.Checks

interface ActionRow {

    /**
     * Gets all the components in this action row.
     *
     * @return All the components in this action row.
     */
    val components: List<Component>

    /**
     * Gets the json representation of this action row.
     *
     * @return The json representation of this action row.
     */
    fun toJson(): JsonNode

    companion object {
        /**
         * Creates a new action row with the given components.
         * max 5 components per action row.
         *
         * @param components The components to add to the action row.
         * @return The new action row.
         */
        fun of(vararg components: Component): ActionRow {
            return of(components.toList())
        }

        /**
         * Creates a new action row with the given components.
         * max 5 components per action row.
         *
         * @param components The components to add to the action row.
         * @return The new action row.
         */
        fun of(components: List<Component>): ActionRow {
            Checks.customCheck(components.size <= 5, "Action row can only have 5 components.")
            return ActionRowImpl(components.toMutableList())
        }
    }
}
