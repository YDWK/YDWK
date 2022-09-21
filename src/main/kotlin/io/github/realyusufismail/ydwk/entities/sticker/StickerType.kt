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
package io.github.realyusufismail.ydwk.entities.sticker

enum class StickerType(val value: Int) {
    /** An official sticker in a pack, part of Nitro or in a removed purchasable pack */
    STANDARD(1),
    /** A sticker uploaded to a guild for the guild's members */
    GUILD(2),
    /** An unknown sticker type */
    UNKNOWN(-1);

    companion object {
        /** Gets the [StickerType] from the provided [value]. */
        fun fromValue(value: Int): StickerType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
