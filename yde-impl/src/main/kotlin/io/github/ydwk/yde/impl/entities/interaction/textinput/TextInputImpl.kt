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
package io.github.ydwk.yde.impl.entities.interaction.textinput

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.textinput.TextInput
import io.github.ydwk.yde.impl.entities.interaction.ComponentImpl

class TextInputImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val customId: String,
    override val style: TextInput.TextInputStyle,
    override val label: String,
    override val minLength: Int?,
    override val maxLength: Int?,
    override val required: Boolean?,
    override val initialValue: String?,
    override val placeholder: String?
) : TextInput, ComponentImpl(yde.entityInstanceBuilder.buildComponent(json))