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
package io.github.ydwk.yde.interaction.application

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.builders.slash.SlashOptionType
import io.github.ydwk.yde.entities.util.GenericEntity

interface ApplicationCommandOption : GenericEntity {
    /**
     * The name of the option.
     *
     * @return The name of the option.
     */
    var name: String

    /**
     * The type of the option.
     *
     * @return The type of the option.
     */
    val type: SlashOptionType

    /**
     * The value as Any.
     *
     * @return The value as Any.
     */
    val value: JsonNode

    /**
     * The options of the option.
     *
     * @return The options of the option.
     */
    val options: List<ApplicationCommandOption>

    /**
     * Gets weather this option is the currently focused option for autocomplete.
     *
     * @return Weather this option is the currently focused option for autocomplete.
     */
    val focused: Boolean?
}
