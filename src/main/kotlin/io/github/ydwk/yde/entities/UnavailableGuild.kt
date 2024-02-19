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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.SnowFlake

/**
 * Unavailable means that the guild data is currently not accessible. Discord knows it exists, but
 * the services can't access the data at the moment.
 */
interface UnavailableGuild : SnowFlake, GenericEntity {
    /**
     * Weather the guild is unavailable or not.
     *
     * @return true if the guild is unavailable.
     */
    var unavailable: Boolean
}
