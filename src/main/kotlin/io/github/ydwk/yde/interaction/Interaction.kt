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

import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.Message
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.sub.GenericCommandData
import io.github.ydwk.yde.interaction.sub.InteractionType
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.SnowFlake

interface Interaction : SnowFlake, GenericEntity {
    /**
     * The Id of the application that this interaction is for.
     *
     * @return The Id of the application that this interaction is for.
     */
    val applicationId: GetterSnowFlake

    /**
     * The type of this interaction.
     *
     * @return The type of this interaction.
     */
    val type: InteractionType

    /**
     * The data of this interaction.
     *
     * @return The data of this interaction.
     */
    val data: GenericCommandData?

    /**
     * The guild that this interaction is for.
     *
     * @return The guild that this interaction is for.
     */
    val guild: Guild?

    /**
     * The channel that this interaction is for.
     *
     * @return The channel that this interaction is for.
     */
    val channel: Channel?

    /**
     * The member who invoked this interaction.
     *
     * @return The member who invoked this interaction.
     */
    val member: Member?

    /**
     * The user who invoked this interaction.
     *
     * @return The user who invoked this interaction.
     */
    val user: User

    /**
     * The token of this interaction.
     *
     * @return The token of this interaction.
     */
    val token: String

    /**
     * The version of this interaction.
     *
     * @return The version of this interaction.
     */
    val version: Int

    /**
     * The message of this interaction.
     *
     * @return The message of this interaction.
     */
    val message: Message?

    /**
     * Gets bitwise set of permissions the app or bot has within the channel the interaction was
     * sent from
     *
     * @return bitwise set of permissions the app or bot has within the channel the interaction was
     *   sent from
     */
    val permissions: Long?

    /**
     * The selected language of the invoking user
     *
     * @return the selected language of the invoking user
     */
    val locale: String?

    /** The Guild's preferred locale, if invoked in a guild */
    val guildLocale: String?
}
