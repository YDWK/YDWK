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