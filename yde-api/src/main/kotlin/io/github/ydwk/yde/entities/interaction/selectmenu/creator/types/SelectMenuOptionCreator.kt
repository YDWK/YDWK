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
package io.github.ydwk.yde.entities.interaction.selectmenu.creator.types

import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuOption

interface SelectMenuOptionCreator {

    /**
     * Sets the label for the select menu option.
     *
     * @param label the label for the select menu option
     * @return the [SelectMenuOptionCreator]
     */
    fun setLabel(label: String): SelectMenuOptionCreator

    /**
     * Sets the value for the select menu option.
     *
     * @param value the value for the select menu option
     * @return the [SelectMenuOptionCreator]
     */
    fun setValue(value: String): SelectMenuOptionCreator

    /**
     * Sets the description for the select menu option.
     *
     * @param description the description for the select menu option
     * @return the [SelectMenuOptionCreator]
     */
    fun setDescription(description: String): SelectMenuOptionCreator

    /**
     * Sets the emoji for the select menu option.
     *
     * @param emoji the emoji for the select menu option
     * @return the [SelectMenuOptionCreator]
     */
    fun setEmoji(emoji: Emoji): SelectMenuOptionCreator

    /**
     * Sets this select menu option as the default.
     *
     * @param default whether this select menu option is the default
     * @return the [SelectMenuOptionCreator]
     */
    fun setDefault(default: Boolean): SelectMenuOptionCreator

    /**
     * Creates the select menu option.
     *
     * @return the created select menu option
     */
    fun create(): SelectMenuOption
}
