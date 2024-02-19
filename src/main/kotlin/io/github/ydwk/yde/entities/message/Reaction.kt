/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.entities.message

import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.util.GenericEntity

interface Reaction : GenericEntity {
    /**
     * The amount of times this reaction was used.
     *
     * @return The count of this reaction.
     */
    val count: Int

    /**
     * Gets whether the current user reacted with this emoji.
     *
     * @return Whether the current user reacted with this emoji.
     */
    val me: Boolean

    /**
     * The emoji of this reaction.
     *
     * @return The emoji of this reaction.
     */
    val emoji: Emoji
}
