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

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.GetterSnowFlake

interface MessageReference : GenericEntity {
    /**
     * The id of the message.
     *
     * @return The id of the message.
     */
    val messageId: GetterSnowFlake

    /**
     * The id of the channel.
     *
     * @return The id of the channel.
     */
    val channelId: GetterSnowFlake

    /**
     * The id of the guild.
     *
     * @return The id of the guild.
     */
    val guildId: GetterSnowFlake

    /**
     * The guild of the message.
     *
     * @return The guild of the message.
     */
    val guild: Guild?
}
