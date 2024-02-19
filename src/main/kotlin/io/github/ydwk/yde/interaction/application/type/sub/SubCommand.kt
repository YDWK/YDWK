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
package io.github.ydwk.yde.interaction.application.type.sub

import io.github.ydwk.yde.builders.slash.SlashOptionGetter
import io.github.ydwk.yde.builders.slash.SlashOptionType
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.NameAbleEntity

interface SubCommand : NameAbleEntity, GenericEntity {
    /**
     * Gets all the options of the command.
     *
     * @return All the options of the command.
     */
    val options: List<SlashOptionGetter>

    /**
     * Gets all the options with the specified name.
     *
     * @param name The name of the option.
     * @return All the options with the specified name.
     */
    fun getOptionsByName(name: String): List<SlashOptionGetter> {
        return options.filter { it.name == name }
    }

    /**
     * The first option with the specified type.
     *
     * @param type The type of the option.
     * @return The first option with the specified type.
     */
    fun getOptionsByType(type: SlashOptionType): List<SlashOptionGetter> {
        return options.filter { it.type == type }
    }

    /**
     * The first option with the specified name.
     *
     * @param name The name of the option.
     * @return The first option with the specified name.
     */
    fun getOption(name: String): SlashOptionGetter? {
        val options = getOptionsByName(name)
        return if (options.isEmpty()) null else options[0]
    }

    /**
     * The option with the specified name.
     *
     * @param name The name of the option.
     * @param resolver The resolver to use.
     * @return The option with the specified name.
     */
    fun <T> getOption(name: String, resolver: (SlashOptionGetter) -> T): T? {
        val option = getOption(name)
        return if (option == null) null else resolver(option)
    }
}
