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
package io.github.ydwk.yde.impl.entities.channel.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.guild.forum.*
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.util.GetterSnowFlake

class GuildForumChannelImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override var topic: String?,
    override var template: String?,
    override var rateLimitPerUser: Int,
    override var permissionOverwrites: List<PermissionOverwrite>,
    override var nsfw: Boolean,
    override var lastMessageId: GetterSnowFlake?,
    override var channelFlags: ChannelFlag,
    override var defaultRateLimitPerUser: Int,
    override var defaultSortOrder: Int?,
    override var defaultReactionEmoji: DefaultReactionEmoji?,
    override var availableForumTags: List<ForumTag>,
    override var availableForumTagsIds: List<GetterSnowFlake>,
    override var defaultForumLayoutView: ForumLayoutType,
) : GuildForumChannel, GuildChannelImpl(yde.entityInstanceBuilder.buildGuildForumChannel(json))
