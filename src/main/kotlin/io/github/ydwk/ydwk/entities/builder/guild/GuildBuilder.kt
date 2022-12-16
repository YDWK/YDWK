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
package io.github.ydwk.ydwk.entities.builder.guild

import io.github.ydwk.ydwk.entities.Guild
import java.util.concurrent.CompletableFuture

/** Used to create a guild. */
interface GuildBuilder {

    /**
     * Sets the name of the guild.
     *
     * @param name the name of the guild
     */
    fun setName(name: String): GuildBuilder

    /**
     * Sets the description of the guild.
     *
     * @param description the description of the guild
     */
    fun setDescription(description: String): GuildBuilder

    /**
     * Builds the guild.
     *
     * @return a future which will contain the guild
     */
    fun build(): CompletableFuture<Guild>
}
