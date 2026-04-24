/*
 * Copyright 2024-2026 YDWK inc.
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
package io.github.ydwk.ydwk.ws.voice.util

enum class VoiceOpcode(val code: Int, val client: Boolean = true, val server: Boolean = false) {
    /** Begin a voice websocket connection. */
    IDENTIFY(0, true, false),

    /** Select the voice protocol. */
    SELECT_PROTOCOL(1, true, false),

    /** Complete the websocket handshake. */
    READY(2, false, true),

    /** Keep the websocket connection alive. */
    HEARTBEAT(3, true, true),

    /** Describe the session. */
    SESSION_DESCRIPTION(4, false, true),

    /** Indicate which users are speaking. */
    SPEAKING(5, true, true),

    /** Sent to acknowledge a received client heartbeat. */
    HEARTBEAT_ACK(6, false, true),

    /** Resume a connection. */
    RESUME(7, true, false),

    /** Sent immediately after connecting, contains the heartbeat_interval to use. */
    HELLO(8, false, true),

    /** Acknowledge a successful session resume. */
    RESUMED(9, false, true),

    /** A client has connected to the voice channel. */
    CLIENT_CONNECT(11, false, true),

    /** A client has disconnected from the voice channel. */
    CLIENT_DISCONNECT(13, false, true),

    /** Sent to indicate the media sink wants specific streams (server to client). */
    MEDIA_SINK_WANTS(15, false, true),

    /** Sent to describe session flags (server to client). */
    FLAGS(17, false, true),

    /** Speed test initiation/result (both directions). */
    SPEED_TEST(18, true, true),

    /** Client platform information (client to server). */
    PLATFORM(19, true, false),

    // DAVE (Discord's E2EE protocol) opcodes — introduced 2024
    /** Prepare a DAVE protocol transition. */
    DAVE_PROTOCOL_PREPARE_TRANSITION(21, false, true),

    /** Execute a pending DAVE protocol transition. */
    DAVE_PROTOCOL_EXECUTE_TRANSITION(22, false, true),

    /** DAVE protocol ready (server to client). */
    DAVE_PROTOCOL_READY(23, false, true),

    /** Prepare a DAVE MLS epoch. */
    DAVE_PROTOCOL_PREPARE_EPOCH(24, false, true),

    /** MLS external sender public key (server to client). */
    DAVE_MLS_EXTERNAL_SENDER(25, false, true),

    /** MLS key package (client to server). */
    DAVE_MLS_KEY_PACKAGE(26, true, false),

    /** MLS proposals (server to client). */
    DAVE_MLS_PROPOSALS(27, false, true),

    /** MLS commit and welcome message. */
    DAVE_MLS_COMMIT_WELCOME(28, true, false),

    /** Announce commit transition (server to client). */
    DAVE_MLS_ANNOUNCE_COMMIT_TRANSITION(29, false, true),

    /** MLS welcome for joining members (server to client). */
    DAVE_MLS_WELCOME(30, false, true),

    /** MLS invalid commit/welcome (server to client). */
    DAVE_MLS_INVALID_COMMIT_WELCOME(31, false, true),

    /** An Unknown opcode has been received. */
    UNKNOWN(-1, false, false);

    companion object {
        /**
         * Get the [VoiceOpcode] from the given [code].
         *
         * @param code The code to get the [VoiceOpcode] from.
         * @return The [VoiceOpcode] with the given [code].
         */
        fun getOpCode(code: Int): VoiceOpcode {
            for (opCode in entries) {
                if (opCode.code == code) {
                    return opCode
                }
            }
            return UNKNOWN
        }
    }
}
