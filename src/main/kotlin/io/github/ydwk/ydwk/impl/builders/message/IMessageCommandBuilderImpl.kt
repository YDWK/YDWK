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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.impl.builders.message

import io.github.ydwk.ydwk.builders.message.IMessageCommandBuilder
import io.github.ydwk.ydwk.builders.message.MessageCommandBuilder
import io.github.ydwk.ydwk.impl.YDWKImpl

class IMessageCommandBuilderImpl(
    private val ydwk: YDWKImpl,
    private val guildIds: MutableList<String>,
    private val applicationId: String
) : IMessageCommandBuilder {
    private val messageCommands: MutableList<MessageCommandBuilder> = mutableListOf()

    override fun addMessageCommand(name: String): IMessageCommandBuilder {
        messageCommands.add(MessageCommandBuilder(name))
        return this
    }

    override fun addMessageCommand(messageCommand: MessageCommandBuilder): IMessageCommandBuilder {
        messageCommands.add(messageCommand)
        return this
    }

    override fun addMessageCommands(
        messageCommands: List<MessageCommandBuilder>
    ): IMessageCommandBuilder {
        this.messageCommands.addAll(messageCommands)
        return this
    }

    override fun addMessageCommands(
        vararg messageCommands: MessageCommandBuilder
    ): IMessageCommandBuilder {
        this.messageCommands.addAll(messageCommands)
        return this
    }

    override fun getMessageCommands(): List<MessageCommandBuilder> {
        return messageCommands
    }

    override fun removeMessageCommand(name: String): IMessageCommandBuilder {
        messageCommands.removeIf { it.name == name }
        return this
    }

    override fun removeMessageCommand(
        messageCommand: MessageCommandBuilder
    ): IMessageCommandBuilder {
        messageCommands.remove(messageCommand)
        return this
    }

    override fun removeMessageCommands(
        messageCommands: List<MessageCommandBuilder>
    ): IMessageCommandBuilder {
        this.messageCommands.removeAll(messageCommands)
        return this
    }

    override fun removeMessageCommands(
        vararg messageCommands: MessageCommandBuilder
    ): IMessageCommandBuilder {
        this.messageCommands.removeAll(messageCommands)
        return this
    }

    override fun removeAllMessageCommands(): IMessageCommandBuilder {
        messageCommands.clear()
        return this
    }

    override fun build() {
        MessageCommandSender(ydwk, guildIds, applicationId, messageCommands)
    }
}
