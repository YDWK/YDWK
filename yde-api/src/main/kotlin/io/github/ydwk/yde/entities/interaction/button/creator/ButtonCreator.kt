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
package io.github.ydwk.yde.entities.interaction.button.creator

import io.github.ydwk.yde.entities.interaction.button.Button
import io.github.ydwk.yde.entities.interaction.button.PartialEmoji

interface ButtonCreator {
    /**
     * Sets the custom id of the button.
     *
     * @param customId The custom id of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setCustomId(customId: String): ButtonCreator

    /**
     * Sets the label of the button.
     *
     * @param label The label of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setLabel(label: String): ButtonCreator

    /**
     * Sets the emoji of the button.
     *
     * @param emoji The emoji of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setEmoji(emoji: PartialEmoji): ButtonCreator

    /**
     * Sets the url of the button.
     *
     * @param url The url of the button.
     * @return This [ButtonCreator] for chaining.
     */
    fun setUrl(url: String): ButtonCreator

    /**
     * Creates a Button.
     *
     * @return The created Button.
     */
    fun create(): Button
}
