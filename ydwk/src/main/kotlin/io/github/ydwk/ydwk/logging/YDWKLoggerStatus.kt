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

enum class YDWKLoggerStatus(private vararg val severity: YDWKLoggerSeverity) {
    /** The [YDWKLoggerImpl] will log all messages except [WARN] and [ERROR] messages. */
    INFO(YDWKLoggerSeverity.INFO),

    /** The [YDWKLoggerImpl] will log all messages except [ERROR], [INFO] and [DEBUG] messages. */
    WARN(YDWKLoggerSeverity.WARN),

    /** The [YDWKLoggerImpl] will log all messages except [INFO] messages. */
    ERROR(YDWKLoggerSeverity.INFO, YDWKLoggerSeverity.WARN, YDWKLoggerSeverity.ERROR),

    /** The [YDWKLoggerImpl] will log all messages. */
    DEBUG(
        YDWKLoggerSeverity.INFO,
        YDWKLoggerSeverity.WARN,
        YDWKLoggerSeverity.ERROR,
        YDWKLoggerSeverity.DEBUG),

    /** The [YDWKLoggerImpl] will log no messages. */
    NONE();

    companion object {
        /** The default [YDWKLoggerStatus] is ALL. */
        val ALL: List<YDWKLoggerStatus> = listOf(INFO, WARN, ERROR)

        /**
         * Gets the [YDWKLoggerStatus] from the [String] value. If the [String] value is not a valid
         * [YDWKLoggerStatus], then [NONE] is returned.
         *
         * @param value The [String] value.
         * @return The [YDWKLoggerStatus] or [NONE] if the [String] value is not a valid
         */
        fun get(value: String): YDWKLoggerStatus =
            when (value) {
                "INFO" -> INFO
                "WARN" -> WARN
                "ERROR" -> ERROR
                "NONE" -> NONE
                else -> NONE
            }
    }

    /**
     * Gets the [YDWKLoggerStatus] as a [String].
     *
     * @return The [YDWKLoggerStatus] as a [String].
     */
    fun getStatus(): String = this.name

    /**
     * Gets the accosted [YDWKLoggerSeverity] for the [YDWKLoggerStatus].
     *
     * @return The [YDWKLoggerSeverity] for the [YDWKLoggerStatus].
     */
    fun getSeverity(): List<YDWKLoggerSeverity> = this.severity.toList()
}
