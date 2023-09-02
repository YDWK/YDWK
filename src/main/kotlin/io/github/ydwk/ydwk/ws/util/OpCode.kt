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

enum class OpCode(
    val code: Int,
    private val send: Boolean = true,
    private val receive: Boolean = false,
) {
    /** An event was dispatched. */
    DISPATCH(0, false),

    /** Fired periodically by the client to keep the connection alive. */
    HEARTBEAT(1, true, false),

    /** Starts a new session during the initial handshake. */
    IDENTIFY(2),

    /** Update the client's presence. */
    PRESENCE(3),

    /** Joins/leaves or moves between voice channels. */
    VOICE_STATE(4),

    /** Resume a previous session that was disconnected. */
    RESUME(6),

    /** You should attempt to reconnect and resume immediately. */
    RECONNECT(7, false),

    /** Request information about offline guild members in a large guild. */
    REQUEST_GUILD_MEMBERS(8),

    /** The session has been invalidated. You should reconnect and identify/resume accordingl */
    INVALID_SESSION(9, false),

    /** Sent immediately after connecting, contains the heartbeat_interval to use. */
    HELLO(10, false),

    /** Sent in response to receiving a heartbeat to acknowledge that it has been received. */
    HEARTBEAT_ACK(11, false),

    /** For future use or unknown opcodes. */
    UNKNOWN(-1, false, false);

    companion object {
        /**
         * Get the [OpCode] from the given [code].
         *
         * @param code The code to get the [OpCode] from.
         * @return The [OpCode] from the given [code].
         */
        fun fromInt(code: Int): OpCode {
            for (opCode in entries) {
                if (opCode.code == code) {
                    return opCode
                }
            }
            return UNKNOWN
        }
    }
}
