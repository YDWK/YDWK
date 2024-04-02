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
package io.github.ydwk.yde.entities.interaction.button.creator.builder

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.button.PartialEmoji
import io.github.ydwk.yde.entities.interaction.button.creator.PartialEmojiCreator

class PartialEmojiCreatorBuilder(
    val yde: YDE,
    val name: String,
    val id: String,
    val json: ObjectNode = yde.objectNode
) : PartialEmojiCreator {
    private var animated: Boolean = false

    override fun setAnimated(animated: Boolean): PartialEmojiCreator {
        this.animated = animated
        return this
    }

    override fun create(): PartialEmoji {
        json.put("name", name)
        json.put("animated", animated)
        json.put("id", id)

        return yde.objectMapper.convertValue(json, PartialEmoji::class.java)
    }
}
