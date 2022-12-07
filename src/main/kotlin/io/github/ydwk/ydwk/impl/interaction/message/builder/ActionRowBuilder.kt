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
package io.github.ydwk.ydwk.impl.interaction.message.builder

import io.github.ydwk.ydwk.impl.interaction.message.ActionRowImpl
import io.github.ydwk.ydwk.impl.interaction.message.button.Button
import io.github.ydwk.ydwk.impl.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.ydwk.interaction.message.ActionRow
import io.github.ydwk.ydwk.interaction.message.Component

class ActionRowBuilder {
    private val components: MutableList<Component> = mutableListOf()

    /**
     * Add a button to the action row.
     *
     * @param button The button to add.
     * @return The action row builder.
     */
    fun addButton(button: Button): ActionRowBuilder {
        components.add(button)
        return this
    }

    /**
     * Add a select menu to the action row.
     *
     * @param selectMenu The select menu to add.
     * @return The action row builder.
     */
    fun addSelectMenu(selectMenu: SelectMenu): ActionRowBuilder {
        components.add(selectMenu)
        return this
    }

    /**
     * Build the action row.
     *
     * @return The action row.
     */
    fun build(): ActionRow {
        return ActionRowImpl(components)
    }
}
