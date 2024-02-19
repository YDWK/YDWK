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
package io.github.ydwk.yde.impl.entities.channel.guild

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.guild.forum.*
import io.github.ydwk.yde.entities.channel.guild.message.text.PermissionOverwrite
import io.github.ydwk.yde.impl.entities.channel.guild.forum.DefaultReactionEmojiImpl
import io.github.ydwk.yde.impl.entities.channel.guild.forum.ForumTagImpl
import io.github.ydwk.yde.util.GetterSnowFlake

class GuildForumChannelImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : GuildForumChannel, GuildChannelImpl(yde, json, idAsLong) {

    override var topic: String? = if (json.has("topic")) json["topic"].asText() else null

    override var template: String? = if (json.has("template")) json["template"].asText() else null

    override var rateLimitPerUser: Int =
        if (json.has("rate_limit_per_user")) json["rate_limit_per_user"].asInt() else 0

    override var permissionOverwrites: List<PermissionOverwrite> =
        if (json.has("permission_overwrites"))
            json["permission_overwrites"].map {
                PermissionOverwriteImpl(yde, it, it["id"].asLong())
            }
        else emptyList()

    override var nsfw: Boolean = json["nsfw"].asBoolean()

    override var lastMessageId: GetterSnowFlake? =
        if (json.has("last_message_id")) GetterSnowFlake.of(json["last_message_id"].asLong())
        else null

    override var channelFlags: ChannelFlag = ChannelFlag.fromValue(json["channel_flags"].asLong())

    override var defaultRateLimitPerUser: Int = json["default_rate_limit_per_user"].asInt()

    override var defaultSortOrder: Int? =
        if (json.has("default_sort_order")) json["default_sort_order"].asInt() else null

    override var defaultReactionEmoji: DefaultReactionEmoji? =
        if (json.has("default_reaction_emoji"))
            DefaultReactionEmojiImpl(yde, json["default_reaction_emoji"])
        else null

    override var availableForumTags: List<ForumTag> =
        if (json.has("available_forum_tags"))
            json["available_forum_tags"].map { ForumTagImpl(yde, it, it["id"].asLong()) }
        else emptyList()

    override var availableForumTagsIds: List<GetterSnowFlake> =
        if (json.has("available_forum_tags"))
            json["available_forum_tags"].map { GetterSnowFlake.of(it["id"].asLong()) }
        else emptyList()

    override var defaultForumLayoutView: ForumLayoutType =
        ForumLayoutType.fromValue(json["default_forum_layout_view"].asInt())
}
