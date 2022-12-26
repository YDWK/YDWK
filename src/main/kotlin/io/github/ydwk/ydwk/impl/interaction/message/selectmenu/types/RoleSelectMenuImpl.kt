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
package io.github.ydwk.ydwk.impl.interaction.message.selectmenu.types

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.ydwk.impl.interaction.message.selectmenu.SelectMenuImpl
import io.github.ydwk.ydwk.interaction.message.selectmenu.types.RoleSelectMenu
import io.github.ydwk.ydwk.util.GetterSnowFlake

class RoleSelectMenuImpl(
    ydwk: YDWK,
    json: JsonNode,
    interactionId: GetterSnowFlake,
) : RoleSelectMenu, SelectMenuImpl(ydwk, json, interactionId) {
    constructor(
        componentInteractionImpl: ComponentInteractionImpl
    ) : this(
        componentInteractionImpl.ydwk,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId)

    override val selectedRoles: List<Role>
        get() =
            json["data"]["resolved"]["roles"].map {
                guild?.getRoleById(it["id"].asText())
                    ?: throw IllegalStateException("Role not found")
            }
}
