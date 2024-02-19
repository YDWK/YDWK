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
package io.github.ydwk.yde.util

/**
 * A snowflake is a 64-bit integer that is used to uniquely identify a Discord entity.
 *
 * @see [DiscordDocs](https://discord.com/developers/docs/reference#snowflakes)
 */
interface SnowFlake {

    /**
     * The id of this snowflake as a String.
     *
     * @return the id of this snowflake as a [String]
     */
    val id: String
        get() = idAsLong.toString()

    /**
     * The id of this snowflake as a Long.
     *
     * @return the id of this snowflake as a [Long]
     */
    val idAsLong: Long

    /**
     * The timestamp of this snowflake. (Milliseconds since Discord Epoch, the first second of 2015
     * or 1420070400000.)
     *
     * @return the timestamp of this snowflake.
     */
    val asTimestamp: Long
        get() = (idAsLong shr 22) + 1420070400000

    /**
     * The worker id of this snowflake.
     *
     * @return the worker id of this snowflake.
     */
    val asWorkerId: Long
        get() = (idAsLong and 0x3E0000) shr 17

    /**
     * The process id of this snowflake.
     *
     * @return the process id of this snowflake.
     */
    val asProcessId: Long
        get() = (idAsLong and 0x1F000) shr 12

    /**
     * The increment of this snowflake.
     *
     * @return the increment of this snowflake.
     */
    val asIncrement: Long
        get() = idAsLong and 0xFFF
}
