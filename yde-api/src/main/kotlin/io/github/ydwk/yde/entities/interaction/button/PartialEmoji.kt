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
package io.github.ydwk.yde.entities.interaction.button

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.button.creator.PartialEmojiCreator
import io.github.ydwk.yde.entities.interaction.button.creator.builder.PartialEmojiCreatorBuilder
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.SnowFlake

interface PartialEmoji : SnowFlake, GenericEntity {
    /**
     * The name of the emoji.
     *
     * @return The name of the emoji.
     */
    val name: String

    /**
     * Whether the emoji is animated.
     *
     * @return Whether the emoji is animated.
     */
    val animated: Boolean

    companion object {
        /**
         * Creates a [PartialEmojiCreator] for a partial emoji with the given [name].
         *
         * @param name The name of the emoji.
         * @param id The id of the emoji.
         * @param yde The YDE instance.
         * @return The created [PartialEmojiCreator].
         */
        fun create(yde: YDE, name: String, id: String): PartialEmojiCreator {
            return PartialEmojiCreatorBuilder(yde, name, id)
        }
    }
}
