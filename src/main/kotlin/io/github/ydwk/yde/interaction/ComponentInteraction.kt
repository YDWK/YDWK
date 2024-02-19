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
package io.github.ydwk.yde.interaction

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.channel.TextChannel
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.message.Component
import io.github.ydwk.yde.interaction.message.ComponentInteractionData
import io.github.ydwk.yde.interaction.message.ComponentType
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.GetterSnowFlake

interface ComponentInteraction : GenericEntity {

    /**
     * The type of component interaction.
     *
     * @return The type of component interaction.
     */
    val type: ComponentType

    /**
     * The interaction token.
     *
     * @return The interaction token.
     */
    val interactionToken: String

    /**
     * The interaction id.
     *
     * @return the interaction id
     */
    val interactionId: GetterSnowFlake

    /**
     * The interaction type of this component.
     *
     * @return the interaction type of this component
     */
    val interactionType: InteractionType
        get() = InteractionType.MESSAGE_COMPONENT

    /**
     * The message corresponding to this component.
     *
     * @return The message corresponding to this component.
     */
    val message: Message

    /**
     * The member who triggered this component.
     *
     * @return The member who triggered this component.
     */
    val member: Member?

    /**
     * The user who triggered this component.
     *
     * @return The user who triggered this component.
     */
    val user: User?

    /**
     * The guild of this component.
     *
     * @return The guild of this component.
     */
    val guild: Guild?

    /**
     * The channel were this component was triggered.
     *
     * @return The channel were this component was triggered.
     */
    val channel: TextChannel?

    /**
     * The application id of this component.
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
