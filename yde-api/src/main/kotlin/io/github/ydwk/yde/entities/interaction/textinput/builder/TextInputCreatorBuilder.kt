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
package io.github.ydwk.yde.entities.interaction.textinput.builder

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.entities.interaction.textinput.TextInput
import io.github.ydwk.yde.entities.interaction.textinput.creator.TextInputCreator
import io.github.ydwk.yde.util.Checks

data class TextInputCreatorBuilder(
    val customId: String,
    val style: TextInput.TextInputStyle,
    val label: String,
    override val yde: YDE,
) : TextInputCreator, Component.ComponentCreator {
    private var minLength: Int? = null
    private var maxLength: Int? = null
    private var required: Boolean? = null
    private var initialValue: String? = null
    private var placeholder: String? = null

    override fun setMinLength(min: Int): TextInputCreator {
        Checks.checkLength(min.toString(), 0, 4000, "min")
        minLength = min
        return this
    }

    override fun setMaxLength(max: Int): TextInputCreator {
        Checks.checkLength(max.toString(), 1, 4000, "max")
        maxLength = max
        return this
    }

    override fun setRequired(required: Boolean): TextInputCreator {
        this.required = required
        return this
    }

    override fun setInitialValue(value: String): TextInputCreator {
        Checks.checkLength(value, 1, 4000, "value")
        initialValue = value
        return this
    }

    override fun setPlaceholder(placeholder: String): TextInputCreator {
        Checks.checkLength(placeholder, 1, 100, "placeholder")
        this.placeholder = placeholder
        return this
    }

    override fun create(): Component.ComponentCreator {
        objectNode.put("type", ComponentType.TEXT_INPUT.getType())
        objectNode.put("custom_id", customId)
        objectNode.put("label", label)
        objectNode.put("style", style.getValue())
        minLength?.let { objectNode.put("min_length", it) }
        maxLength?.let { objectNode.put("max_length", it) }
        required?.let { objectNode.put("required", it) }
        initialValue?.let { objectNode.put("initial_value", it) }
        placeholder?.let { objectNode.put("placeholder", it) }
        return this
    }
}
