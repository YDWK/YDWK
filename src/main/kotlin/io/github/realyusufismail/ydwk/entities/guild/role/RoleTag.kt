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
package io.github.realyusufismail.ydwk.entities.guild.role

import io.github.realyusufismail.ydwk.entities.util.GenericEntity
import io.github.realyusufismail.ydwk.util.GetterSnowFlake

interface RoleTag : GenericEntity {
    /**
     * Gets the id of the bot this role belongs to.
     *
     * @return The id of the bot this role belongs to.
     */
    val botId: GetterSnowFlake?

    /**
     * Gets the id of the integration this role belongs to.
     *
     * @return The id of the integration this role belongs to.
     */
    val integrationId: GetterSnowFlake?
}
