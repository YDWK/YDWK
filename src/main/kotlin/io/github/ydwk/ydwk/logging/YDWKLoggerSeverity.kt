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
package io.github.ydwk.ydwk.logging

import java.awt.Color

enum class YDWKLoggerSeverity(private var colour: Color) {
    /** The [INFO] severity. */
    INFO(Color(0, 0, 0)),

    /** The [DEBUG] severity. */
    DEBUG(Color(0, 255, 0)),

    /** The [WARN] severity. */
    WARN(Color(255, 0, 0)),

    /** The [ERROR] severity. */
    ERROR(Color(255, 0, 255)),

    /** The [FATAL] severity. */
    FATAL(Color(255, 255, 255));

    companion object {
        /**
         * Gets the [YDWKLoggerSeverity] from the [String] value.
         *
         * @param value The [String] value.
         * @return The [YDWKLoggerSeverity] or [INFO] if the [String] value is not a valid
         *   [YDWKLoggerSeverity].
         */
        fun get(value: String): YDWKLoggerSeverity =
            when (value) {
                "INFO" -> INFO
                "DEBUG" -> DEBUG
                "WARN" -> WARN
                "ERROR" -> ERROR
                "FATAL" -> FATAL
                else -> INFO
            }
    }

    fun getSeverity(): String {
        return this.name
    }

    fun getColour(): Color {
        return this.colour
    }

    /**
     * Sets the colour of the [YDWKLoggerSeverity].
     *
     * @param colour The colour of the [YDWKLoggerSeverity].
     * @return The [YDWKLoggerSeverity] with the new colour.
     */
    fun setColour(colour: Color): Color {
        this.colour = colour
        return this.colour
    }
}
