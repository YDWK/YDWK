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
package io.github.ydwk.yde.interaction.application.type

import io.github.ydwk.yde.builders.slash.SlashOptionGetter
import io.github.ydwk.yde.builders.slash.SlashOptionType
import io.github.ydwk.yde.interaction.application.ApplicationCommand
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.application.type.sub.SubCommand
import io.github.ydwk.yde.interaction.reply.Repliable

interface SlashCommand : ApplicationCommand, Repliable {
    /**
     * The type of the command.
     *
     * @return The type of the command.
     */
    override val type: ApplicationCommandType
        get() = ApplicationCommandType.CHAT_INPUT

    /**
     * The selected language of the invoking user
     *
     * @return the selected language of the invoking user
     */
    val locale: String?

    /**
     * Gets all the options of the command.
     *
     * @return All the options of the command.
     */
    val options: List<SlashOptionGetter>

    /**
     * Gets all the subcommands of the command. (They are a type of option)
     *
     * @return All the subcommands of the command.
     */
    val subcommands: List<SubCommand>
        get() {
            val subcommands = mutableListOf<SubCommand>()
            for (option in options) {
                if (option.type == SlashOptionType.SUB_COMMAND) {
                    subcommands.add(option.asSubCommand)
                }
            }
            return subcommands
        }

    /**
     * Gets the triggered subcommand name.
     *
     * @return The triggered subcommand name.
     */
    val triggeredSubcommandName: String?
        get() {
            for (option in options) {
                if (option.type == SlashOptionType.SUB_COMMAND) {
                    return option.name
                }
            }
            return null
        }

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

    /**
     * The option with the specified name.
     *
     * @param name The name of the option.
     * @param resolver The resolver to use.
     * @param default The default value to return if the option is not present.
     */
    fun <T> getOption(name: String, resolver: (SlashOptionGetter) -> T, default: T): T {
        val option = getOption(name)
        return if (option == null) default else resolver(option)
    }
}
