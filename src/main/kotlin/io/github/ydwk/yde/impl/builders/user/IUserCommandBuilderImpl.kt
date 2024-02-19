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
package io.github.ydwk.yde.impl.builders.user

import io.github.ydwk.yde.builders.user.IUserCommandBuilder
import io.github.ydwk.yde.builders.user.UserCommandBuilder
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.ydwk.impl.builders.user.UserCommandSender

class IUserCommandBuilderImpl(
    private val yde: YDEImpl,
    private val guildIds: MutableList<String>,
    private val applicationId: String
) : IUserCommandBuilder {
    private val userCommands: MutableList<UserCommandBuilder> = mutableListOf()

    override fun addUserCommand(name: String): IUserCommandBuilder {
        userCommands.add(UserCommandBuilder(name))
        return this
    }

    override fun addUserCommand(userCommand: UserCommandBuilder): IUserCommandBuilder {
        userCommands.add(userCommand)
        return this
    }

    override fun addUserCommands(userCommands: List<UserCommandBuilder>): IUserCommandBuilder {
        this.userCommands.addAll(userCommands)
        return this
    }

    override fun addUserCommands(vararg userCommands: UserCommandBuilder): IUserCommandBuilder {
        this.userCommands.addAll(userCommands)
        return this
    }

    override fun getUserCommands(): List<UserCommandBuilder> {
        return userCommands
    }

    override fun removeUserCommand(userCommand: UserCommandBuilder): IUserCommandBuilder {
        userCommands.remove(userCommand)
        return this
    }

    override fun removeUserCommands(userCommands: List<UserCommandBuilder>): IUserCommandBuilder {
        this.userCommands.removeAll(userCommands)
        return this
    }

    override fun removeUserCommands(vararg userCommands: UserCommandBuilder): IUserCommandBuilder {
        this.userCommands.removeAll(userCommands.toSet())
        return this
    }

    override fun build() {
        UserCommandSender(yde, guildIds, applicationId, userCommands)
    }
}
