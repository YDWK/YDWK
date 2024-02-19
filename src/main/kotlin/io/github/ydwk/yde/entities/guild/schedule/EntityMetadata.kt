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
package io.github.ydwk.yde.entities.guild.schedule

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.GetterSnowFlake

interface EntityMetadata : GenericEntity {
    /**
     * The scheduled event id which the user subscribed to.
     *
     * @return the scheduled event id which the user subscribed to.
     */
    val scheduledEventId: GetterSnowFlake

    /**
     * The user which subscribed to an event.
     *
     * @return the user which subscribed to an event.
     */
    val user: User

    /**
     * The guild member data for this user for the guild which this event belongs to, if any.
     *
     * @return the guild member data for this user for the guild which this event belongs to, if
     *   any.
     */
    val member: Member?
}
