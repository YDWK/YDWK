/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.entities.interaction.actionrow

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.interaction.actionrow.creator.ActionRowCreator
import io.github.ydwk.yde.entities.interaction.actionrow.creator.builder.ActionRowCreatorBuilder
import io.github.ydwk.yde.util.Checks

interface ActionRow : Component {

    /**
     * Gets all the components in this action row.
     *
     * @return All the components in this action row.
     */
    val components: List<Component>

    companion object {
        /**
         * Creates a new action row with the given components. max 5 components per action row.
         *
         * Example:
         * ```
         *
         * val actionRow = ActionRow.createActionRow(Button.createButton(ButtonStyle.PRIMARY, "1", "Primary"))
         *
         * it.slash
         *        .reply("This is a button test!")
         *        .addActionRow(actionRow)
         *        .reply()
         * ```
         *
         * @param components The components to add to the action row.
         * @return [ActionRowCreator] used to create the action row.
         */
        fun createActionRow(yde: YDE, vararg components: Component): ActionRowCreator {
            return createActionRow(yde, components.toList())
        }

        fun createActionRow(yde: YDE, components: List<Component>): ActionRowCreator {
            Checks.customCheck(components.size <= 5, "Action row can only have 5 components.")
            return ActionRowCreatorBuilder(yde, components)
        }
    }
}
