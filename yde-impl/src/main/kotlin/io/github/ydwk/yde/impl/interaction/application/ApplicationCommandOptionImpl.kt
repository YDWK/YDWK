/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.impl.interaction.application

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.builders.slash.SlashOptionType
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.interaction.application.ApplicationCommandOption

class ApplicationCommandOptionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override var name: String,
    override val type: SlashOptionType,
    override val value: JsonNode,
    override val options: List<ApplicationCommandOption>,
    override val focused: Boolean?
) :
    ApplicationCommandOption,
    ToStringEntityImpl<ApplicationCommandOption>(yde, ApplicationCommandOption::class.java)
