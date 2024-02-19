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
package io.github.ydwk.yde.util

import java.awt.Color

fun Color.getColourHex(): String {
    return String.format("#%02x%02x%02x", this.red, this.green, this.blue)
}

fun Color.getColourRGB(): String {
    return String.format("rgb(%d, %d, %d)", this.red, this.green, this.blue)
}

fun Color.getColourANSIEscape(): String {
    return String.format("\u001B[38;2;%d;%d;%dm", this.red, this.green, this.blue)
}
