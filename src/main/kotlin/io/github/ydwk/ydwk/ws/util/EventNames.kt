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
package io.github.ydwk.ydwk.ws.util

enum class EventNames(private val eventName: String) {
    HELLO("HELLO"),
    READY("READY"),
    RESUMED("RESUMED"),
    RECONNECT("RECONNECT"),
    INVALID_SESSION("INVALID_SESSION"),
    APPLICATION_COMMAND_PERMISSIONS_UPDATE("APPLICATION_COMMAND_PERMISSIONS_UPDATE"),
    CHANNEL_CREATE("CHANNEL_CREATE"),
    CHANNEL_UPDATE("CHANNEL_UPDATE"),
    CHANNEL_DELETE("CHANNEL_DELETE"),
    CHANNEL_PINS_UPDATE("CHANNEL_PINS_UPDATE"),
    THREAD_CREATE("THREAD_CREATE"),
    THREAD_UPDATE("THREAD_UPDATE"),
    THREAD_DELETE("THREAD_DELETE"),
    THREAD_LIST_SYNC("THREAD_LIST_SYNC"),
    THREAD_MEMBERS_UPDATE("THREAD_MEMBERS_UPDATE"),
    GUILD_CREATE("GUILD_CREATE"),
    GUILD_UPDATE("GUILD_UPDATE"),
    GUILD_DELETE("GUILD_DELETE"),
    GUILD_BAN_ADD("GUILD_BAN_ADD"),
    GUILD_BAN_REMOVE("GUILD_BAN_REMOVE"),
    GUILD_EMOJIS_UPDATE("GUILD_EMOJIS_UPDATE"),
    GUILD_INTEGRATIONS_UPDATE("GUILD_INTEGRATIONS_UPDATE"),
    GUILD_MEMBER_ADD("GUILD_MEMBER_ADD"),
    GUILD_MEMBER_REMOVE("GUILD_MEMBER_REMOVE"),
    GUILD_MEMBER_UPDATE("GUILD_MEMBER_UPDATE"),
    GUILD_ROLE_CREATE("GUILD_ROLE_CREATE"),
    GUILD_ROLE_UPDATE("GUILD_ROLE_UPDATE"),
    GUILD_ROLE_DELETE("GUILD_ROLE_DELETE"),
    GUILD_SCHEDULED_EVENT_CREATE("GUILD_SCHEDULED_EVENT_CREATE"),
    GUILD_SCHEDULED_EVENT_UPDATE("GUILD_SCHEDULED_EVENT_UPDATE"),
    GUILD_SCHEDULED_EVENT_DELETE("GUILD_SCHEDULED_EVENT_DELETE"),
    GUILD_SCHEDULED_EVENT_USER_ADD("GUILD_SCHEDULED_EVENT_USER_ADD"),
    GUILD_SCHEDULED_EVENT_USER_REMOVE("GUILD_SCHEDULED_EVENT_USER_REMOVE"),
    INTEGRATION_CREATE("INTEGRATION_CREATE"),
    INTEGRATION_UPDATE("INTEGRATION_UPDATE"),
    INTEGRATION_DELETE("INTEGRATION_DELETE"),
    INTERACTION_CREATE("INTERACTION_CREATE"),
    INVITE_CREATE("INVITE_CREATE"),
    INVITE_DELETE("INVITE_DELETE"),
    MESSAGE_CREATE("MESSAGE_CREATE"),
    MESSAGE_UPDATE("MESSAGE_UPDATE"),
    MESSAGE_DELETE("MESSAGE_DELETE"),
    MESSAGE_DELETE_BULK("MESSAGE_DELETE_BULK"),
    MESSAGE_REACTION_ADD("MESSAGE_REACTION_ADD"),
    MESSAGE_REACTION_REMOVE("MESSAGE_REACTION_REMOVE"),
    MESSAGE_REACTION_REMOVE_ALL("MESSAGE_REACTION_REMOVE_ALL"),
    PRESENCE_UPDATE("PRESENCE_UPDATE"),
    TYPING_START("TYPING_START"),
    USER_UPDATE("USER_UPDATE"),
    VOICE_STATE_UPDATE("VOICE_STATE_UPDATE"),
    VOICE_SERVER_UPDATE("VOICE_SERVER_UPDATE"),
    WEBHOOKS_UPDATE("WEBHOOKS_UPDATE"),
    UNKNOWN("UNKNOWN");

    companion object {
        /**
         * Get the [EventNames] from the event name.
         *
         * @param eventName The event name.
         * @return The [EventNames] with the event name.
         */
        fun fromString(eventName: String): EventNames {
            return values().firstOrNull { it.eventName == eventName } ?: UNKNOWN
        }
    }

    /**
     * Get the event name.
     *
     * @return The event name.
     */
    fun getEventName(): String {
        return eventName
    }
}
