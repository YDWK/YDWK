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
package io.github.ydwk.yde.entities.interaction.button

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.ButtonStyle
import io.github.ydwk.yde.entities.interaction.Component
import io.github.ydwk.yde.entities.interaction.button.creator.ButtonCreator
import io.github.ydwk.yde.entities.interaction.button.creator.builder.ButtonCreatorBuilder
import java.net.URL

interface Button : Component {

    /**
     * The button style of this button.
     *
     * @return The button style of this button.
     */
    val style: ButtonStyle

    /**
     * The label of this button.
     *
     * @return The label of this button.
     */
    val label: String?

    /**
     * The custom id of this button.
     *
     * @return The custom id of this button.
     */
    val customId: String?

    /**
     * The partial emoji of this button (name, id, and animated).
     *
     * @return The emoji of this button.
     */
    val emoji: PartialEmoji?

    /**
     * The url of this button if it is a link button.
     *
     * @return The url of this button if it is a link button.
     */
    val url: URL?

    /**
     * The disabled state of this button.
     *
     * @return The disabled state of this button.
     */
    val disabled: Boolean

    companion object {
        /**
         * Creates a [ButtonCreator] for a button with the given [style].
         *
         * @param style The style of the button.
         * @return The [ButtonCreator] for chaining.
         */
        fun createButton(
            yde: YDE,
            style: ButtonStyle,
        ): ButtonCreator {
            return ButtonCreatorBuilder(style, yde)
        }
    }
}
