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
package io.github.ydwk.yde

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.Bot
import io.github.ydwk.yde.entities.User

interface EntityInstanceBuilder {

    /**
     * Used to build an instance of [User]
     *
     * @return [User] the user
     */
    fun buildUser(json: JsonNode): User

    /**
     * Used to build an instance of [Bot]
     *
     * @return [Bot] the bot
     */
    fun buildBot(json: JsonNode): Bot
}
