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
package io.github.ydwk.yde.entities.builder.guild

/** Creates entities which are guild specific. */
interface GuildEntitiesBuilder {

    /**
     * Creates a new role. This is used when creating a new guild as you don't need to specify a
     * guild id.
     *
     * @param name the name of the role
     * @return a new role builder
     * @see RoleBuilder
     */
    fun createRole(name: String): RoleBuilder

    /**
     * Creates a new role for a specific guild.
     *
     * @param name the name of the role
     * @param guildId the id of the guild
     * @return a new role builder
     */
    fun createRole(name: String, guildId: String): RoleBuilder

    /**
     * Creates a new channel. This is used when creating a new guild as you don't need to specify a
     * guild id.
     *
     * @param name the name of the channel
     * @return a new channel builder
     * @see ChannelBuilder
     */
    fun createChannel(name: String): ChannelBuilder

    /**
     * Creates a new channel for a specific guild.
     *
     * @param name the name of the channel
     * @param guildId the id of the guild
     * @return a new channel builder
     */
    fun createChannel(name: String, guildId: String): ChannelBuilder
}
