/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.realyusufismail.ydwk.ws.util

enum class CloseCode(val code: Int, val reason: String, val reconnect: Boolean = true) {
    UNKNOWN_OPCODE(
        4001,
        "You sent an invalid Gateway opcode or an invalid payload for an opcode. Don't do that!"),
    DECODE_ERROR(4002, "You sent an invalid payload to us. Make sure you follow the protocol."),
    NOT_AUTHENTICATED(4003, "You sent an invalid token. Make sure you follow the protocol."),
    AUTHENTICATION_FAILED(
        4004, "You sent an invalid token. Make sure you follow the protocol.", false),
    ALREADY_AUTHENTICATED(4005, "You sent more than one token. Make sure you follow the protocol."),
    INVALID_SEQ(
        4007,
        "The sequence sent when resuming the session was invalid. Reconnect and start a new session."),
    RATE_LIMITED(
        4008,
        "Woah nelly! You're sending payloads to us too quickly. Slow it down! You will be disconnected on receiving this."),
    SESSION_TIMEOUT(4009, "Your session timed out. Reconnect and start a new one.", false),
    INVALID_SHARD(
        4010,
        "You sent us an invalid shard when identifying. Make sure you follow the protocol.",
        false),
    SHARDING_REQUIRED(
        4011,
        "The session would have handled too many guilds - you are required to shard your connection in order to connect.",
        false),
    INVALID_API_VERSION(4012, "You sent an invalid version for the gateway.", false),
    INVALID_INTENTS(4013, "You sent an invalid intents. Make sure you follow the protocol.", false),
    DISALLOWED_INTENTS(
        4014,
        "You sent a disallowed intent for a Gateway Intent. You may have tried to specify an intent that you have not enabled or are not approved for. Examples include : GUILD_PRESENCES, MESSAGE_CONTENT, or GUILD_MEMBERS.",
        false),
    RECONNECT(4015, "Seems like the bot has disconnected. Will try to reconnect."),
    ZOMBIE_CONNECTION(
        1000,
        "The connection to the gateway has been lost. This is most likely due to a network error. Will try to reconnect."),
    UNKNOWN(4000, "We're not sure what went wrong. Will try to reconnect.");

    companion object {
        fun from(code: Int): CloseCode {
            return values().firstOrNull { it.code == code } ?: UNKNOWN
        }
    }

    override fun toString(): String {
        return reason
    }

    fun isReconnect(): Boolean {
        return reconnect
    }

    fun isDisconnect(): Boolean {
        return !reconnect
    }
}
