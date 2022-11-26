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
package io.github.ydwk.ydwk.interaction.application.type

import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.interaction.application.ApplicationCommand
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandType
import io.github.ydwk.ydwk.interaction.application.sub.Reply
import io.github.ydwk.ydwk.slash.SlashOptionGetter
import io.github.ydwk.ydwk.slash.SlashOptionType

interface SlashCommand : ApplicationCommand {
    /**
     * Gets the type of the command.
     *
     * @return The type of the command.
     */
    override val type: ApplicationCommandType
        get() = ApplicationCommandType.CHAT_INPUT

    /**
     * Gets the selected language of the invoking user
     *
     * @return the selected language of the invoking user
     */
    val locale: String?

    /**
     * Replies to an interaction.
     *
     * @param content The content of the reply.
     * @return The reply.
     */
    fun reply(content: String): Reply

    /**
     * Replies to an interaction.
     *
     * @param embed The embed of the reply.
     * @return The reply.
     */
    fun reply(
        embed: Embed,
    ): Reply

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
     * Gets the first option with the specified type.
     *
     * @param type The type of the option.
     * @return The first option with the specified type.
     */
    fun getOptionsByType(type: SlashOptionType): List<SlashOptionGetter> {
        return options.filter { it.type == type }
    }

    /**
     * Gets the first option with the specified name.
     *
     * @param name The name of the option.
     * @return The first option with the specified name.
     */
    fun getOption(name: String): SlashOptionGetter? {
        val options = getOptionsByName(name)
        return if (options.isEmpty()) null else options[0]
    }

    /**
     * Gets the option with the specified name.
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
