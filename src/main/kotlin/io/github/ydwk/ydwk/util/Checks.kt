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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.util

class Checks {
    companion object {
        fun checkNotNull(obj: Any?, message: String) {
            if (obj == null) {
                throw NullPointerException(message)
            }
        }

        fun checkLength(obj: String, length: Int, message: String) {
            if (obj.length >= length) {
                throw IllegalArgumentException(message)
            }
        }

        fun checkLength(obj: CharSequence, length: Int, message: String) {
            if (obj.length >= length) {
                throw IllegalArgumentException(message)
            }
        }

        fun checkLength(obj: String, min: Int, max: Int, message: String) {
            if (obj.length < min || obj.length > max) {
                throw IllegalArgumentException(message)
            }
        }

        fun checkIfCapital(name: String, s: String) {
            if (name[0].isUpperCase()) {
                throw IllegalArgumentException(s)
            }
        }
    }
}
