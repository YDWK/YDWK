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
package io.github.ydwk.yde.entities.message

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.SnowFlake

interface MessageInteraction : GenericEntity, SnowFlake {
    /**
     * The type of interaction.
     *
     * @return The type of interaction.
     */
    val type: InteractionType

    /**
     * The name of the interaction command.
     *
     * @return The name of the interaction command.
     */
    val name: String

    /**
     * The user who invoked the interaction.
     *
     * @return The user who invoked the interaction.
     */
    val user: User

    /**
     * The member who invoked the interaction.
     *
     * @return The member who invoked the interaction.
     */
    val member: Member?
}
