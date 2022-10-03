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
package io.github.ydwk.ydwk.impl.slash

import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.rest.RestApiManager
import io.github.ydwk.ydwk.slash.Slash
import io.github.ydwk.ydwk.slash.SlashBuilder
import io.github.ydwk.ydwk.util.Checks
import java.util.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class SlashBuilderImpl(
    private val ydwk: YDWKImpl,
    private val guildIds: MutableList<String>,
    private val applicationId: String
) : SlashBuilder {
    private val slashCommands: MutableList<Slash> = mutableListOf()

    override fun addSlashCommand(slash: Slash): SlashBuilder {
        slashCommands.add(slash)
        return this
    }

    override fun addSlashCommands(slashes: List<Slash>): SlashBuilder {
        slashCommands.addAll(slashes)
        return this
    }

    override fun addSlashCommands(vararg slashes: Slash): SlashBuilder {
        slashCommands.addAll(slashes)
        return this
    }

    override fun getSlashCommands(): List<Slash> {
        return slashCommands
    }

    override fun removeSlashCommand(slash: Slash): SlashBuilder {
        slashCommands.remove(slash)
        return this
    }

    override fun removeSlashCommands(slashes: List<Slash>): SlashBuilder {
        slashCommands.removeAll(slashes)
        return this
    }

    override fun removeSlashCommands(vararg slashes: Slash): SlashBuilder {
        slashCommands.removeAll(slashes.toSet())
        return this
    }

    override fun removeAllSlashCommands(): SlashBuilder {
        slashCommands.clear()
        return this
    }

    override fun build() {
        val rest = ydwk.restApiManager

        slashCommands.forEach { it ->
            Checks.checkIfCapital(it.name, "Slash command name must be lowercase")
            Checks.checkLength(
                it.name, 32, "Slash command name can not be longer than 32 characters")
            Checks.checkLength(
                it.description,
                100,
                "Slash command description can not be longer than 100 characters")

            if (it.guildOnly && guildIds.isEmpty()) {
                guildIds.forEach { c ->
                    addGuildSlashCommand(rest, c, it.toJson().toPrettyString().toRequestBody())
                }
            } else {
                addGlobalSlashCommand(rest, it.toJson().toPrettyString().toRequestBody())
            }
        }
    }

    private fun addGuildSlashCommand(rest: RestApiManager, guildId: String, json: RequestBody) {
        rest
            .post(
                json,
                EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND,
                applicationId,
                guildId)
            .execute()
    }

    private fun addGlobalSlashCommand(rest: RestApiManager, json: RequestBody) {
        rest
            .post(json, EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND, applicationId)
            .execute()
    }
}