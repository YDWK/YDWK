/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.interaction.application

import io.github.realyusufismail.ydwk.entities.util.GenericEntity

interface ApplicationCommandOption : GenericEntity {
    /**
     * Gets the name of the option.
     *
     * @return The name of the option.
     */
    val name: String

    /**
     * Gets the type of the option.
     *
     * @return The type of the option.
     */
    val type: ApplicationCommandType

    /**
     * Gets the value as Any.
     *
     * @return The value as Any.
     */
    val value: Any?

    /**
     * Gets the value of the option as a string.
     *
     * @return The value of the option as a string.
     */
    val valueAsString: String?
        get() = value as? String

    /**
     * Gets the value of the option as a long.
     *
     * @return The value of the option as a long.
     */
    val valueAsLong: Long?
        get() = value as? Long

    /**
     * Gets the value of the option as an Int.
     *
     * @return The value of the option as an Int.
     */
    val valueAsInt: Int?
        get() = value as? Int

    /**
     * Gets the value of the option as a Double
     *
     * @return The value of the option as a Double
     */
    val valueAsDouble: Double?
        get() = value as? Double

    /**
     * Gets the options of the option.
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
