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
package io.github.ydwk.ydwk.interaction.sub

import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.guild.Role
import io.github.ydwk.ydwk.entities.message.Attachment
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.GetterSnowFlake

interface InteractionResolvedData : GenericEntity {
    /**
     * Gets the users resolved by this interaction.
     *
     * @return The users resolved by this interaction.
     */
    val users: Map<GetterSnowFlake, User>

    /**
     * Gets the members resolved by this interaction.
     *
     * @return The members resolved by this interaction.
     */
    val members: Map<GetterSnowFlake, Member>

    /**
     * Gets the roles resolved by this interaction.
     *
     * @return The roles resolved by this interaction.
     */
    val roles: Map<GetterSnowFlake, Role>

    /**
     * Gets the channels resolved by this interaction.
     *
     * @return The channels resolved by this interaction.
     */
    val channels: Map<GetterSnowFlake, Channel>

    /**
     * Gets the messages resolved by this interaction.
     *
     * @return The messages resolved by this interaction.
     */
    val messages: Map<GetterSnowFlake, Message>

    /**
     * Gets the attachments resolved by this interaction.
     *
     * @return The attachments resolved by this interaction.
     */
    val attachments: Map<GetterSnowFlake, Attachment>
}
