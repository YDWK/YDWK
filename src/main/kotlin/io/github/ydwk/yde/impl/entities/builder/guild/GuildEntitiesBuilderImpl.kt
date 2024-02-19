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
package io.github.ydwk.yde.impl.entities.builder.guild

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.builder.guild.ChannelBuilder
import io.github.ydwk.yde.entities.builder.guild.GuildEntitiesBuilder
import io.github.ydwk.yde.entities.builder.guild.RoleBuilder

class GuildEntitiesBuilderImpl(val yde: YDE) : GuildEntitiesBuilder {
    override fun createRole(name: String): RoleBuilder {
        return RoleBuilderImpl(yde, null, name)
    }

    override fun createRole(name: String, guildId: String): RoleBuilder {
        return RoleBuilderImpl(yde, guildId, name)
    }

    override fun createChannel(name: String): ChannelBuilder {
        return ChannelBuilderImpl(yde, null, name)
    }

    override fun createChannel(name: String, guildId: String): ChannelBuilder {
        return ChannelBuilderImpl(yde, guildId, name)
    }
}
