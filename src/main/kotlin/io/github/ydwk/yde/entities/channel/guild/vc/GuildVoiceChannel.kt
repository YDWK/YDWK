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
package io.github.ydwk.yde.entities.channel.guild.vc

import io.github.ydwk.yde.entities.channel.GuildChannel

interface GuildVoiceChannel : GuildChannel {
    /**
     * The bitrate (in bits) of the voice channel.
     *
     * @return the bitrate.
     */
    var bitrate: Int

    /**
     * The user limit of the voice channel.
     *
     * @return the user limit.
     */
    var userLimit: Int

    /**
     * The rate limit per user of the voice channel.
     *
     * @return the rate limit per user.
     */
    var rateLimitPerUser: Int
}
