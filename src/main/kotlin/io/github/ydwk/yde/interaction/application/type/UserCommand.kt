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
package io.github.ydwk.yde.interaction.application.type

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.interaction.application.ApplicationCommand
import io.github.ydwk.yde.interaction.application.ApplicationCommandType
import io.github.ydwk.yde.interaction.reply.Repliable

interface UserCommand : ApplicationCommand, Repliable {
    /**
     * The type of the command.
     *
     * @return The type of the command.
     */
    override val type: ApplicationCommandType
        get() = ApplicationCommandType.USER

    /**
     * The user who the app command was invoked for.
     *
     * @return The user who the app command was invoked for.
     */
    val targetUser: User

    /**
     * The member who the app command was invoked for.
     *
     * @return The member who the app command was invoked for.
     */
    val targetMember: Member
}
