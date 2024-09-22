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
package io.github.ydwk.yde.entities.interaction

import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.message.ComponentType

interface Component : GenericEntity {
    /**
     * The type of this component.
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
}
