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
package io.github.ydwk.yde.interaction.message.button

import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.impl.interaction.message.ComponentImpl
import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.reply.Repliable
import io.github.ydwk.yde.util.Checks
import java.net.URL

interface Button : ComponentInteraction, Repliable {

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
     * The emoji of this button.
     *
     * @return The emoji of this button.
     */
    val emoji: Emoji?

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
         * Creates a new [Button] with the specified [style], [customId] and [label].
         *
         * @param style The style of the button.
         * @param customId The custom id of the button.
         * @param label The label of the button. (Max 80 characters)
         * @return [ComponentImpl.ComponentCreator] which contains the json representation of the
         *   button.
         */
        operator fun invoke(
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
         * Creates a new [Button] with the specified, [label] and [url] (for [ButtonStyle.LINK]).
         *
         * @param label The label of the button. (Max 80 characters)
         * @param url The url of the button.
         * @return [ComponentImpl.ComponentCreator] which contains the json representation of the
         *   button.
         */
        operator fun invoke(label: String?, url: String): ComponentImpl.ComponentCreator {
            Checks.customCheck(
                label != null && label.length <= 80,
                "Label must be between 1 and 80 characters long.")
            Checks.customCheck(URL(url).protocol == "https", "Url must be a valid https url.")
            return ComponentImpl.ButtonCreator(ButtonStyle.LINK, null, label, url)
        }
    }
}
