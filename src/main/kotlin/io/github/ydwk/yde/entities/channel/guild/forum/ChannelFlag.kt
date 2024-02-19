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
package io.github.ydwk.yde.entities.channel.guild.forum

enum class ChannelFlag(private val value: Long) {
    /** This thread is pinned to the top of its parent GUILD_FORUM channel. */
    PINNED(1 shl 1),

    /**
     * Whether a tag is required to be specified when creating a thread in a GUILD_FORUM channel.
     * Tags are specified in the applied_tags field.
     */
    REQUIRE_TAG(1 shl 4),

    /** An unknown flag. */
    UNKNOWN(-1);

    companion object {
        /**
         * Gets the [ChannelFlag] from the provided [value].
         *
         * @param value The value to get the [ChannelFlag] from.
         * @return The [ChannelFlag] from the provided [value].
         */
        fun fromValue(value: Long): ChannelFlag {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * The value of this [ChannelFlag].
     *
     * @return The value of this [ChannelFlag].
     */
    fun getValue(): Long {
        return value
    }
}
