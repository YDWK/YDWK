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
package io.github.ydwk.yde.builders.user

import org.slf4j.helpers.CheckReturnValue

interface IUserCommandBuilder {

    /**
     * Adds a user command to the builder
     *
     * @param name The name of the user command
     * @return The [IUserCommandBuilder] object that was added
     */
    @CheckReturnValue fun addUserCommand(name: String): IUserCommandBuilder

    /**
     * Adds a new User Command to the builder
     *
     * @param userCommand The User Command to add
     */
    @CheckReturnValue fun addUserCommand(userCommand: UserCommandBuilder): IUserCommandBuilder

    /**
     * Lists of User Commands to the builder
     *
     * @param userCommands The list of User Commands to add
     */
    @CheckReturnValue
    fun addUserCommands(userCommands: List<UserCommandBuilder>): IUserCommandBuilder

    /**
     * Lists of User Commands to the builder
     *
     * @param userCommands The list of User Commands to add
     */
    @CheckReturnValue
    fun addUserCommands(vararg userCommands: UserCommandBuilder): IUserCommandBuilder

    /**
     * All the User Commands in the builder
     *
     * @return The list of User Commands in the builder
     */
    @CheckReturnValue fun getUserCommands(): List<UserCommandBuilder>

    /**
     * Removes a User Command from the builder
     *
     * @param userCommand The User Command to remove
     */
    @CheckReturnValue fun removeUserCommand(userCommand: UserCommandBuilder): IUserCommandBuilder

    /**
     * Removes a List of User Commands from the builder
     *
     * @param userCommands The List of User Commands to remove
     */
    @CheckReturnValue
    fun removeUserCommands(userCommands: List<UserCommandBuilder>): IUserCommandBuilder

    /**
     * Removes a List of User Commands from the builder
     *
     * @param userCommands The List of User Commands to remove
     */
    @CheckReturnValue
    fun removeUserCommands(vararg userCommands: UserCommandBuilder): IUserCommandBuilder

    /** Builds the User Commands */
    fun build()
}
