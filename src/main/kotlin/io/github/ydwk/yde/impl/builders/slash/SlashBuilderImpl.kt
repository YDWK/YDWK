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
package io.github.ydwk.yde.impl.builders.slash

import io.github.ydwk.yde.builders.slash.ISlashCommandBuilder
import io.github.ydwk.yde.builders.slash.SlashCommandBuilder
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.ydwk.impl.builders.slash.SlashInfoSender

class SlashBuilderImpl(
    private val yde: YDEImpl,
    private val guildIds: MutableList<String>,
    private val applicationId: String
) : ISlashCommandBuilder {
    private val slashCommands: MutableList<SlashCommandBuilder> = mutableListOf()

    override fun addSlashCommand(name: String, description: String): ISlashCommandBuilder {
        slashCommands.add(SlashCommandBuilder(name, description))
        return this
    }

    override fun addSlashCommand(slash: SlashCommandBuilder): ISlashCommandBuilder {
        slashCommands.add(slash)
        return this
    }

    override fun addSlashCommands(slashes: List<SlashCommandBuilder>): ISlashCommandBuilder {
        slashCommands.addAll(slashes)
        return this
    }

    override fun addSlashCommands(vararg slashes: SlashCommandBuilder): ISlashCommandBuilder {
        slashCommands.addAll(slashes)
        return this
    }

    override fun getSlashCommands(): List<SlashCommandBuilder> {
        return slashCommands
    }

    override fun removeSlashCommand(slash: SlashCommandBuilder): ISlashCommandBuilder {
        slashCommands.remove(slash)
        return this
    }

    override fun removeSlashCommands(slashes: List<SlashCommandBuilder>): ISlashCommandBuilder {
        slashCommands.removeAll(slashes)
        return this
    }

    override fun removeSlashCommands(vararg slashes: SlashCommandBuilder): ISlashCommandBuilder {
        slashCommands.removeAll(slashes.toSet())
        return this
    }

    override fun removeAllSlashCommands(): ISlashCommandBuilder {
        slashCommands.clear()
        return this
    }

    override fun build() {
        SlashInfoSender(yde, guildIds, applicationId, slashCommands)
    }
}
