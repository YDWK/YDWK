/*
 * Copyright 2022 YDWK inc.
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

import java.time.Instant

class YDWKLoggerImpl(
    private val manager: YDWKLogManager,
    val message: String,
    private val classToLog: String? = null,
) : YDWKLogger {
    private var currentLoggerSeverity: YDWKLoggerSeverity = YDWKLoggerSeverity.INFO

    override fun setSeverity(severity: YDWKLoggerSeverity): YDWKLogger {
        this.currentLoggerSeverity = severity
        return this
    }

    override fun log() {
        val enabledLoggerStatus = manager.getEnabledLoggerStatus()

        when (currentLoggerSeverity) {
            YDWKLoggerSeverity.INFO -> {
                if (enabledLoggerStatus.contains(YDWKLoggerStatus.INFO)) {
                    manager.print(
                        message,
                        currentLoggerSeverity.getColour(),
                        Instant.now(),
                        currentLoggerSeverity.getSeverity(),
                        classToLog)
                }
            }
            YDWKLoggerSeverity.WARN -> {
                if (enabledLoggerStatus.contains(YDWKLoggerStatus.WARN)) {
                    manager.print(
                        message,
                        currentLoggerSeverity.getColour(),
                        Instant.now(),
                        currentLoggerSeverity.getSeverity(),
                        classToLog)
                }
            }
            YDWKLoggerSeverity.ERROR -> {
                if (enabledLoggerStatus.contains(YDWKLoggerStatus.ERROR)) {
                    manager.print(
                        message,
                        currentLoggerSeverity.getColour(),
                        Instant.now(),
                        currentLoggerSeverity.getSeverity(),
                        classToLog)
                }
            }
            YDWKLoggerSeverity.DEBUG -> {
                if (enabledLoggerStatus.contains(YDWKLoggerStatus.DEBUG)) {
                    manager.print(
                        message,
                        currentLoggerSeverity.getColour(),
                        Instant.now(),
                        currentLoggerSeverity.getSeverity(),
                        classToLog)
                }
            }
            YDWKLoggerSeverity.FATAL -> {
                if (enabledLoggerStatus.contains(YDWKLoggerStatus.ERROR)) {
                    manager.print(
                        message,
                        currentLoggerSeverity.getColour(),
                        Instant.now(),
                        currentLoggerSeverity.getSeverity(),
                        classToLog)
                }
            }
        }
    }
}
