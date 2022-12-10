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
package io.github.ydwk.ydwk.interaction.message

import io.github.ydwk.ydwk.interaction.sub.GenericCommandData
import io.github.ydwk.ydwk.util.SnowFlake

interface Component : SnowFlake, GenericCommandData {

    /**
     * Gets the type of this component.
     *
     * @return The type of this component.
     */
    val type: ComponentType

    /**
     * Weather this component is compatible with a message.
     *
     * @return Weather this component is compatible with a message.
     */
    val messageCompatible: Boolean

    /**
     * Weather this component is compatible with a modal.
     *
     * @return Weather this component is compatible with a modal.
     */
    val modalCompatible: Boolean

    /**
     * Gets the custom id of this button if it is not a link button.
     *
     * @return The custom id of this button if it is not a link button.
     */
    val customId: String?

    /**
     * Gets a list of [Component]s that are children of this component.
     *
     * @return A list of [Component]s that are children of this component.
     */
    val children: List<Component>
}
