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

enum class MessageType(private val type: Int, private val isDeletable: Boolean) {
    DEFAULT(0, true),
    RECIPIENT_ADD(1, false),
    RECIPIENT_REMOVE(2, false),
    CALL(3, false),
    CHANNEL_NAME_CHANGE(4, false),
    CHANNEL_ICON_CHANGE(5, false),
    CHANNEL_PINNED_MESSAGE(6, true),
    USER_JOIN(7, true),
    GUILD_BOOST(8, true),
    GUILD_BOOST_TIER_1(9, true),
    GUILD_BOOST_TIER_2(10, true),
    GUILD_BOOST_TIER_3(11, true),
    CHANNEL_FOLLOW_ADD(12, true),
    GUILD_DISCOVERY_DISQUALIFIED(14, false),
    GUILD_DISCOVERY_REQUALIFIED(15, false),
    GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16, false),
    GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17, false),
    THREAD_CREATED(18, true),
    REPLY(19, true),
    CHAT_INPUT_COMMAND(20, true),
    THREAD_STARTER_MESSAGE(21, false),
    GUILD_INVITE_REMINDER(22, true),
    CONTEXT_MENU_COMMAND(23, true),

    /** Can only be deleted by members with MANAGE_MESSAGES permission. */
    AUTO_MODERATION_ACTION(24, true),

    /** An Unknown Message Type. */
    UNKNOWN(-1, false);

    companion object {
        /**
         * The [MessageType] of the provided [type].
         *
         * @param type The type to get the [MessageType] of.
         * @return The [MessageType] of the provided [type].
         */
        fun fromInt(type: Int): MessageType {
            return values().firstOrNull { it.type == type } ?: UNKNOWN
        }
    }

    /**
     * Weather or not the message type is deletable.
     *
     * @return Weather or not the message type is deletable.
     */
    fun isDeletable(): Boolean {
        return isDeletable
    }

    /**
     * The type of the message.
     *
     * @return The type of the message.
     */
    fun getType(): Int {
        return type
    }
}
