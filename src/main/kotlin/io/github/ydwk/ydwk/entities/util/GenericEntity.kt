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
package io.github.ydwk.ydwk.entities.util

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK

interface GenericEntity {

    /** The main YDWK instance. */
    val ydwk: YDWK

    /** The json representation of this entity. */
    val json: JsonNode

    /**
     * Used to get the full json of the entity as a string.
     *
     * @return The full json of the entity as a string.
     */
    fun toJson(): String = json.toPrettyString()
}
