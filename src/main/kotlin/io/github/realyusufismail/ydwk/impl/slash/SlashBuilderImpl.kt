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
package io.github.realyusufismail.ydwk.impl.slash

import io.github.realyusufismail.ydwk.impl.YDWKImpl
import io.github.realyusufismail.ydwk.rest.EndPoint
import io.github.realyusufismail.ydwk.rest.RestApiManager
import io.github.realyusufismail.ydwk.slash.Slash
import io.github.realyusufismail.ydwk.slash.SlashBuilder
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

        val jsons = mutableListOf<RequestBody>()
        slashCommands.forEach { it -> jsons.add(it.toJson().asText().toRequestBody()) }

        if (guildIds.isNotEmpty()) {
            addGuildSlashCommands(rest, jsons)
        } else {
            addGlobalSlashCommands(rest, jsons)
        }
    }

    private fun addGuildSlashCommands(rest: RestApiManager, jsons: List<RequestBody>) {
        guildIds.forEach { guildId ->
            jsons.forEach { json -> addGuildSlashCommand(rest, guildId, json) }
        }
    }

    private fun addGuildSlashCommand(rest: RestApiManager, guildId: String, json: RequestBody) {
        rest.post(
            json, EndPoint.ApplicationCommandsEndpoint.CREATE_GUILD_COMMAND, applicationId, guildId)
    }

    private fun addGlobalSlashCommands(rest: RestApiManager, jsons: List<RequestBody>) {
        jsons.forEach { json -> addGlobalSlashCommand(rest, json) }
    }

    private fun addGlobalSlashCommand(rest: RestApiManager, json: RequestBody) {
        rest.post(json, EndPoint.ApplicationCommandsEndpoint.CREATE_GLOBAL_COMMAND, applicationId)
    }
}
