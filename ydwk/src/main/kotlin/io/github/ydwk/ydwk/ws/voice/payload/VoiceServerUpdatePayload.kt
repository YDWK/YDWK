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

import io.github.ydwk.yde.entities.Guild

/**
 * Represents a payload containing information about a voice server update.
 *
 * @param token The voice connection token.
 * @param guild The guild associated with the voice server.
 * @param endpoint The endpoint for the voice server host.
 */
data class VoiceServerUpdatePayload(val token: String, val guild: Guild, val endpoint: String?)
