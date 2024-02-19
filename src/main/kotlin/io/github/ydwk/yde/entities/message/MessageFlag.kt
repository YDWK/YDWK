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

enum class MessageFlag(private val value: Long) {
    /** This message has been published to subscribed channels (via Channel Following). */
    CROSSPOSTED(1 shl 0),

    /** This message originated from a message in another channel (via Channel Following). */
    IS_CROSSPOST(1 shl 1),

    /** Do not include any embeds when serializing this message. */
    SUPPRESS_EMBEDS(1 shl 2),

    /** The source message for this crosspost has been deleted (via Channel Following). */
    SOURCE_MESSAGE_DELETED(1 shl 3),

    /** This message came from the urgent message system. */
    URGENT(1 shl 4),

    /** This message has an associated thread, with the same id as the message. */
    HAS_THREAD(1 shl 5),

    /** This message is only visible to the user who invoked the Interaction. */
    EPHEMERAL(1 shl 6),

    /** This message is an Interaction Response and the bot is "thinking". */
    LOADING(1 shl 7),

    /** This message failed to mention some roles and add their members to the thread. */
    FAILED_TO_MENTION_SOME_ROLES_IN_THREAD(1 shl 8),

    /** This message will not trigger push and desktop notifications. */
    SUPPRESS_NOTIFICATIONS(1 shl 12),

    /** An Unknown Message Flag. */
    UNKNOWN(-1);

    companion object {
        /**
         * The [MessageFlag] from the given value.
         *
         * @param value The value of the flag.
         * @return The [MessageFlag] with the given value.
         */
        fun fromLong(value: Long): MessageFlag {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * The value of the flag.
     *
     * @return The value of the flag.
     */
    fun getValue(): Long {
        return value
    }
}
