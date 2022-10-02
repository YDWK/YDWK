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
package io.github.ydwk.ydwk.cache

enum class CacheType(private val value: String) {
    GUILD("guild"),
    USER("user"),
    CHANNEL("channel"),
    ROLE("role"),
    EMOJI("emoji"),
    MESSAGE("message"),
    STICKER("sticker"),
    VOICE_STATE("voice_state"),
    MEMBER("member"),
    APPLICATION_COMMAND("application_command"),
    APPLICATION("application");

    companion object {
        /** Get the [CacheType] from a [String] type. */
        fun fromString(string: String): CacheType? {
            return values().firstOrNull { it.name.equals(string, true) }
        }
    }

    override fun toString(): String {
        return value
    }
}
