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
package io.github.ydwk.yde.interaction.message.textinput.creator

import io.github.ydwk.yde.impl.interaction.message.ComponentImpl

interface TextInputCreator {

    /**
     * Sets the minimum length of the text input.
     *
     * @param min The minimum length of the text input. max 4000
     * @return The current [TextInputCreator] instance.
     */
    fun setMinLength(min: Int): TextInputCreator

    /**
     * Sets the maximum length of the text input. max 4000
     *
     * @param max The maximum length of the text input.
     * @return The current [TextInputCreator] instance.
     */
    fun setMaxLength(max: Int): TextInputCreator

    /**
     * Sets weather the text input is required.
     *
     * @param required Whether the text input is required.
     * @return The current [TextInputCreator] instance.
     */
    fun setRequired(required: Boolean): TextInputCreator

    /**
     * Sets the pre-filled value for this component; max 4000 characters
     *
     * @param value The pre-filled value for this component.
     * @return The current [TextInputCreator] instance.
     */
    fun setInitialValue(value: String): TextInputCreator

    /**
     * Sets the placeholder for this component; max 4000 characters
     *
     * @param placeholder The placeholder for this component.
     * @return The current [TextInputCreator] instance.
     */
    fun setPlaceholder(placeholder: String): TextInputCreator

    /**
     * Creates the text input.
     *
     * @return The text input.
     */
    fun create(): ComponentImpl.ComponentCreator
}
