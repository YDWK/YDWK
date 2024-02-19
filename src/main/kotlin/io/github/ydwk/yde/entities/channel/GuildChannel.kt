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
package io.github.ydwk.yde.entities.channel

import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.channel.getter.guild.GuildChannelGetter
import io.github.ydwk.yde.entities.channel.guild.GuildCategory
import io.github.ydwk.yde.entities.guild.invite.InviteCreator
import io.github.ydwk.yde.util.NameAbleEntity

interface GuildChannel : Channel, NameAbleEntity {
    /**
     * The guild of this channel.
     *
     * @return the guild of this channel.
     */
    val guild: Guild

    /**
     * The position of this channel.
     *
     * @return the position of this channel.
     */
    var position: Int

    /**
     * The parent of this channel.
     *
     * @return the parent of this channel.
     */
    var parent: GuildCategory?

    /**
     * The channel getter which gives access to the channels of this guild.
     *
     * @return the channel getter which gives access to the channels of this guild.
     */
    val guildChannelGetter: GuildChannelGetter

    /**
     * Creates an invitation for this channel.
     *
     * @return the invite creator which can be used to create the invite.
     */
    val inviteCreator: InviteCreator
}
