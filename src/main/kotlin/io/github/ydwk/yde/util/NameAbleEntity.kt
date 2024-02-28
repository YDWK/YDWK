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
package io.github.ydwk.yde.util

import java.util.*

/**
 * An entity that has a name.
 */
interface NameAbleEntity {
    var name: String

    /**
     * Formats the name of the Discord entity.
     * Default format returns the name enclosed in backticks for Discord code formatting.
     */
    fun format(format: String = "`%s`"): String {
        return format.format(name)
    }
}