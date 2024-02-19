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
package io.github.ydwk.yde.entities.guild

import io.github.ydwk.yde.entities.guild.ws.WelcomeChannel
import io.github.ydwk.yde.entities.util.GenericEntity

/** This class is used to represent a guild welcome screen object. */
interface WelcomeScreen : GenericEntity {
    /**
     * The description of the Welcome Screen, if the guild has it enabled.
     *
     * @return The description of the Welcome Screen, if the guild has it enabled.
     */
    var description: String?

    /**
     * The channels shown in the Welcome Screen, if the guild has it enabled.
     *
     * @return The channels shown in the Welcome Screen, if the guild has it enabled.
     */
    var welcomeChannels: List<WelcomeChannel>
}
