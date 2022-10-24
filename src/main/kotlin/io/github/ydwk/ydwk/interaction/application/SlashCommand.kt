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
package io.github.ydwk.ydwk.interaction.application

import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.interaction.sub.GenericCommandData
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.slash.SlashOptionGetter
import io.github.ydwk.ydwk.slash.SlashOptionType
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.SnowFlake
import java.util.concurrent.CompletableFuture

interface SlashCommand : SnowFlake, GenericCommandData {
    /**
     * Gets the name of the command.
     *
     * @return The name of the command.
     */
    val name: String

    /**
     * Gets the type of the command.
     *
     * @return The type of the command.
     */
    val type: ApplicationCommandType

    /**
     * Gets the guild where the interaction occurred.
     *
     * @return The guild where the interaction occurred.
     */
    val guild: Guild?

    /**
     * Gets the id of the user or message targeted by a user or message command
     *
     * @return The id of the user or message targeted by a user or message command
     */
    val targetId: GetterSnowFlake?

    /**
     * Gets the user who invoked the command.
     *
     * @return The user who invoked the command.
     */
    val user: User?

    /**
     * Gets the member who invoked the command.
     *
     * @return The member who invoked the command.
     */
    val member: Member?

    /**
     * Gets the Id of the application that this interaction is for.
     *
     * @return The Id of the application that this interaction is for.
     */
    val applicationId: GetterSnowFlake

    /**
     * Gets the type of this interaction.
     *
     * @return The type of this interaction.
     */
    val interactionType: InteractionType

    /**
     * Gets the channel that this interaction is for.
     *
     * @return The channel that this interaction is for.
     */
    val channel: TextChannel?

    /**
     * Gets the token of this interaction.
     *
     * @return The token of this interaction.
     */
    val token: String

    /**
     * Gets the version of this interaction.
     *
     * @return The version of this interaction.
     */
    val version: Int

    /**
     * Gets the message of this interaction.
     *
     * @return The message of this interaction.
     */
    val message: Message?

    /**
     * Gets bitwise set of permissions the app or bot has within the channel the interaction was
     * sent from
     *
     * @return bitwise set of permissions the app or bot has within the channel the interaction was
     * sent from
     */
    val permissions: Long?

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
     * @param tts Whether the reply should be sent with text-to-speech.
     * @param ephemeral Whether the reply should be ephemeral.
     * @return The reply.
     */
    fun reply(
        content: String,
        tts: Boolean = false,
        ephemeral: Boolean = false,
    ): CompletableFuture<Void>

    /**
     * Replies to an interaction.
     *
     * @param content The content of the reply.
     * @param ephemeral Whether the reply should be ephemeral.
     * @return The reply.
     */
    fun reply(content: String, ephemeral: Boolean = false): CompletableFuture<Void> =
        reply(content, false, ephemeral)

    /**
     * Replies to an interaction.
     *
     * @param embed The embed of the reply.
     * @param tts Whether the reply should be sent with text-to-speech.
     * @param ephemeral Whether the reply should be ephemeral.
     * @return The reply.
     */
    fun reply(
        embed: Embed,
        tts: Boolean = false,
        ephemeral: Boolean = false,
    ): CompletableFuture<Void>

    /**
     * Replies to an interaction.
     *
     * @param embed The embed of the reply.
     * @param ephemeral Whether the reply should be ephemeral.
     * @return The reply.
     */
    fun reply(embed: Embed, ephemeral: Boolean = false): CompletableFuture<Void> =
        reply(embed, false, ephemeral)

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
