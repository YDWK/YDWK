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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.ws.util

enum class CloseCode(
    val code: Int,
    val reason: String,
    val reconnect: Boolean = true,
) {
    TERMINATION(
        1008,
        "This endpoint is terminating the connection because it has received a message that violates its policy",
        false),
    UNKNOWN_ERROR(4000, "We're not sure what went wrong. Try reconnecting?"),
    UNKNOWN_OPCODE(4001, "You sent an invalid Gateway opcode. Don't do that!"),
    DECODE_ERROR(4002, "You sent an invalid payload to us. Don't do that!"),
    NOT_AUTHENTICATED(4003, "You sent us a payload prior to identifying."),
    AUTHENTICATION_FAILED(
        4004, "The account token sent with your identify payload is incorrect.", false),
    ALREADY_AUTHENTICATED(4005, "You sent more than one identify payload. Don't do that!"),
    INVALID_SEQ(
        4007,
        "The sequence sent when resuming the session was invalid. Reconnect and start a new session."),
    RATE_LIMITED(4008, "Woah nelly! You're sending payloads to us too quickly. Slow it down!"),
    SESSION_TIMED_OUT(4009, "Your session timed out. Reconnect and start a new one."),
    INVALID_SHARD(4010, "You sent us an invalid shard when identifying.", false),
    SHARDING_REQUIRED(
        4011,
        "The session would have handled too many guilds - you are required to shard your connection in order to connect.",
        false),
    INVALID_VERSION(4012, "You sent an invalid version for the gateway.", false),
    INVALID_INTENTS(
        4013,
        "You sent an invalid intent for a Gateway Intent. You may have incorrectly calculated the bitwise value.",
        false),
    DISALLOWED_INTENTS(
        4014,
        "You sent a disallowed intent for a Gateway Intent. You may have tried to specify an intent that you have not enabled or are not whitelisted for.",
        false),
    RECONNECT(4999, "Sent when opcode 7 is received"),
    INVALID_SESSION(4998, "The session has been invalidated. We will reconnect you automatically."),
    MISSED_HEARTBEAT(4997, "You missed too many heartbeats, reconnecting."),
    UNKNOWN(4999, "Unknown error", false);

    companion object {
        fun from(code: Int): CloseCode {
            return values().firstOrNull { it.code == code } ?: UNKNOWN
        }
    }

    override fun toString(): String {
        return "CloseCode(code=$code, reason='$reason', reconnect=$reconnect)"
    }
}
