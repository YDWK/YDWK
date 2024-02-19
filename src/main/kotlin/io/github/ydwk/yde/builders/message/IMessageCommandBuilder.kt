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
package io.github.ydwk.yde.builders.message

interface IMessageCommandBuilder {

    /**
     * Adds a message command to the builder
     *
     * @param name The name of the message command
     * @return The [IMessageCommandBuilder] object that was added
     */
    fun addMessageCommand(name: String): IMessageCommandBuilder

    /**
     * Adds a new Message Command to the builder
     *
     * @param messageCommand The Message Command to add
     * @return The [IMessageCommandBuilder] object that was added
     */
    fun addMessageCommand(messageCommand: MessageCommandBuilder): IMessageCommandBuilder

    /**
     * Adds a list of Message Commands to the builder
     *
     * @param messageCommands The list of Message Commands to add
     * @return The [IMessageCommandBuilder] object that was added
     */
    fun addMessageCommands(messageCommands: List<MessageCommandBuilder>): IMessageCommandBuilder

    /**
     * Adds a list of Message Commands to the builder
     *
     * @param messageCommands The list of Message Commands to add
     * @return The [IMessageCommandBuilder] object that was added
     */
    fun addMessageCommands(vararg messageCommands: MessageCommandBuilder): IMessageCommandBuilder

    /**
     * All the Message Commands in the builder
     *
     * @return The list of Message Commands in the builder
     */
    fun getMessageCommands(): List<MessageCommandBuilder>

    /**
     * Removes a Message Command from the builder
     *
     * @param name The name of the Message Command to remove
     * @return The [IMessageCommandBuilder] object that was removed
     */
    fun removeMessageCommand(name: String): IMessageCommandBuilder

    /**
     * Removes a Message Command from the builder
     *
     * @param messageCommand The Message Command to remove
     * @return The [IMessageCommandBuilder] object that was removed
     */
    fun removeMessageCommand(messageCommand: MessageCommandBuilder): IMessageCommandBuilder

    /**
     * Removes a list of Message Commands from the builder
     *
     * @param messageCommands The list of Message Commands to remove
     * @return The [IMessageCommandBuilder] object that was removed
     */
    fun removeMessageCommands(messageCommands: List<MessageCommandBuilder>): IMessageCommandBuilder

    /**
     * Removes a list of Message Commands from the builder
     *
     * @param messageCommands The list of Message Commands to remove
     * @return The [IMessageCommandBuilder] object that was removed
     */
    fun removeMessageCommands(vararg messageCommands: MessageCommandBuilder): IMessageCommandBuilder

    /**
     * Removes all Message Commands from the builder
     *
     * @return The [IMessageCommandBuilder] object that was removed
     */
    fun removeAllMessageCommands(): IMessageCommandBuilder

    /** Builds the Message Commands */
    fun build()
}
