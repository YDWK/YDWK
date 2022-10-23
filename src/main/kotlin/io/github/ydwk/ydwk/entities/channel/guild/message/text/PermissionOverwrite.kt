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
package io.github.ydwk.ydwk.entities.channel.guild.message.text

import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.SnowFlake

interface PermissionOverwrite : GenericEntity, SnowFlake {
    /**
     * Gets the type of this permission overwrite.
     *
     * @return the type of this permission overwrite.
     */
    val type: Int

    /**
     * Gets to allow of this permission overwrite.
     *
     * @return the allow of this permission overwrite.
     */
    val allow: String

    /**
     * Gets the deny of this permission overwrite.
     *
     * @return the deny of this permission overwrite.
     */
    val deny: String
}
