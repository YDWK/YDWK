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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.channel.getter.ChannelGetter
import io.github.ydwk.yde.impl.entities.channel.getter.ChannelGetterImpl
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl

internal open class ChannelImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
    override val isGuildChannel: Boolean,
    override val isDmChannel: Boolean,
    override val type: ChannelType,
) : Channel, ToStringEntityImpl<Channel>(yde, Channel::class.java) {
    constructor(
        channel: Channel
    ) : this(
        channel.yde,
        channel.json,
        channel.idAsLong,
        channel.isGuildChannel,
        channel.isDmChannel,
        channel.type,
    )

    override val channelGetter: ChannelGetter
        get() = ChannelGetterImpl(this)
}
