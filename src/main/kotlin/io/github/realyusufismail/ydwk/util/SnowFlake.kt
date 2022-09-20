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
package io.github.realyusufismail.ydwk.util

interface SnowFlake {

    fun of(string: String): SnowFlake {
        return SnowFlakeReg(string.toLong())
    }

    fun of(id: Long): SnowFlake {
        return SnowFlakeReg(id)
    }

    /** @return The id of an object as a string */
    val id: String
        get() = idAsLong.toString()

    /** @return The id of an object as a long */
    val idAsLong: Long
}

internal class SnowFlakeReg(override val idAsLong: Long) : SnowFlake
