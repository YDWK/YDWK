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
package io.github.ydwk.ydwk.log

import io.github.ydwk.ydwk.util.getColourANSIEscape
import io.github.ydwk.ydwk.ws.util.formatInstant
import java.awt.Color
import java.time.Instant

fun main() {
    print("Hello, world!", Color.RED, Instant.now(), "INFO")
}

fun print(message: String, colour: Color, now: Instant, severity: String) {
    // message wixll be in the format of: [21:00] [severity] message(in colour)
    printWithColour(colour, "${formatInstant(now)} [$severity] $message")
}

fun printWithColour(colour: Color, message: String) {
    println(colour.getColourANSIEscape() + message + colour.getColourANSIEscape())
}
