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

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuOption
import io.github.ydwk.yde.interaction.message.selectmenu.creator.types.SelectMenuOptionCreator

class SelectMenuOptionCreatorBuilder(val yde: YDE) : SelectMenuOptionCreator {
    private var label: String? = null
    private var value: String? = null
    private var description: String? = null
    private var emoji: Emoji? = null
    private var default: Boolean = false

    override fun setLabel(label: String): SelectMenuOptionCreator {
        this.label = label
        return this
    }

    override fun setValue(value: String): SelectMenuOptionCreator {
        this.value = value
        return this
    }

    override fun setDescription(description: String): SelectMenuOptionCreator {
        this.description = description
        return this
    }

    override fun setEmoji(emoji: Emoji): SelectMenuOptionCreator {
        this.emoji = emoji
        return this
    }

    override fun setDefault(default: Boolean): SelectMenuOptionCreator {
        this.default = default
        return this
    }

    override fun create(): SelectMenuOption {
        val json = yde.objectNode.objectNode()
        json.put("label", label)
        json.put("value", value)
        json.put("description", description)
        if (emoji != null) {
            json.set<ObjectNode>("emoji", emoji!!.json)
        }
        json.put("default", default)

        return yde.entityInstanceBuilder.buildStringSelectMenuOption(json)
    }
}
