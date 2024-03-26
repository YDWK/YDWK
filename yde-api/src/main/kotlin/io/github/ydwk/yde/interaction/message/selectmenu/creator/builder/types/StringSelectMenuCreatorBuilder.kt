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
package io.github.ydwk.yde.interaction.message.selectmenu.creator.builder.types

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.message.selectmenu.SelectMenuOption
import io.github.ydwk.yde.interaction.message.selectmenu.creator.builder.SelectMenuCreatorBuilder
import io.github.ydwk.yde.interaction.message.selectmenu.creator.types.StringSelectMenuCreator

data class StringSelectMenuCreatorBuilder(
    override val yde: YDE,
    override val customId: String,
    val options: List<SelectMenuOption>
) :
    StringSelectMenuCreator,
    SelectMenuCreatorBuilder(customId, ComponentType.STRING_SELECT_MENU, yde) {
    init {
        for (option in options) {
            objectNode.putArray("options").addObject().apply {
                put("label", option.label)
                put("value", option.value)
                put("description", option.description)
                set<JsonNode>("emoji", option.emoji?.json)
                put("default", option.default)
            }
        }
    }
}
