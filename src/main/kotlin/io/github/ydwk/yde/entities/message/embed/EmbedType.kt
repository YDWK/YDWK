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
package io.github.ydwk.yde.entities.message.embed

enum class EmbedType(private val type: String) {
    /** A generic embed rendered from embed attributes. */
    RICH("rich"),

    /** An image embed. */
    IMAGE("image"),

    /** A video embed. */
    VIDEO("video"),

    /** An animated gif image embed rendered as a video embed. */
    GIFV("gifv"),

    /** An article embed. */
    ARTICLE("article"),

    /** A link embed. */
    LINK("link"),

    /** An unknown embed type. */
    UNKNOWN("unknown");

    companion object {
        /**
         * The [EmbedType] from the given [type].
         *
         * @param type The type to get the [EmbedType] from.
         * @return The [EmbedType] from the given [type].
         */
        fun fromString(type: String): EmbedType {
            return values().firstOrNull { it.type == type } ?: UNKNOWN
        }
    }

    override fun toString(): String {
        return type
    }
}
