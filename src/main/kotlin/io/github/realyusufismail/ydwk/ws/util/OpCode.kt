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

enum class OpCode {
    /** An event was dispatched. */
    DISPATCH(0, false),
    /** Fired periodically by the client to keep the connection alive. */
    HEARTBEAT(1, true, false),
    /** Starts a new session during the initial handshake. */
    IDENTIFY(2),
    /** Update the client's presence. */
    PRESENCE(3),
    /** Used to join/leave or move between voice channels. */
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

    private var code: Int
    private var send: Boolean
    private var receive: Boolean

    constructor(code: Int, send: Boolean, receive: Boolean) {
        this.code = code
        this.send = send
        this.receive = receive
    }

    constructor(code: Int, send: Boolean) {
        this.code = code
        this.send = send
        this.receive = false
    }

    constructor(code: Int) {
        this.code = code
        this.send = true
        this.receive = true
    }

    fun getCode(): Int {
        return code
    }

    fun isSend(): Boolean {
        return send
    }

    fun isReceive(): Boolean {
        return receive
    }

    companion object {
        fun fromCode(code: Int): OpCode {
            for (opCode in values()) {
                if (opCode.getCode() == code) {
                    return opCode
                }
            }
            return UNKNOWN
        }
    }
}
