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
package io.github.ydwk.yde.interaction.message.selectmenu

import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.reply.Repliable

interface SelectMenuInteraction : ComponentInteraction, Repliable {

    /**
     * The custom id of the select menu.
     *
     * @return the custom id of the select menu
     */
    val customId: String

    /**
     * The placeholder of the select menu.
     *
     * @return the placeholder of the select menu
     */
    val placeholder: String

    /**
     * The minimum number of options that must be selected.
     *
     * @return the minimum number of options that must be selected
     */
    val minValues: Int

    /**
     * The maximum number of options that can be selected.
     *
     * @return the maximum number of options that can be selected
     */
    val maxValues: Int
}
