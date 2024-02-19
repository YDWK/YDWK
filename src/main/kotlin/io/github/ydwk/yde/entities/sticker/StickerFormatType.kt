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

enum class StickerFormatType(private val value: Int) {
    /** The file is a PNG image. */
    PNG(1),

    /** The file is a TGS animation. */
    APNG(2),

    /** The file is a TGS animation. */
    LOTTIE(3),

    /** An unknown format. */
    UNKNOWN(-1);

    companion object {
        /**
         * Get the [StickerFormatType] from the given [value].
         *
         * @param value The value to get the [StickerFormatType] from.
         * @return The [StickerFormatType] from the given [value].
         */
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: UNKNOWN
    }

    /**
     * Get the value of the [StickerFormatType].
     *
     * @return The value of the [StickerFormatType].
     */
    fun getValue(): Int {
        return value
    }
}
