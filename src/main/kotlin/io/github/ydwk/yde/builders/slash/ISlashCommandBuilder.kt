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
package io.github.ydwk.yde.builders.slash

import org.slf4j.helpers.CheckReturnValue

interface ISlashCommandBuilder {

    /**
     * Adds a slash command to the builder
     *
     * @param name The name of the slash command
     * @param description The description of the slash command
     * @return The [ISlashCommandBuilder] object that was added
     */
    @CheckReturnValue fun addSlashCommand(name: String, description: String): ISlashCommandBuilder

    /**
     * Adds a new Slash Command to the builder
     *
     * @param slash The Slash Command to add
     */
    @CheckReturnValue fun addSlashCommand(slash: SlashCommandBuilder): ISlashCommandBuilder

    /**
     * Lists of Slash Commands to the builder
     *
     * @param slashes The list of Slash Commands to add
     */
    @CheckReturnValue fun addSlashCommands(slashes: List<SlashCommandBuilder>): ISlashCommandBuilder

    /**
     * Lists of Slash Commands to the builder
     *
     * @param slashes The list of Slash Commands to add
     */
    @CheckReturnValue
    fun addSlashCommands(vararg slashes: SlashCommandBuilder): ISlashCommandBuilder

    /**
     * All the Slash Commands in the builder
     *
     * @return The list of Slash Commands in the builder
     */
    @CheckReturnValue fun getSlashCommands(): List<SlashCommandBuilder>

    /**
     * Removes a Slash Command from the builder
     *
     * @param slash The Slash Command to remove
     */
    @CheckReturnValue fun removeSlashCommand(slash: SlashCommandBuilder): ISlashCommandBuilder

    /**
     * Removes a List of Slash Commands from the builder
     *
     * @param slashes The List of Slash Commands to remove
     */
    @CheckReturnValue
    fun removeSlashCommands(slashes: List<SlashCommandBuilder>): ISlashCommandBuilder

    /**
     * Removes a List of Slash Commands from the builder
     *
     * @param slashes The List of Slash Commands to remove
     */
    @CheckReturnValue
    fun removeSlashCommands(vararg slashes: SlashCommandBuilder): ISlashCommandBuilder

    /** Removes all Slash Commands from the builder */
    @CheckReturnValue fun removeAllSlashCommands(): ISlashCommandBuilder

    /** Builds the Slash Commands */
    fun build()
}
