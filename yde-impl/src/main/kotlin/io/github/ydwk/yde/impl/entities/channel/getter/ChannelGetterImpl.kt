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
package io.github.ydwk.yde.impl.entities.channel.getter

import io.github.ydwk.yde.entities.Channel
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.channel.GuildChannel
import io.github.ydwk.yde.entities.channel.getter.ChannelGetter
import io.github.ydwk.yde.impl.entities.ChannelImpl

internal class ChannelGetterImpl(channel: Channel) : ChannelImpl(channel), ChannelGetter {
    override fun asGuildChannel(): GuildChannel? {
        return isGuildChannel.let {
            if (it) {
                yde.entityInstanceBuilder.buildGuildChannel(json)
            } else {
                null
            }
        }
    }

    override fun asDmChannel(): DmChannel? {
        return isDmChannel.let {
            if (it) {
                yde.entityInstanceBuilder.buildDMChannel(json)
            } else {
                null
            }
        }
    }
}
