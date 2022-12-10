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
package io.github.ydwk.ydwk.interaction.message.button

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.impl.interaction.message.ComponentImpl
import io.github.ydwk.ydwk.interaction.message.Component
import io.github.ydwk.ydwk.util.Checks
import java.net.URL

interface Button : Component {

    /**
     * Gets the url of this button if it is a link button.
     *
     * @return The url of this button if it is a link button.
     */
    val url: URL?

    /**
     * Gets the label of this button.
     *
     * @return The label of this button.
     */
    val label: String?

    /**
     * Gets the button style of this button.
     *
     * @return The button style of this button.
     */
    val style: ButtonStyle

    companion object {
        /**
         * Creates a new [Button] with the specified [style], [customId] and [label].
         *
         * @param ydwk The [YDWK] instance.
         * @param style The style of the button.
         * @param customId The custom id of the button.
         * @param label The label of the button. (Max 80 characters)
         * @return an empty [ComponentImpl.ComponentCreator].
         */
        fun of(
            style: ButtonStyle,
            customId: String,
            label: String?
        ): ComponentImpl.ComponentCreator {
            Checks.customCheck(
                label != null && label.length <= 80,
                "Label must be between 1 and 80 characters long.")
            return ComponentImpl.ButtonCreator(style, customId, label)
        }

        /**
         * Creates a new [Button] with the specified, [label] and [link] (for [ButtonStyle.LINK]).
         *
         * @param label The label of the button. (Max 80 characters)
         * @param link The link of the button.
         * @return an empty [ComponentImpl.ComponentCreator].
         */
        fun of(ydwk: YDWK, label: String?, link: String): ComponentImpl.ComponentCreator {
            Checks.customCheck(
                label != null && label.length <= 80,
                "Label must be between 1 and 80 characters long.")
            return ComponentImpl.ButtonCreator(ButtonStyle.LINK, label, link)
        }
    }
}
