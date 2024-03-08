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
package io.github.ydwk.yde.interaction.message.selectmenu.creator.builder

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.yde.interaction.message.selectmenu.creator.SelectMenuCreator

open class SelectMenuCreatorBuilder(
    open val customId: String,
    private val componentType: ComponentType,
    override val json: ObjectNode = JsonNodeFactory.instance.objectNode(),
) : SelectMenu.ISelectMenuCreator, SelectMenuCreator {
    private var placeholder: String? = null
    private var minValues: Int? = null
    private var maxValues: Int? = null
    private var disabled: Boolean = false

    override fun setPlaceholder(placeholder: String): SelectMenuCreator {
        this.placeholder = placeholder
        return this
    }

    override fun setMinValues(minValues: Int): SelectMenuCreator {
        this.minValues = minValues
        return this
    }

    override fun setMaxValues(maxValues: Int): SelectMenuCreator {
        this.maxValues = maxValues
        return this
    }

    override fun setDisabled(disabled: Boolean): SelectMenuCreator {
        this.disabled = disabled
        return this
    }

    override fun create(): SelectMenu.ISelectMenuCreator {
        json.put("type", componentType.getType())
        json.put("custom_id", customId)
        if (placeholder != null) json.put("placeholder", placeholder)
        if (minValues != null) json.put("min_values", minValues)
        if (maxValues != null) json.put("max_values", maxValues)
        json.put("disabled", disabled)
        return this
    }
}
