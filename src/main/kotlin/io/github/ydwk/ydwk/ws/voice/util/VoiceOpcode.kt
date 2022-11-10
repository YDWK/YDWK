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
    /** A client has disconnected from the voice channel. */
    CLIENT_DISCONNECT(13, false, true)
}
