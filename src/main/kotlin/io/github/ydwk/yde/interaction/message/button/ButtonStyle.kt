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
package io.github.ydwk.yde.interaction.message.button

enum class ButtonStyle(private val type: Int) {
    /** Creates a primary button with the color purple. */
    PRIMARY(1),
    /** Creates a secondary button with the color grey. */
    SECONDARY(2),
    /** Creates a success button with the color green. */
    SUCCESS(3),
    /** Creates a danger button with the color red. */
    DANGER(4),
    /** Creates a link button with the color grey and the sign of a link. */
    LINK(5);

    companion object {
        /**
         * The [ButtonStyle] with the specified [type].
         *
         * @param type The type of the button.
         * @return The [ButtonStyle] with the specified [type].
         */
        fun fromInt(type: Int): ButtonStyle {
            return values().first { it.type == type }
        }
    }

    /**
     * The type of the button.
     *
     * @return The type of the button.
     */
    fun getType(): Int {
        return type
    }
}
