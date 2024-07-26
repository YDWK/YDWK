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
package io.github.ydwk.yde.entities.message

enum class AttachmentFlags(private val value: Int) {
    /** This attachment has been edited using the remix feature on mobile. */
    IS_REMIX(1 shl 2),
    /** An unknown flag. */
    UNKNOWN(-1);

    companion object {
        fun getValue(value: Int): AttachmentFlags {
            return when (value) {
                4 -> IS_REMIX
                else -> UNKNOWN
            }
        }
    }
}