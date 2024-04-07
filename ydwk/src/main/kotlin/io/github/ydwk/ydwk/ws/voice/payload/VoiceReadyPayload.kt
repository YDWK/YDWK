/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.ydwk.ws.voice.payload

import io.github.ydwk.ydwk.ws.voice.util.VoiceEncryption

/**
 * Represents the payload for voice ready event.
 *
 * @property ssrc The synchronization source identifier.
 * @property ip The IP address of the voice server.
 * @property port The port number of the voice server.
 * @property modes The list of supported voice modes.
 */
data class VoiceReadyPayload(
    val ssrc: Int,
    val ip: String,
    val port: Int,
    val modes: List<VoiceEncryption>
)
