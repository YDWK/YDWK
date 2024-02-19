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
package io.github.ydwk.yde.interaction.message.selectmenu.creator

import io.github.ydwk.yde.impl.interaction.message.selectmenu.SelectMenuImpl

interface SelectMenuCreator {
    /**
     * Sets the placeholder of the select menu.
     *
     * @param placeholder the placeholder of the select menu
     */
    fun setPlaceholder(placeholder: String): SelectMenuCreator

    /**
     * Sets the minimum number of values.
     *
     * @param minValues the minimum number of values
     */
    fun setMinValues(minValues: Int): SelectMenuCreator

    /**
     * Sets the maximum number of values.
     *
     * @param maxValues the maximum number of values
     */
    fun setMaxValues(maxValues: Int): SelectMenuCreator

    /**
     * Sets whether the select menu is disabled.
     *
     * @param disabled whether the select menu is disabled
     */
    fun setDisabled(disabled: Boolean): SelectMenuCreator

    /**
     * Creates the select menu.
     *
     * @return the select menu
     */
    fun create(): SelectMenuImpl.ISelectMenuCreator
}
