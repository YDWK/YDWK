/*
 * Copyright 2022 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.interaction.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.ydwk.ydwk.impl.interaction.message.button.Button
import io.github.ydwk.ydwk.impl.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.ydwk.interaction.message.ActionRow
import io.github.ydwk.ydwk.interaction.message.Component

class ActionRowImpl(components: MutableList<Component>) : ActionRow {

    override val components: List<Component> = components

    override fun toJson(): JsonNode {
        val json = ObjectMapper().createObjectNode()
        components.forEach {
            when (it) {
                is Button -> {}
                is SelectMenu -> {}
            }
        }

        return json
    }
}
