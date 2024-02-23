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
package io.github.ydwk.yde.impl.interaction.message.textinput

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.message.Embed
import io.github.ydwk.yde.impl.entities.MessageImpl
import io.github.ydwk.yde.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.yde.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.yde.impl.interaction.message.ComponentImpl
import io.github.ydwk.yde.interaction.application.sub.Reply
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.message.textinput.TextInput
import io.github.ydwk.yde.interaction.message.textinput.creator.TextInputCreator
import io.github.ydwk.yde.util.Checks
import io.github.ydwk.yde.util.GetterSnowFlake

class TextInputImpl(
    yde: YDE,
    json: JsonNode,
    interactionId: GetterSnowFlake,
    override val customId: String,
    override val style: TextInput.TextInputStyle,
    override val label: String,
    override val minLength: Int?,
    override val maxLength: Int?,
    override val required: Boolean?,
    override val initialValue: String?,
    override val placeholder: String?,
) : TextInput, ComponentInteractionImpl(yde, json, interactionId) {
    private val componentJson: JsonNode =
        MessageImpl(yde, json["message"], json["message"]["id"].asLong()).json

    constructor(
        componentInteractionImpl: ComponentInteractionImpl,
    ) : this(
        componentInteractionImpl.yde,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId)

    override fun reply(content: String): Reply {
        return ReplyImpl(yde, content, null, interactionId.asString, interactionToken)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(yde, null, embed, interactionId.asString, interactionToken)
    }

    data class TextInputCreatorImpl(
        val customId: String,
        val style: TextInput.TextInputStyle,
        val label: String,
        override val json: ObjectNode = JsonNodeFactory.instance.objectNode(),
    ) : TextInputCreator, ComponentImpl.ComponentCreator {
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

        override fun create(): ComponentImpl.ComponentCreator {
            json.put("type", ComponentType.TEXT_INPUT.getType())
            json.put("custom_id", customId)
            json.put("label", label)
            json.put("style", style.getValue())
            minLength?.let { json.put("min_length", it) }
            maxLength?.let { json.put("max_length", it) }
            required?.let { json.put("required", it) }
            initialValue?.let { json.put("initial_value", it) }
            placeholder?.let { json.put("placeholder", it) }
            return this
        }
    }
}
