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
package io.github.ydwk.yde.entities.channel.guild.forum

import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.util.GetterSnowFlake

interface GuildForumChannel : GuildChannel {
    /**
     * The topic of this channel.
     *
     * @return the topic of this channel.
     */
    var topic: String?

    /**
     * The template of this channel.
     *
     * @return the template of this channel.
     */
    var template: String?

    /**
     * The rate limit per user of this channel.
     *
     * @return the rate limit per user of this channel.
     */
    var rateLimitPerUser: Int

    /**
     * The permission overwrites of this channel.
     *
     * @return the permission overwrites of this channel.
     */
    var permissionOverwrites: List<PermissionOverwrite>

    /**
     * Whether this channel is nsfw.
     *
     * @return whether this channel is nsfw.
     */
    var nsfw: Boolean

    /**
     * The last message id of this channel.
     *
     * @return the last message id of this channel.
     */
    var lastMessageId: GetterSnowFlake?

    /**
     * The channel flags of this channel.
     *
     * @return the channel flags of this channel.
     */
    var channelFlags: ChannelFlag

    /**
     * The default rate limit per user of this channel.
     *
     * @return the default rate limit per user of this channel.
     */
    var defaultRateLimitPerUser: Int

    /**
     * The default sort order of this channel.
     *
     * @return the default sort order of this channel.
     */
    var defaultSortOrder: Int?

    /**
     * The default reaction Emoji of this channel.
     *
     * @return the default reaction Emoji of this channel.
     */
    var defaultReactionEmoji: DefaultReactionEmoji?

    /**
     * The list of available forum tags of this channel.
     *
     * @return the list of available forum tags of this channel.
     */
    var availableForumTags: List<ForumTag>

    /**
     * The list of available forum tags ids of this channel.
     *
     * @return the list of available forum tags ids of this channel.
     */
    var availableForumTagsIds: List<GetterSnowFlake>

    /**
     * The default forum layout view used to display posts in GUILD_FORUM channels. Defaults to 0,
     * which indicates a layout view has not been set by a channel admin.
     *
     * @return the default forum layout view used to display posts in GUILD_FORUM channels.
     */
    var defaultForumLayoutView: ForumLayoutType
}
