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
package io.github.ydwk.yde.interaction.message.textinput

import io.github.ydwk.yde.impl.interaction.message.textinput.TextInputImpl
import io.github.ydwk.yde.interaction.ComponentInteraction
import io.github.ydwk.yde.interaction.message.textinput.creator.TextInputCreator
import io.github.ydwk.yde.interaction.reply.Repliable

interface TextInput : ComponentInteraction, Repliable {
    /**
     * The custom id of the select menu.
     *
     * @return the custom id of the select menu
     */
    val customId: String

    /**
     * The text input style.
     *
     * @return the text input style
     */
    val style: TextInputStyle

    /**
     * The label of the text input.
     *
     * @return the label of the text input
     */
    val label: String

    /**
     * The minimum length of the text input.
     *
     * @return the minimum length of the text input
     */
    val minLength: Int?

    /**
     * The maximum length of the text input.
     *
     * @return the maximum length of the text input
     */
    val maxLength: Int?

    /**
     * Whether the text input is required.
     *
     * @return whether the text input is required
     */
    val required: Boolean?

    /**
     * The pre-filled value for this component.
     *
     * @return the pre-filled value for this component
     */
    val initialValue: String?

    /**
     * The placeholder for this component.
     *
     * @return the placeholder for this component
     */
    val placeholder: String?

    enum class TextInputStyle(private val value: Int) {
        /** A single-line text input. */
        SHORT(1),

        /** A multi-line text input. */
        PARAGRAPH(2);

        companion object {
            /**
             * Gets the [TextInputStyle] from the provided value.
             *
             * @param value the value of the style
             * @return the [TextInputStyle] from the provided value
             */
            fun fromValue(value: Int): TextInputStyle {
                return values().first { it.value == value }
            }
        }

        fun getValue(): Int {
            return value
        }
    }

    companion object {
        /**
         * Create a new [TextInput].
         *
         * @param customId the custom id of the text input
         * @param style the text input style
         * @param label the label of the text input
         * @return [TextInputCreator] to construct the text input
         */
        operator fun invoke(
            customId: String,
            style: TextInputStyle,
            label: String
        ): TextInputCreator {
            return TextInputImpl.TextInputCreatorImpl(customId, style, label)
        }
    }
}
