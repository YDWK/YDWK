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
package io.github.ydwk.ydwk.evm.listeners

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.event.events.interaction.AutoCompleteSlashCommandEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.ModelEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.PingEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.button.ButtonClickEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.message.MessageCommandEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.selectmenu.*
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.textinput.TextInputEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.user.UserCommandEvent

interface InteractionListeners : IEventListener {
    /**
     * Listens to SlashCommandEvent
     *
     * @param event The SlashCommandEvent
     */
    fun onSlashCommand(event: SlashCommandEvent) {}

    /**
     * Listens to UserCommandEvent
     *
     * @param event The UserCommandEvent
     */
    fun onUserCommand(event: UserCommandEvent) {}

    /**
     * Listens to MessageCommandEvent
     *
     * @param event The MessageCommandEvent
     */
    fun onMessageCommand(event: MessageCommandEvent) {}

    /**
     * Listens to AutoCompleteSlashCommandEvent
     *
     * @param event The AutoCompleteSlashCommandEvent
     */
    fun onAutoCompleteSlashCommand(event: AutoCompleteSlashCommandEvent) {}

    /**
     * Listens to ButtonClickEvent
     *
     * @param event The ButtonClickEvent
     */
    fun onButtonClick(event: ButtonClickEvent) {}

    /**
     * Listens to StringSelectMenuEvent
     *
     * @param event The StringSelectMenuEvent
     */
    fun onStringSelectMenu(event: StringSelectMenuEvent) {}

    /**
     * Listens to UserSelectMenuEvent
     *
     * @param event The UserSelectMenuEvent
     */
    fun onUserSelectMenu(event: UserSelectMenuEvent) {}

    /**
     * Listens to RoleSelectMenuEvent
     *
     * @param event The RoleSelectMenuEvent
     */
    fun onRoleSelectMenu(event: RoleSelectMenuEvent) {}

    /**
     * Listens to ChannelSelectMenuEvent
     *
     * @param event The ChannelSelectMenuEvent
     */
    fun onChannelSelectMenu(event: ChannelSelectMenuEvent) {}

    /**
     * Listens to MemberSelectMenuEvent
     *
     * @param event The MemberSelectMenuEvent
     */
    fun onMemberSelectMenu(event: MemberSelectMenuEvent) {}

    /**
     * Listens to Text Input Event
     *
     * @param event The Text Input Event
     */
    fun onTextInput(event: TextInputEvent) {}

    /**
     * Listens to Model Event
     *
     * @param event The Model Event
     */
    fun onModel(event: ModelEvent) {}

    /**
     * Listens to Ping Event
     *
     * @param event The Ping Event
     */
    fun onPing(event: PingEvent) {}

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is SlashCommandEvent -> onSlashCommand(event)
            is UserCommandEvent -> onUserCommand(event)
            is MessageCommandEvent -> onMessageCommand(event)
            is AutoCompleteSlashCommandEvent -> onAutoCompleteSlashCommand(event)
            is ButtonClickEvent -> onButtonClick(event)
            is StringSelectMenuEvent -> onStringSelectMenu(event)
            is UserSelectMenuEvent -> onUserSelectMenu(event)
            is RoleSelectMenuEvent -> onRoleSelectMenu(event)
            is ChannelSelectMenuEvent -> onChannelSelectMenu(event)
            is MemberSelectMenuEvent -> onMemberSelectMenu(event)
            is TextInputEvent -> onTextInput(event)
            is ModelEvent -> onModel(event)
            is PingEvent -> onPing(event)
        }
    }
}
