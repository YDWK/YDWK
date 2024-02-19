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
package io.github.ydwk.yde.entities.guild.ws

import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.GetterSnowFlake

interface WelcomeChannel : GenericEntity {
    /**
     * The channel's id.
     *
     * @return The channel's id.
     */
    val channelId: GetterSnowFlake

    /**
     * The description shown for the channel.
     *
     * @return The description shown for the channel.
     */
    var description: String

    /**
     * The emoji id, if the emoji is custom.
     *
     * @return The emoji id, if the emoji is custom.
     */
    val emojiId: GetterSnowFlake?

    /**
     * The emoji name if custom, or the unicode character if standard.
     *
     * @return The emoji name if custom, or the unicode character if standard.
     */
    var emojiName: String?
}
