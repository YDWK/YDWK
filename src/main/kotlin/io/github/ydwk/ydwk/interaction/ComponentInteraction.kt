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
package io.github.ydwk.ydwk.interaction

import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.Message
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.TextChannel
import io.github.ydwk.ydwk.entities.guild.Member
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.interaction.message.Component
import io.github.ydwk.ydwk.interaction.message.ComponentInteractionData
import io.github.ydwk.ydwk.interaction.sub.InteractionType
import io.github.ydwk.ydwk.util.GetterSnowFlake

interface ComponentInteraction : GenericEntity {

    /**
     * Gets the interaction token.
     *
     * @return The interaction token.
     */
    val interactionToken: String

    /**
     * Gets the interaction id.
     *
     * @return the interaction id
     */
    val interactionId: GetterSnowFlake

    /**
     * Gets the interaction type of this component.
     *
     * @return the interaction type of this component
     */
    val type: InteractionType

    /**
     * Gets the message corresponding to this component.
     *
     * @return The message corresponding to this component.
     */
    val message: Message

    /**
     * Gets the member who triggered this component.
     *
     * @return The member who triggered this component.
     */
    val member: Member?

    /**
     * Gets the user who triggered this component.
     *
     * @return The user who triggered this component.
     */
    val user: User?

    /**
     * Gets the guild of this component.
     *
     * @return The guild of this component.
     */
    val guild: Guild?

    /**
     * Gets the channel were this component was triggered.
     *
     * @return The channel were this component was triggered.
     */
    val channel: TextChannel?

    /**
     * Gets the application id of this component.
     *
     * @return The application id of this component.
     */
    val applicationId: GetterSnowFlake?

    /**
     * Gets a list of [Component]s that were sent with this component.
     *
     * @return A list of [Component]s that were sent with this component.
     */
    val components: List<Component>

    /**
     * Gets extra data sent with this component.
     *
     * @return Extra data sent with this component.
     */
    val data: ComponentInteractionData
}
