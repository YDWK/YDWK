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
package io.github.ydwk.yde.entities.sticker

enum class StickerType(private val value: Int) {
    /** An official sticker in a pack, part of Nitro or in a removed purchasable pack. */
    STANDARD(1),

    /** A sticker uploaded to a guild for the guild's members. */
    GUILD(2),

    /** An unknown sticker type. */
    UNKNOWN(-1);

    companion object {
        /**
         * The [StickerType] from the provided [value].
         *
         * @param value The value to get the [StickerType] from.
         * @return The [StickerType] from the provided [value].
         */
        fun fromInt(value: Int): StickerType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    /**
     * The value of the [StickerType].
     *
     * @return The value of the [StickerType].
     */
    fun getValue(): Int {
        return value
    }
}
