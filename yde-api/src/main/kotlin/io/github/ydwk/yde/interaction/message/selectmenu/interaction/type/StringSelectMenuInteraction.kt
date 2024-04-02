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
package io.github.ydwk.yde.interaction.message.selectmenu.interaction.type

import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuOption
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuInteraction

interface StringSelectMenuInteraction : SelectMenuInteraction {
    /**
     * The selected options that the user selected.
     *
     * @return the selected options that the user selected
     */
    val selectedOptions: List<SelectMenuOption>

    /**
     * The possible options that the user can select.
     *
     * @return the possible options that the user can select
     */
    val options: List<SelectMenuOption>
}
